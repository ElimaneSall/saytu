package sn.sonatel.dsi.ins.ftsirc.scripts;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.hibernate.Session;
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
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.repository.ONTRepository;
import sn.sonatel.dsi.ins.ftsirc.service.DiagnosticService;
import sn.sonatel.dsi.ins.ftsirc.service.OLTService;
import sn.sonatel.dsi.ins.ftsirc.service.ONTService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.dto.ONTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.impl.ONTServiceImpl;
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

    @Autowired
    ONTRepository ontRepository;

    @Autowired
    DiagnosticService diagnosticService;

    @Override
    public void run(String... args) throws Exception {
        //                                                                Long id = Long.parseLong("1659");
        //                                                                List<ONTDTO> listONTs;
        //                                                                Optional<OLTDTO> oltdto = oltService.findOne(id);
        //                                                                OLTDTO ontdto = oltdto.orElseThrow();
        //                                                                listONTs = getAllONTOnOLT(ontdto);
        //                                                                System.out.println("Fin diagnostic:");

        //                                                                ontService.saveListONT(ontMapper.toEntity(listONTs));
        //        System.out.println("Debut diagnostic:");
        //       diagnosticService.diagnosticFiberAutomatique();
        //        diagnosticService.diagnosticFiberManuel("338674808");
        diagnosticService.diagnosticLastDownTime("338674808");
        diagnosticService.diagnosticLastUpTime("338674808");
        //        338257692 >> Nokia Siege1
        //        338674808 >> Huawei Foire

    }

    public ResponseEvent connectToOID(String ip, String oid, String vendeur) throws IOException {
        TransportMapping<?> transport = null;
        try {
            transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            transport.listen();
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(vendeur.equalsIgnoreCase("NOKIA") ? "t1HAI2nai" : "OLT@osn_read"));
            target.setAddress(new UdpAddress(ip + "/161"));
            target.setRetries(2);
            target.setTimeout(1500);
            target.setVersion(SnmpConstants.version2c);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(oid)));
            pdu.setType(PDU.GET);
            ResponseEvent event = snmp.send(pdu, target);
            return event;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    public String getServiceFlowUp(String ip, String index, String ont) throws IOException {
        String idService = "";
        String oidIdService = "1.3.6.1.4.1.2011.6.128.1.1.2.62.1.4." + index + "." + ont;

        ResponseEvent event = this.connectToOID(ip, oidIdService, "HUAWEI");
        if (event != null && event.getResponse() != null) {
            for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                idService = varBind.getVariable().toString();
                System.out.println(idService);
            }
        }

        return idService;
    }
}
