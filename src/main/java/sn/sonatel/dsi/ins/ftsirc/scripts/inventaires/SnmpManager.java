//package sn.sonatel.dsi.ins.ftsirc.scripts.inventaires;
//
//import org.snmp4j.CommunityTarget;
//import org.snmp4j.PDU;
//import org.snmp4j.Snmp;
//import org.snmp4j.TransportMapping;
//import org.snmp4j.event.ResponseEvent;
//import org.snmp4j.mp.SnmpConstants;
//import org.snmp4j.smi.*;
//import org.snmp4j.transport.DefaultUdpTransportMapping;
//import org.springframework.beans.factory.annotation.Autowired;
//import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
//import sn.sonatel.dsi.ins.ftsirc.service.OLTService;
//import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;
//import sn.sonatel.dsi.ins.ftsirc.service.impl.OLTServiceImpl;
//import sn.sonatel.dsi.ins.ftsirc.service.mapper.OLTMapper;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class SnmpManager {
//     private final OLTService oltService;
//    private final OLTMapper oltMapper ;
//
//    SnmpManager(OLTService oltService, OLTMapper oltMapper){
//        this.oltService=oltService;
//        this.oltMapper = oltMapper;
//    }
//
//    public static void main(String[] args) {
//        Long id = Long.parseLong("2080");
//        Optional<OLTDTO> oltdto = this.oltService.findOne(id);
//
//        getAllDataOnOLT(this.oltMapper.toEntity(oltdto.get()));
//    }
//
//    public static void getAllDataOnOLT(OLT olt) {
//        List<String> _index_ = new ArrayList<>();
//        List<Integer> _ont_ = new ArrayList<>();
//        List<Integer> _slot_ = new ArrayList<>();
//        List<Integer> _pon_ = new ArrayList<>();
//        List<String> serviceId = new ArrayList<>();
//        List<Integer> listPonIndex = new ArrayList<>();
//        System.out.println("debut de l'inventaire");
//
//        // startTime = time.time();
//
//        String[] oids = {"1.3.6.1.4.1.2011.6.128.1.1.2.43.1.9", "1.3.6.1.4.1.637.61.1.6.5.1.1"};
//        System.out.println("inventaire de l'OLT " + olt.getLibelle());
//        if (olt.getVendeur().equals("Nokia")) {
//            TransportMapping<?> transport = null;
//            try {
//                transport = new DefaultUdpTransportMapping();
//                transport.listen();
//
//                CommunityTarget target = new CommunityTarget();
//                target.setCommunity(new OctetString("t1HAI2nai"));
//                target.setAddress(new UdpAddress(olt.getIp() + "/161"));
//                target.setRetries(2);
//                target.setTimeout(1500);
//                target.setVersion(SnmpConstants.version2c);
//
//                Snmp snmp = new Snmp(transport);
//                PDU pdu = new PDU();
//                pdu.setType(PDU.GETNEXT);
//                OID oid = new OID(oids[1]);
//                pdu.add(new VariableBinding(oid));
//
//                ResponseEvent responseEvent;
//                do{
//                responseEvent   = snmp.send(pdu, target);
//                   PDU response = responseEvent.getResponse();
//
//                if (response != null) {
//                    pdu.setRequestID(new Integer32(0));
//                    pdu.clear();
//                    for (VariableBinding varBind : response.getVariableBindings()) {
//                        pdu.add(new VariableBinding(varBind.getOid()));
//
//                        String binary_ = Integer.toBinaryString(Integer.parseInt(String.valueOf(varBind.getOid().last())));
//                        int len_level = binary_.length() - 21;
//                        int level = Integer.parseInt(binary_.substring(0, len_level).substring(len_level - 4), 2);
//                        int len_pon = binary_.length() - 16;
//                        int pon = Integer.parseInt(binary_.substring(0, len_pon).substring(len_pon - 5), 2) + 1;
//                        int len_slot = binary_.length() - 25;
//                        int slot = Integer.parseInt(binary_.substring(0,  len_slot), 2) - 1;
//                        int len_ont = binary_.length() - 9;
//                        int ont = Integer.parseInt(binary_.substring(0, len_ont).substring(len_ont - 7), 2) + 1;
//                        String index = String.valueOf(varBind.getOid().last());
//                        _index_.add(index);  // get port index of the ont
//                        int ponIndex = (int) ((slot + 1) *Math.pow(2,25) + 13 * Math.pow(2,21) + (pon - 1) * Math.pow(2,16));
//                        String numberPhone = varBind.getVariable().toString().replace(" ", "");
//                        System.out.println(varBind.getOid().toString());
//                        System.out.println("ponIndex: " + ponIndex + ", ontId: " + ont + ", index: " + index + ", numberPhone: " + numberPhone + ", slot: " + slot + ", pon: " + pon);
//                        _slot_.add(slot);  // get slot
//                        _ont_.add(ont);  // get ont_id
//                        _pon_.add(pon);  // get pon
//                        serviceId.add(numberPhone);
//                    }
//                } else {
//                    System.err.println("No response received.");
//                }}while (responseEvent !=null && responseEvent.getResponse() != null);
//
//                snmp.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (transport != null) {
//                        transport.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            System.err.println("No response received.2");
//        }
//    }
//}
