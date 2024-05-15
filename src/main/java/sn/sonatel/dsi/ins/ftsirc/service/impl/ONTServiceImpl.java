package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.repository.OLTRepository;
import sn.sonatel.dsi.ins.ftsirc.repository.ONTRepository;
import sn.sonatel.dsi.ins.ftsirc.service.ONTService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.ONTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.ONTMapper;

/**
 * Service Implementation for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.ONT}.
 */
@Service
@Transactional
public class ONTServiceImpl implements ONTService {

    private final Logger log = LoggerFactory.getLogger(ONTServiceImpl.class);

    private final ONTRepository oNTRepository;
    private final OLTRepository oltRepository;
    private final ONTMapper oNTMapper;

    public ONTServiceImpl(ONTRepository oNTRepository, OLTRepository oltRepository, ONTMapper oNTMapper) {
        this.oNTRepository = oNTRepository;
        this.oltRepository = oltRepository;
        this.oNTMapper = oNTMapper;
    }

    @Override
    public ONTDTO save(ONTDTO oNTDTO) {
        log.debug("Request to save ONT : {}", oNTDTO);
        ONT oNT = oNTMapper.toEntity(oNTDTO);
        oNT = oNTRepository.save(oNT);
        return oNTMapper.toDto(oNT);
    }
    public List<ONTDTO> saveListONT(List<ONT> listONTs) {
        log.debug("Request to save ONTs : {}");
        oNTRepository.saveAll(listONTs);
        return oNTMapper.toDto(listONTs);
    }

    @Override
    public ONTDTO update(ONTDTO oNTDTO) {
        log.debug("Request to update ONT : {}", oNTDTO);
        ONT oNT = oNTMapper.toEntity(oNTDTO);
        oNT = oNTRepository.save(oNT);
        return oNTMapper.toDto(oNT);
    }

    @Override
    public void updateALLONTS() {
        List<OLT> oltList = oltRepository.findAll();

        for (OLT olt : oltList) {
            List<ONT> listONTs = new ArrayList<>();
            listONTs = getAllONTOnOLT(olt.getId());
            for (ONT newONT : listONTs) {
                ONT oldONT = oNTRepository.findByServiceId(newONT.getServiceId());

                // check if newONT exist in ONT table
                if (oldONT == null) {
                    this.save(oNTMapper.toDto(newONT));
                }
                // check if oltONT and newONT have a same informations
                else if (oldONT.getOlt().equals(newONT.getOlt()) || oldONT.getPonIndex().equals(newONT.getPonIndex())) {
                    continue;
                } else {
                    // check good ONT

                    // update
                    this.update(oNTMapper.toDto(newONT));
                    System.out.println("ONT saving " + newONT.getServiceId());
                }
            }
        }
    }

