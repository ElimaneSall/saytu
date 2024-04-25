package sn.sonatel.dsi.ins.ftsirc.scripts.inventaires;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
import sn.sonatel.dsi.ins.ftsirc.service.OLTService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.OLTMapperImpl;

@Component
public class InventaireONT implements CommandLineRunner {

    @Autowired
    OLTService oltService;

    @Autowired
    OLTMapperImpl oltMapper;

    @Override
    public void run(String... args) throws Exception {
        Long id = Long.parseLong("2080");
        Optional<OLTDTO> olt1 = oltService.findOne(id);

        olt1.ifPresent(oltdto -> {
            try {
                getAllONTOnOLT(oltMapper.toEntity(oltdto));
            } catch (IOException e) {
                System.err.println("Erreur en effetuant la MAJ");
            }
        });
    }

    public static List<String> getAllONTOnOLT(OLT olt) throws IOException {
        List<String> serviceIdList = new ArrayList<>();
        List<String> indexList = new ArrayList<>();
        List<Integer> ontList = new ArrayList<>();
        List<Integer> slotList = new ArrayList<>();
        List<Integer> ponList = new ArrayList<>();
        List<Integer> listPonIndex = new ArrayList<>();

        System.out.println("Debut de l'inventaire");

        String[] oids = { "1.3.6.1.4.1.2011.6.128.1.1.2.43.1.9", "1.3.6.1.4.1.637.61.1.6.5.1.1" };

        TransportMapping<?> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();

        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(olt.getVendeur().equals("Nokia") ? "t1HAI2nai" : "OLT@osn_read"));
        target.setAddress(GenericAddress.parse("udp:" + olt.getIp() + "/161"));
        target.setRetries(150);
        target.setTimeout(2000);
        target.setVersion(SnmpConstants.version2c);

        for (String oid : oids) {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(oid)));
            pdu.setType(PDU.GETBULK);

            ResponseEvent responseEvent = snmp.send(pdu, target);

            if (responseEvent != null && responseEvent.getResponse() != null) {
                System.out.println("responseEvent != null && responseEvent.getResponse() != null");
                for (VariableBinding vb : responseEvent.getResponse().toArray()) {
                    String varBind = vb.getOid().toString();
                    int level = Integer.parseInt(varBind.substring(varBind.lastIndexOf(".") - 4, varBind.lastIndexOf(".")), 2);
                    int pon = Integer.parseInt(varBind.substring(varBind.lastIndexOf(".") - 5, varBind.lastIndexOf(".")), 2) + 1;
                    int slot = Integer.parseInt(varBind.substring(varBind.lastIndexOf(".") - 25, varBind.lastIndexOf(".")), 2) - 1;
                    int ont = Integer.parseInt(varBind.substring(varBind.lastIndexOf(".") - 7, varBind.lastIndexOf(".")), 2) + 1;
                    String index = varBind.substring(varBind.lastIndexOf(".") + 1);

                    indexList.add(index);
                    slotList.add(slot);
                    ontList.add(ont);
                    ponList.add(pon);
                    serviceIdList.add(vb.getVariable().toString().replaceAll(" ", ""));

                    int ponIndex = (slot + 1) * (int) Math.pow(2, 25) + 13 * (int) Math.pow(2, 21) + (pon - 1) * (int) Math.pow(2, 16);
                    listPonIndex.add(ponIndex);

                    System.out.println(varBind);
                    System.out.println(
                        "ponIndex: " +
                        ponIndex +
                        ", ontId: " +
                        ont +
                        ", index: " +
                        index +
                        ", numberPhone: " +
                        vb.getVariable().toString().replaceAll(" ", "") +
                        ", slot: " +
                        slot +
                        ", pon: " +
                        pon
                    );
                }
            }
        }

        System.out.println("Fin de l'inventaire");

        snmp.close();
        return serviceIdList;
    }
}
