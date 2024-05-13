package sn.sonatel.dsi.ins.ftsirc.scripts.inventaires;

import java.io.IOException;
import java.util.*;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.service.OLTService;
import sn.sonatel.dsi.ins.ftsirc.service.ONTService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.dto.ONTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.OLTMapperImpl;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.ONTMapper;

@Component
public class InventaireONT implements CommandLineRunner {

    @Autowired
    OLTService oltService;

    @Autowired
    ONTService ontService;

    @Autowired
    OLTMapperImpl oltMapper;

    @Autowired
    ONTMapper ontMapper;

    @Override
    public void run(String... args) throws Exception {
        //                                Long id = Long.parseLong("2489");
        //                                List<ONTDTO> listONTs;
        //                                Optional<OLTDTO> oltdto = oltService.findOne(id);
        //                                OLTDTO ontdto = oltdto.orElseThrow();
        //                                listONTs = getAllONTOnOLT(ontdto);
        //                                ontService.saveListONT(ontMapper.toEntity(listONTs));
        //        ontService.updateALLONTS();
    }

    public static List<ONTDTO> getAllONTOnOLT(OLTDTO olt) {
        List<ONTDTO> listONTs = new ArrayList<>();

        System.out.println("debut de l'inventaire");

        // startTime = time.time();

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
                return (List<ONTDTO>) result;
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

                    ONTDTO ontdto = new ONTDTO();
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
                        System.out.println(
                            "ponIndex: " +
                            ponIndex +
                            ", ontId: " +
                            ontId +
                            ", index: " +
                            index +
                            ", numberPhone: " +
                            numberPhone +
                            ", slot: " +
                            slot +
                            ", pon: " +
                            pon +
                            ", " +
                            binary_
                        );
                        ontdto.setOlt(olt);
                        ontdto.setIndex(index);
                        ontdto.setServiceId(numberPhone);
                        ontdto.setSlot(String.valueOf(slot));
                        ontdto.setPon(String.valueOf(pon));
                        ontdto.setPonIndex(String.valueOf(ponIndex));
                        ontdto.setOntIP(String.valueOf(ontId));
                        listONTs.add(ontdto);
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

                        System.out.println(
                            "ponIndex: " +
                            ponIndex +
                            ", shelf: " +
                            shelf +
                            ", index: " +
                            index +
                            ", IP: " +
                            olt.getIp() +
                            ", numberPhone: " +
                            serviceId +
                            ", slot: " +
                            slot +
                            ", pon: " +
                            pon +
                            ", " +
                            _oid
                        );

                        ontdto.setOlt(olt);
                        ontdto.setIndex(String.valueOf(index));
                        ontdto.setServiceId(serviceId);
                        ontdto.setSlot(String.valueOf(slot));
                        ontdto.setPon(String.valueOf(pon));
                        ontdto.setPonIndex(String.valueOf(ponIndex));
                        ontdto.setOntIP(String.valueOf(ontId));
                        listONTs.add(ontdto);
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
            System.out.println("Fin 1 inventaire");
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

    public void getPowerOLT(String serviceId) {
        ONT ont = ontService.findByServiceId(serviceId).get();

        if (!ont) {}
    }
}