    @Override
    public Optional<ONTDTO> partialUpdate(ONTDTO oNTDTO) {
        log.debug("Request to partially update ONT : {}", oNTDTO);

        return oNTRepository
            .findById(oNTDTO.getId())
            .map(existingONT -> {
                oNTMapper.partialUpdate(existingONT, oNTDTO);

                return existingONT;
            })
            .map(oNTRepository::save)
            .map(oNTMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ONTDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ONTS");
        return oNTRepository.findAll(pageable).map(oNTMapper::toDto);
    }

    public Page<ONTDTO> findAllWithEagerRelationships(Pageable pageable) {
        return oNTRepository.findAllWithEagerRelationships(pageable).map(oNTMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ONTDTO> findOne(Long id) {
        log.debug("Request to get ONT : {}", id);
        return oNTRepository.findOneWithEagerRelationships(id).map(oNTMapper::toDto);
    }

    public List<ONT> getAllONTOnOLT(Long id) {
        OLT olt = oltRepository.findById(id).get();
        List<ONT> listONTs = new ArrayList<>();

        System.out.println("debut de l'inventaire");

        String[] oids = { "1.3.6.1.4.1.2011.6.128.1.1.2.43.1.9", "1.3.6.1.4.1.637.61.1.6.5.1.1" };
        System.out.println("inventaire de l'OLT " + olt.getLibelle() + " (" + olt.getVendeur() + ")");
        TransportMapping<?> transport = null;
        try {
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(olt.getVendeur().equals("NOKIA") ? "t1HAI2nai" : "OLT@osn_read"));
            target.setAddress(new UdpAddress(olt.getIp() + "/161"));
            target.setRetries(2);
            target.setTimeout(1500);
            target.setVersion(SnmpConstants.version2c);
            OID oid = new OID(olt.getVendeur().equals("NOKIA") ? oids[1] : oids[0]);
            Map<String, String> result = new TreeMap<>();

            transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            transport.listen();
            TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());
            List<TreeEvent> events = treeUtils.getSubtree(target, new OID(oid));

            if (events == null || events.size() == 0) {
                System.out.println("Error: Unable to read table...");
                return listONTs;
            }

            for (TreeEvent event : events) {
                if (event == null) {
                    continue;
                }
                if (event.isError()) {
                    System.out.println("Error: table OID [" + oid + "] " + event.getErrorMessage());
                    continue;
                }

                VariableBinding[] varBindings = event.getVariableBindings();
                if (varBindings == null || varBindings.length == 0) {
                    continue;
                }
                for (VariableBinding varBinding : varBindings) {
                    if (varBinding == null) {
                        continue;
                    }
                    String binary_ = Integer.toBinaryString(Integer.parseInt(String.valueOf(varBinding.getOid().last())));

                    ONT ont = new ONT();
                    if (olt.getVendeur().equals("NOKIA")) {
                        int len_level = binary_.length() - 21;
                        //                          int level = Integer.parseInt(binary_.substring(0, len_level).substring(len_level - 4), 2);
                        int len_pon = binary_.length() - 16;
                        int pon = Integer.parseInt(binary_.substring(0, len_pon).substring(len_pon - 5), 2) + 1;
                        int len_slot = binary_.length() - 25;
                        System.out.println("len_slot" + binary_.length());
                        System.out.println("slot" + binary_.substring(0, len_slot));
                        int slot = Integer.parseInt(binary_.substring(0, len_slot), 2) - 1;
                        int len_ont = binary_.length() - 9;
                        int ontId = Integer.parseInt(binary_.substring(0, len_ont).substring(len_ont - 7), 2) + 1;
                        String index = String.valueOf(varBinding.getOid().last());
                        long ponIndex = (long) ((slot + 1) * Math.pow(2, 25) + 13 * Math.pow(2, 21) + (pon - 1) * Math.pow(2, 16));
                        String numberPhone = varBinding.getVariable().toString().replace(" ", "");
                        ont.setOlt(olt);
                        ont.setIndex(index);
                        ont.setServiceId(numberPhone);
                        ont.setSlot(String.valueOf(slot));
                        ont.setPon(String.valueOf(pon));
                        ont.setPonIndex(String.valueOf(ponIndex));
                        ont.setOntID(String.valueOf(ontId));
                        listONTs.add(ont);
                    } else if (olt.getVendeur().equals("HUAWEI")) {
                        String _oid = String.valueOf(varBinding.getOid());
                        String[] _oidList = _oid.split("\\.");
                        String ontId = _oidList[_oidList.length - 1];
                        Long index = Long.parseLong(_oidList[_oidList.length - 2]);
                        String serviceId = String.valueOf(varBinding.getVariable());
                        int slot = Integer.parseInt(Long.toBinaryString(index).substring(13, 19), 2);
                        //                            System.out.println(Long.toBinaryString(index).substring(19, 24));
                        int shelf = Integer.parseInt(Long.toBinaryString(index).substring(5, 11), 2);

                        int pon = Integer.parseInt(Long.toBinaryString(index).substring(19, 24), 2);

                        long ponIndex = (long) (125 * Math.pow(2, 25) +
                            shelf * Math.pow(2, 19) +
                            slot * Math.pow(2, 13) +
                            pon * Math.pow(2, 8));
                        ont.setOlt(olt);
                        ont.setIndex(String.valueOf(index));
                        ont.setServiceId(serviceId);
                        ont.setSlot(String.valueOf(slot));
                        ont.setPon(String.valueOf(pon));
                        ont.setPonIndex(String.valueOf(ponIndex));
                        ont.setOntID(String.valueOf(ontId));
                        listONTs.add(ont);
                    } else {
                        System.out.println("La longueur binaire depasse 25 ou n'est pas de Huawei ni de Nokia");
                    }

                    System.out.println("Numero: " + varBinding.getVariable());
                    result.put("." + varBinding.getOid().toString(), varBinding.getVariable().toString());
                }
            }
            snmp.close();

            return listONTs;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Fin inventaire");
        return listONTs;
    }

    @Transactional(readOnly = true)
    public ONT findByServiceId(String serviceId) {
        log.debug("Request to get ONT : {}", serviceId);
        return oNTRepository.findByServiceId(serviceId);
    }


    @Override
    public void delete(Long id) {
        log.debug("Request to delete ONT : {}", id);
        oNTRepository.deleteById(id);
    }
}
