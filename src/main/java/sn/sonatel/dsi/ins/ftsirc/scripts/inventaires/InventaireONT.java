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
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
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
        Long id = Long.parseLong("2330");
        List<ONTDTO> listONTs;
        Optional<OLTDTO> oltdto = oltService.findOne(id);

        if (oltService.findOne(id).isPresent()) {
            listONTs = getAllONTOnOLT(oltdto.get());

            //         ONTDTO ontdto = ontMapper.toEntity(ont1);
            ontService.saveListONT(listONTs);
        }
    }

    public static List<ONTDTO> getAllONTOnOLT(OLTDTO olt) {
        List<ONTDTO> listONTs = new ArrayList<>();

        System.out.println("debut de l'inventaire");

        // startTime = time.time();

        String[] oids = { "1.3.6.1.4.1.2011.6.128.1.1.2.43.1.9", "1.3.6.1.4.1.637.61.1.6.5.1.1" };
        System.out.println("inventaire de l'OLT " + olt.getLibelle() + olt.getVendeur());
        if (olt.getVendeur().equals("NOKIA")) {
            TransportMapping<?> transport = null;
            try {
                transport = new DefaultUdpTransportMapping();
                transport.listen();

                CommunityTarget target = new CommunityTarget();
                target.setCommunity(new OctetString("t1HAI2nai"));
                target.setAddress(new UdpAddress(olt.getIp() + "/161"));
                target.setRetries(2);
                target.setTimeout(1500);
                target.setVersion(SnmpConstants.version2c);

                Snmp snmp = new Snmp(transport);
                PDU pdu = new PDU();
                pdu.setType(PDU.GETNEXT);
                OID oid = new OID(oids[1]);
                pdu.add(new VariableBinding(oid));

                ResponseEvent responseEvent;
                do {
                    responseEvent = snmp.send(pdu, target);
                    PDU response = responseEvent.getResponse();

                    if (response != null && !response.getVariableBindings().isEmpty()) {
                        pdu.setRequestID(new Integer32(0));
                        pdu.clear();
                        for (VariableBinding varBind : response.getVariableBindings()) {
                            pdu.add(new VariableBinding(varBind.getOid()));

                            String binary_ = Integer.toBinaryString(Integer.parseInt(String.valueOf(varBind.getOid().last())));

                            ONTDTO ontdto = new ONTDTO();
                            if (binary_.length() > 25) {
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
                                String index = String.valueOf(varBind.getOid().last());
                                int ponIndex = (int) ((slot + 1) * Math.pow(2, 25) + 13 * Math.pow(2, 21) + (pon - 1) * Math.pow(2, 16));
                                String numberPhone = varBind.getVariable().toString().replace(" ", "");
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
                            }
                        }
                    } else {
                        System.out.println("La longueur binaire est inferieure a 25");
                        System.err.println("No response received.");
                    }
                } while (responseEvent != null && responseEvent.getResponse() != null);

                snmp.close();
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
        } else {
            System.err.println("No response received" + olt.getLibelle() + olt.getVendeur());
        }
        return listONTs;
    }
}
