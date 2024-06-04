package sn.sonatel.dsi.ins.ftsirc.scripts;

import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class ScriptsDiagnostic {

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

    public String getCurrentAlarms(String ip, String index, String ontId, String vendeur) throws IOException {
        String ontCurrAlarms = "";
        String ontLastDownCause = "";
        String alarm = "";
        List<String> alarmList = new ArrayList<>();

        if (vendeur.equalsIgnoreCase("NOKIA")) {
            ontCurrAlarms = "1.3.6.1.4.1.637.61.1.35.10.4.1.2" + "." + index; //No Defects(0),Loss of Signal (1),Loss of Acknowlwdgement (2),Undefined - Not Supported(3),Loss of GEM Channel Delineation (4),Undefined - Not Supported(5),Physical Equipment Error (6),Startup Failure (7),Undefined - Not Supported(8),Signal Degrade (9),Undefined - Not Supported(10),Undefined - Not Supported(11),ONT Disabled (12),Message Error Message (13),Undefined - Not Supported(14),Inactive (15),Loss of Frame (16),Signal fail (17),Dying Gasp (18),Deactivate Failure (19),Loss of PLOAM (20),Drift of Window (21),Remote Defect Indication (22),Undefined - Not Supported(23),Loss of Key Synchronzation (24),Undefined - Not Supported(25)

            ResponseEvent event = this.connectToOID(ip, ontCurrAlarms, "NOKIA");
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    String temp_ = Integer.toBinaryString(Integer.parseInt(varBind.getVariable().toString()));
                    for (int i = 0; i < temp_.length(); i++) {
                        if (temp_.charAt(i) == '1') {
                            alarmList.add(String.valueOf(i + 1));
                        }
                    }
                    for (String i : alarmList) {
                        if (i.equals("1") || i.equals("15") || i.equals("16") || i.equals("17") || i.equals("18")) {
                            alarm = "KO";
                        } else {
                            alarm = "OK";
                        }
                    }
                }
            }
        } else if (vendeur.equalsIgnoreCase("HUAWEI")) {
            ontLastDownCause = "1.3.6.1.4.1.2011.6.128.1.1.2.46.1.24" + "." + index + "." + ontId; //Loss of signal(1),Loss of signal for ONUi or Loss of burst for ONUi(2),Loss of frame of ONUi(3),Signal fail of ONUi(4),Loss of acknowledge with ONUi(5),Loss of PLOAM for ONUi(6),Deactive ONT fails(7),Deactive ONT success(8),Reset ONT(9),Re-register ONT(10),Pop up fail(11),Dying-gasp(13),Loss of key synch with ONUi(15),Deactived ONT due to the ring(18),Shut down ONT optical module(30),Reset ONT by ONT command(31),Reset ONT by ONT reset button(32),Reset ONT by ONT software(33),Deactived ONT due to broadcast attack(34),Unknown(-1)
            ResponseEvent event = this.connectToOID(ip, ontLastDownCause, "HUAWEI");
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    String result = varBind.getVariable().toString();
                    if (result.equals("1") || result.equals("2") || result.equals("3") || result.equals("4") || result.equals("13")) {
                        alarm = "KO";
                    } else {
                        alarm = "OK";
                    }
                }
            }
        }
        return alarm;
    }

    public String getRxOpticalPower(String ip, String index, String ontId, String vendeur) throws IOException {
        String ontRxPower = "";
        String varOpticalPower = "";
        String opticalPower = "";

        if (vendeur.equalsIgnoreCase("NOKIA")) {
            ontRxPower = "1.3.6.1.4.1.637.61.1.35.10.14.1.2" + "." + index;
            ResponseEvent event = this.connectToOID(ip, ontRxPower, "NOKIA");
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    varOpticalPower = varBind.getVariable().toString();
                    if (varOpticalPower.equals("32768")) {
                        opticalPower = "KO";
                    } else {
                        opticalPower = "OK";
                    }
                }
            }
        } else if (vendeur.equalsIgnoreCase("HUAWEI")) {
            ontRxPower = "1.3.6.1.4.1.2011.6.128.1.1.2.51.1.4" + "." + index + "." + ontId;
            ResponseEvent event = this.connectToOID(ip, ontRxPower, "HUAWEI");

            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    varOpticalPower = varBind.getVariable().toString();
                    if (varOpticalPower.equals("2147483647")) {
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
        String ontOperStatus = "";
        String varOperStatus = "";
        String operStatus = "";

        if (vendeur.equalsIgnoreCase("NOKIA")) {
            ontOperStatus = "1.3.6.1.2.1.2.2.1.8" + "." + index; //Up(1),Down(2),Testing(3),Unknown(4),Dormant(5), notPresent(6),lowerLayerDown(7)
            ResponseEvent event = this.connectToOID(ip, ontOperStatus, "NOKIA");
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    varOperStatus = varBind.getVariable().toString();
                    if (varOperStatus.equals("1")) {
                        operStatus = "OK";
                    } else {
                        operStatus = "KO";
                    }
                }
            }
        } else if (vendeur.equalsIgnoreCase("HUAWEI")) {
            ontOperStatus = "1.3.6.1.4.1.2011.6.128.1.1.2.46.1.15" + "." + index + "." + ontId; //Up(1),Down(2),Unknown(-1)

            ResponseEvent event = this.connectToOID(ip, ontOperStatus, "HUAWEI");
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    varOperStatus = varBind.getVariable().toString();
                    if (varOperStatus.equals("2")) {
                        operStatus = "KO";
                    } else {
                        operStatus = "OK";
                    }
                }
            }
        }
        return operStatus;
    }

    public String getRowStatus(String ip, String index, String ontId, String vendeur) throws IOException {
        String ontRowStatus = "";
        String varRowStatus = "";
        String rowStatus = "";

        if (vendeur.equalsIgnoreCase("NOKIA")) {
            ontRowStatus = "1.3.6.1.4.1.637.61.1.35.10.1.1.2" + "." + index; //Active(1),NotInService(2),NotReady(3),CreateAndGo(4),CreateAndWait(5),Destroy(6)

            ResponseEvent event = this.connectToOID(ip, ontRowStatus, "NOKIA");
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    varRowStatus = varBind.getVariable().toString();
                    if (varRowStatus.equals("1")) {
                        rowStatus = "OK";
                    } else {
                        rowStatus = "KO";
                    }
                }
            }
        } else if (vendeur.equalsIgnoreCase("HUAWEI")) {
            ontRowStatus = "1.3.6.1.4.1.2011.6.128.1.1.2.43.1.10" + "." + index + "." + ontId; //Active(1), NotInService(2), NotReady(3), CreateAndGo(4), CreateAndWait(5), Destroy(6)

            ResponseEvent event = this.connectToOID(ip, ontRowStatus, "HUAWEI");
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    varRowStatus = varBind.getVariable().toString();
                    if (varRowStatus.equals("1")) {
                        rowStatus = "OK";
                    } else {
                        rowStatus = "KO";
                    }
                }
            }
        }
        return rowStatus;
    }

    public String getRanging(String ip, String index, String ontId, String vendeur) throws IOException {
        String ontRanging = "";
        String varRanging = "";
        String ranging = "";

        if (vendeur.equalsIgnoreCase("NOKIA")) {
            ontRanging = "1.3.6.1.4.1.637.61.1.35.11.4.1.5" + "." + index; //Not Ranged and Not DISABLED(0), Ranged(1), Manually-DISABLED by the operator(2), Auto-DISABLED by the OLT (3)

            ResponseEvent event = this.connectToOID(ip, ontRanging, "NOKIA");
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    varRanging = varBind.getVariable().toString();
                    if (varRanging.equals("1")) {
                        ranging = "OK";
                    } else {
                        ranging = "KO";
                    }
                }
            }
        } else if (vendeur.equalsIgnoreCase("HUAWEI")) {
            ontRanging = "1.3.6.1.4.1.2011.6.128.1.1.2.46.1.20" + "." + index + "." + ontId; //Not Ranged(-1), Ranged(>0)

            ResponseEvent event = this.connectToOID(ip, ontRanging, "HUAWEI");
            if (event != null && event.getResponse() != null) {
                for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                    varRanging = varBind.getVariable().toString();
                    if (varRanging.equals("-1")) {
                        ranging = "KO";
                    } else {
                        ranging = "OK";
                    }
                }
            }
        }
        return ranging;
    }

    public Long getOLTRxPower(String vendeur, String index, String ip, String _ont_) throws IOException {
        String OltRxPower = "";
        Long oltRxPowerValue = null;

        OltRxPower = vendeur.equalsIgnoreCase("NOKIA")
            ? "1.3.6.1.4.1.637.61.1.35.10.18.1.2" + "." + index
            : "1.3.6.1.4.1.2011.6.128.1.1.2.51.1.6" + "." + index + "." + _ont_;

        ResponseEvent event = this.connectToOID(ip, OltRxPower, vendeur.toUpperCase());
        if (event != null && event.getResponse() != null) {
            for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                oltRxPowerValue = Long.valueOf(varBind.getVariable().toString());
            }
        }
        return oltRxPowerValue;
    }

    public Long getONTRxPower(String vendeur, String index, String ip, String _ont_) throws IOException {
        String OntRxPower = "";
        Long ontRxPowerValue = null;

        OntRxPower = vendeur.equalsIgnoreCase("NOKIA")
            ? "1.3.6.1.4.1.637.61.1.35.10.14.1.2" + "." + index // commentaires
            : "1.3.6.1.4.1.2011.6.128.1.1.2.51.1.4" + "." + index + "." + _ont_; // commentaires

        ResponseEvent event = this.connectToOID(ip, OntRxPower, vendeur.toUpperCase());

        if (event != null && event.getResponse() != null) {
            for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                ontRxPowerValue = Long.valueOf(varBind.getVariable().toString());
            }
        }
        return ontRxPowerValue;
    }

    public Long getSfpClass(String vendeur, String index, String ip, String _ont_, Integer slot, String pon) throws IOException {
        String SfpClass = "";
        Long SfpClassValue = null;
        Integer slot_index = 4353 + slot + 1;
        SfpClass = vendeur.equalsIgnoreCase("NOKIA")
            ? "1.3.6.1.4.1.637.61.1.56.6.1.13" + "." + slot_index + "." + pon
            : "1.3.6.1.4.1.2011.6.128.1.1.2.22.1.28" + "." + index;

        ResponseEvent event = this.connectToOID(ip, SfpClass, vendeur.toUpperCase());
        if (event != null && event.getResponse() != null) {
            for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                SfpClassValue = Long.valueOf(varBind.getVariable().toString());
            }
        }
        return SfpClassValue;
    }

    public String getLastDownTime(String ip, String index, String ontId, String vendeur) throws IOException {
        String OntLastDownTime = "";
        String OntLastDownTimeValue = null;
        OntLastDownTime = vendeur.equalsIgnoreCase("NOKIA")
            ? "1.3.6.1.2.1.2.2.1.9" + "." + index
            : "1.3.6.1.4.1.2011.6.128.1.1.2.46.1.23" + "." + index + "." + ontId;

        ResponseEvent event = this.connectToOID(ip, OntLastDownTime, vendeur.toUpperCase());
        if (event != null && event.getResponse() != null) {
            for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                OntLastDownTimeValue = varBind.getVariable().toString();
                System.out.println("Time Hexa:" + OntLastDownTimeValue);
                OntLastDownTimeValue = hexToHumanReadable(OntLastDownTimeValue);
                System.out.println("Last Down Time:" + OntLastDownTimeValue);
            }
        }
        return OntLastDownTimeValue;
    }

    public String getLastUpTime(String ip, String index, String ontId, String vendeur) throws IOException {
        String OntLastUpTime = "";
        String OntLastUpTimeValue = null;
        OntLastUpTime = vendeur.equalsIgnoreCase("NOKIA")
            ? "1.3.6.1.2.1.2.2.1.9" + "." + index
            : "1.3.6.1.4.1.2011.6.128.1.1.2.46.1.22" + "." + index + "." + ontId;

        ResponseEvent event = this.connectToOID(ip, OntLastUpTime, vendeur.toUpperCase());
        if (event != null && event.getResponse() != null) {
            for (VariableBinding varBind : event.getResponse().getVariableBindings()) {
                OntLastUpTimeValue = varBind.getVariable().toString();
                System.out.println("Time Hexa:" + OntLastUpTimeValue);
                OntLastUpTimeValue = hexToHumanReadable(OntLastUpTimeValue);
                System.out.println("Last Up Time:" + OntLastUpTimeValue);
            }
        }
        return OntLastUpTimeValue;
    }

    public static String hexToHumanReadable(String hexTime) {
        // Split the hexadecimal string by colons
        String[] hexParts = hexTime.split(":");

        // Convert each part from hex to decimal
        int year = Integer.parseInt(hexParts[0] + hexParts[1], 16);
        int month = Integer.parseInt(hexParts[2], 16);
        int day = Integer.parseInt(hexParts[3], 16);
        int hour = Integer.parseInt(hexParts[4], 16);
        int minute = Integer.parseInt(hexParts[5], 16);
        int second = Integer.parseInt(hexParts[6], 16);

        // Construct a LocalDateTime object
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second);

        // Format the datetime to a readable string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
