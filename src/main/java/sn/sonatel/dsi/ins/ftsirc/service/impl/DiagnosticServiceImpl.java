package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.*;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.repository.DiagnosticRepository;
import sn.sonatel.dsi.ins.ftsirc.repository.ONTRepository;
import sn.sonatel.dsi.ins.ftsirc.service.DiagnosticService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.DiagnosticDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.DiagnosticMapper;

/**
 * Service Implementation for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic}.
 */
@Service
@Transactional
public class DiagnosticServiceImpl implements DiagnosticService {

    private final Logger log = LoggerFactory.getLogger(DiagnosticServiceImpl.class);

    private final DiagnosticRepository diagnosticRepository;

    private final DiagnosticMapper diagnosticMapper;
    private final ONTRepository ontRepository;

    public DiagnosticServiceImpl(
        DiagnosticRepository diagnosticRepository,
        DiagnosticMapper diagnosticMapper,
        ONTRepository ontRepository
    ) {
        this.diagnosticRepository = diagnosticRepository;
        this.diagnosticMapper = diagnosticMapper;
        this.ontRepository = ontRepository;
    }

    @Override
    public DiagnosticDTO save(DiagnosticDTO diagnosticDTO) {
        log.debug("Request to save Diagnostic : {}", diagnosticDTO);
        Diagnostic diagnostic = diagnosticMapper.toEntity(diagnosticDTO);
        diagnostic = diagnosticRepository.save(diagnostic);
        return diagnosticMapper.toDto(diagnostic);
    }

    @Override
    public DiagnosticDTO update(DiagnosticDTO diagnosticDTO) {
        log.debug("Request to update Diagnostic : {}", diagnosticDTO);
        Diagnostic diagnostic = diagnosticMapper.toEntity(diagnosticDTO);
        diagnostic = diagnosticRepository.save(diagnostic);
        return diagnosticMapper.toDto(diagnostic);
    }

    @Override
    public Optional<DiagnosticDTO> partialUpdate(DiagnosticDTO diagnosticDTO) {
        log.debug("Request to partially update Diagnostic : {}", diagnosticDTO);

        return diagnosticRepository
            .findById(diagnosticDTO.getId())
            .map(existingDiagnostic -> {
                diagnosticMapper.partialUpdate(existingDiagnostic, diagnosticDTO);

                return existingDiagnostic;
            })
            .map(diagnosticRepository::save)
            .map(diagnosticMapper::toDto);
    }

    public Page<DiagnosticDTO> findAllWithEagerRelationships(Pageable pageable) {
        return diagnosticRepository.findAllWithEagerRelationships(pageable).map(diagnosticMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DiagnosticDTO> findOne(Long id) {
        log.debug("Request to get Diagnostic : {}", id);
        return diagnosticRepository.findOneWithEagerRelationships(id).map(diagnosticMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Diagnostic : {}", id);
        diagnosticRepository.deleteById(id);
    }

    public String diagnosticFiberCut(String serviceId) {
        try {
            ONT ont = ontRepository.findByServiceId(serviceId);

            String index = ont.getIndex();
            String ip = ont.getOlt().getIp();
            String ontId = ont.getOntIP();
            String vendeur = ont.getOlt().getVendeur();

            String currentAlarmList = getCurrentAlarms(ip, ontId, index, vendeur);
            String ontpower = getRxOpticalPower(ip, ontId, index, vendeur);

            if (
                ((ontpower.equals("32768") && currentAlarmList.contains("1")) ||
                    currentAlarmList.contains("15") ||
                    currentAlarmList.contains("16") ||
                    currentAlarmList.contains("17")) ||
                ((ontpower.equals("2147483647") && currentAlarmList.equals("1")) ||
                    currentAlarmList.equals("2") ||
                    currentAlarmList.equals("3") ||
                    currentAlarmList.equals("4"))
            ) {
                System.out.println(
                    "{'status': 'ko', 'anomalie': 'Coupure fibre', 'etat': 'Critique', 'description': 'Coupure de fibre optique', 'Recommandation': ['Remonter l'anomalie à l'équipe intervention terrain', 'Faire une mesure de réflectometrie pour localisation du point de coupure.']}"
                );
            } else {
                System.out.println("{'status': 'ok', 'description': 'coupure fibre Non'}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getCurrentAlarms(String ip, String index, String ontId, String vendeur) throws IOException {
        String oid_ont = "";
        CommunityTarget target = new CommunityTarget();
        if (vendeur.equals("Nokia")) {
            oid_ont = "1.3.6.1.4.1.637.61.1.35.10.4.1.2." + index;
            target.setCommunity(new OctetString("t1HAI2nai"));
            target.setAddress(new UdpAddress(ip + "/161"));
            target.setRetries(20);
            target.setTimeout(2000);
            target.setVersion(SnmpConstants.version2c);
        } else if (vendeur.equals("Huawei")) {
            oid_ont = "1.3.6.1.4.1.2011.6.128.1.1.2.46.1.24." + index + "." + ontId;
            target.setCommunity(new OctetString("OLT@osn_read"));
            target.setAddress(new UdpAddress(ip + "/161"));
            target.setRetries(20);
            target.setTimeout(2000);
            target.setVersion(SnmpConstants.version2c);
        }
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();

        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid_ont)));
        pdu.setType(PDU.GET);
        ResponseEvent event = snmp.send(pdu, target);

        if (event != null && event.getResponse() != null) {
            return event.getResponse().get(0).getVariable().toString();
        }

        return "";
    }

    private static String getRxOpticalPower(String ip, String index, String ontId, String vendeur) throws IOException {
        String oid_ont = "";
        CommunityTarget target = new CommunityTarget();
        if (vendeur.equals("Nokia")) {
            oid_ont = "1.3.6.1.4.1.637.61.1.35.10.14.1.2." + index;
            target.setCommunity(new OctetString("t1HAI2nai"));
            target.setAddress(new UdpAddress(ip + "/161"));
            target.setRetries(20);
            target.setTimeout(2000);
            target.setVersion(SnmpConstants.version2c);
        } else if (vendeur.equals("Huawei")) {
            oid_ont = "1.3.6.1.4.1.2011.6.128.1.1.2.51.1.4." + index + "." + ontId;
            target.setCommunity(new OctetString("OLT@osn_read"));
            target.setAddress(new UdpAddress(ip + "/161"));
            target.setRetries(20);
            target.setTimeout(2000);
            target.setVersion(SnmpConstants.version2c);
        }
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();

        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid_ont)));
        pdu.setType(PDU.GET);
        ResponseEvent event = snmp.send(pdu, target);

        if (event != null && event.getResponse() != null) {
            return event.getResponse().get(0).getVariable().toString();
        }

        return "";
    }
}
