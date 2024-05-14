package sn.sonatel.dsi.ins.ftsirc.scripts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class ScriptsDiagnostic {

    public String getCurrentAlarms(String ip, String index, String ontId, String vendeur) throws IOException {
        String OntCurrAlarms = "";
        String OntLastDownCause = "";
        String checkFiberCut = "";
        List<String> alarmList = new ArrayList<>();

        CommunityTarget target = new CommunityTarget();
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();
        if (vendeur.equalsIgnoreCase("NOKIA")) {
            OntCurrAlarms = "1.3.6.1.4.1.637.61.1.35.10.4.1.2" + "." + index; //No Defects(0),Loss of Signal (1),Loss of Acknowlwdgement (2),Undefined - Not Supported(3),Loss of GEM Channel Delineation (4),Undefined - Not Supported(5),Physical Equipment Error (6),Startup Failure (7),Undefined - Not Supported(8),Signal Degrade (9),Undefined - Not Supported(10),Undefined - Not Supported(11),ONT Disabled (12),Message Error Message (13),Undefined - Not Supported(14),Inactive (15),Loss of Frame (16),Signal fail (17),Dying Gasp (18),Deactivate Failure (19),Loss of PLOAM (20),Drift of Window (21),Remote Defect Indication (22),Undefined - Not Supported(23),Loss of Key Synchronzation (24),Undefined - Not Supported(25)
            target.setCommunity(new OctetString("t1HAI2nai"));
            target.setAddress(new UdpAddress(ip + "/" + "161"));
            target.setRetries(20);
            target.setTimeout(2000);
            target.setVersion(SnmpConstants.version2c);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(OntCurrAlarms)));
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    String temp_ = Integer.toBinaryString(Integer.parseInt(varBind.getVariable().toString()));
                    for (int i = 0; i < temp_.length(); i++) {
                        if (temp_.charAt(i) == '1') {
                            alarmList.add(String.valueOf(i + 1));
                        }
                    }
                    for (String i : alarmList) {
                        if (i.equals("1") || i.equals("15") || i.equals("16") || i.equals("17")) {
                            checkFiberCut = "KO";
                        } else {
                            checkFiberCut = "OK";
                        }
                    }
                }
            }
        } else if (vendeur.equalsIgnoreCase("HUAWEI")) {
            OntLastDownCause = "1.3.6.1.4.1.2011.6.128.1.1.2.46.1.24" + "." + index + "." + ontId; //Loss of signal(1),Loss of signal for ONUi or Loss of burst for ONUi(2),Loss of frame of ONUi(3),Signal fail of ONUi(4),Loss of acknowledge with ONUi(5),Loss of PLOAM for ONUi(6),Deactive ONT fails(7),Deactive ONT success(8),Reset ONT(9),Re-register ONT(10),Pop up fail(11),Dying-gasp(13),Loss of key synch with ONUi(15),Deactived ONT due to the ring(18),Shut down ONT optical module(30),Reset ONT by ONT command(31),Reset ONT by ONT reset button(32),Reset ONT by ONT software(33),Deactived ONT due to broadcast attack(34),Unknown(-1)
            target.setCommunity(new OctetString("OLT@osn_read"));
            target.setAddress(new UdpAddress(ip + "/" + "161"));
            target.setRetries(20);
            target.setTimeout(2000);
            target.setVersion(SnmpConstants.version2c);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(OntLastDownCause)));
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    String result = varBind.getVariable().toString();
                    if (result.equals("1") || result.equals("2") || result.equals("3") || result.equals("4")) {
                        checkFiberCut = "KO";
                    } else {
                        checkFiberCut = "OK";
                    }
                }
            }
        }
        return checkFiberCut;
    }

    public static String getRxOpticalPower(String ip, String index, String ontId, String vendeur) throws IOException {
        String OntRxPower = "";
        String ontpower = "";
        String opticalPower = "";

        CommunityTarget target = new CommunityTarget();
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();
        if (vendeur.equalsIgnoreCase("NOKIA")) {
            OntRxPower = "1.3.6.1.4.1.637.61.1.35.10.14.1.2" + "." + index;
            target.setCommunity(new OctetString("t1HAI2nai"));
            target.setAddress(new UdpAddress(ip + "/" + "161"));
            target.setRetries(20);
            target.setTimeout(2000);
            target.setVersion(SnmpConstants.version2c);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(OntRxPower)));
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    ontpower = varBind.getVariable().toString();
                    if (ontpower.equals("32768")) {
                        opticalPower = "KO";
                    } else {
                        opticalPower = "OK";
                    }
                }
            }
        } else if (vendeur.equalsIgnoreCase("HUAWEI")) {
            OntRxPower = "1.3.6.1.4.1.2011.6.128.1.1.2.51.1.4" + "." + index + "." + ontId;
            target.setCommunity(new OctetString("OLT@osn_read"));
            target.setAddress(new UdpAddress(ip + "/" + "161"));
            target.setRetries(20);
            target.setTimeout(2000);
            target.setVersion(SnmpConstants.version2c);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(OntRxPower)));
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    ontpower = varBind.getVariable().toString();
                    if (ontpower.equals("2147483647")) {
                        opticalPower = "KO";
                    } else {
                        opticalPower = "OK";
                    }
                }
            }
        }
        return opticalPower;
    }

    public String getOperStatus(String ip, String index, String ontId, String vendeur) throws IOException {
        String OntOperStatus = "";
        String OperStatus = "";

        CommunityTarget target = new CommunityTarget();
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();
        if (vendeur.equalsIgnoreCase("NOKIA")) {
            OntOperStatus = "1.3.6.1.2.1.2.2.1.8" + "." + index; //Up(1),Down(2),Testing(3),Unknown(4),Dormant(5), notPresent(6),lowerLayerDown(7)
            target.setCommunity(new OctetString("t1HAI2nai"));
            target.setAddress(new UdpAddress(ip + "/" + "161"));
            target.setRetries(20);
            target.setTimeout(2000);
            target.setVersion(SnmpConstants.version2c);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(OntOperStatus)));
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    OperStatus = varBind.getVariable().toString();
                    if (OperStatus != "1") {
                        OperStatus = "KO";
                    } else {
                        OperStatus = "OK";
                    }
                }
            }
        } else if (vendeur.equalsIgnoreCase("HUAWEI")) {
            OntOperStatus = "1.3.6.1.4.1.2011.6.128.1.1.2.46.1.15" + "." + index + "." + ontId; //Up(1),Down(2),Unknown(-1)
            target.setCommunity(new OctetString("OLT@osn_read"));
            target.setAddress(new UdpAddress(ip + "/" + "161"));
            target.setRetries(20);
            target.setTimeout(2000);
            target.setVersion(SnmpConstants.version2c);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(OntOperStatus)));
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    OperStatus = varBind.getVariable().toString();
                    if (OperStatus.equals("2")) {
                        OperStatus = "KO";
                    } else {
                        OperStatus = "OK";
                    }
                }
            }
        }
        return OperStatus;
    }

    public String getRowStatus(String ip, String index, String ontId, String vendeur) throws IOException {
        String OntOperStatus = "";
        String OperStatus = "";

        CommunityTarget target = new CommunityTarget();
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        transport.listen();
        if (vendeur.equalsIgnoreCase("NOKIA")) {
            OntOperStatus = "1.3.6.1.2.1.2.2.1.8" + "." + index; //Up(1),Down(2),Testing(3),Unknown(4),Dormant(5), notPresent(6),lowerLayerDown(7)
            target.setCommunity(new OctetString("t1HAI2nai"));
            target.setAddress(new UdpAddress(ip + "/" + "161"));
            target.setRetries(20);
            target.setTimeout(2000);
            target.setVersion(SnmpConstants.version2c);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(OntOperStatus)));
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    OperStatus = varBind.getVariable().toString();
                    if (OperStatus != "1") {
                        OperStatus = "KO";
                    } else {
                        OperStatus = "OK";
                    }
                }
            }
        } else if (vendeur.equalsIgnoreCase("HUAWEI")) {
            OntOperStatus = "1.3.6.1.4.1.2011.6.128.1.1.2.46.1.15" + "." + index + "." + ontId; //Up(1),Down(2),Unknown(-1)
            target.setCommunity(new OctetString("OLT@osn_read"));
            target.setAddress(new UdpAddress(ip + "/" + "161"));
            target.setRetries(20);
            target.setTimeout(2000);
            target.setVersion(SnmpConstants.version2c);
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(OntOperStatus)));
            pdu.setType(PDU.GET);

            ResponseEvent event = snmp.send(pdu, target);
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    OperStatus = varBind.getVariable().toString();
                    if (OperStatus.equals("2")) {
                        OperStatus = "KO";
                    } else {
                        OperStatus = "OK";
                    }
                }
            }
        }
        return OperStatus;
    }
}
