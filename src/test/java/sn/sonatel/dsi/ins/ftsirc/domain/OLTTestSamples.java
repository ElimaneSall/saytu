package sn.sonatel.dsi.ins.ftsirc.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OLTTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static OLT getOLTSample1() {
        return new OLT()
            .id(1L)
            .libelle("libelle1")
            .ip("ip1")
            .vendeur("vendeur1")
            .typeEquipment("typeEquipment1")
            .codeEquipment("codeEquipment1")
            .adresse("adresse1")
            .emplacement("emplacement1")
            .typeCarte("typeCarte1")
            .latitude("latitude1")
            .longitude("longitude1")
            .capacite("capacite1")
            .etat("etat1");
    }

    public static OLT getOLTSample2() {
        return new OLT()
            .id(2L)
            .libelle("libelle2")
            .ip("ip2")
            .vendeur("vendeur2")
            .typeEquipment("typeEquipment2")
            .codeEquipment("codeEquipment2")
            .adresse("adresse2")
            .emplacement("emplacement2")
            .typeCarte("typeCarte2")
            .latitude("latitude2")
            .longitude("longitude2")
            .capacite("capacite2")
            .etat("etat2");
    }

    public static OLT getOLTRandomSampleGenerator() {
        return new OLT()
            .id(longCount.incrementAndGet())
            .libelle(UUID.randomUUID().toString())
            .ip(UUID.randomUUID().toString())
            .vendeur(UUID.randomUUID().toString())
            .typeEquipment(UUID.randomUUID().toString())
            .codeEquipment(UUID.randomUUID().toString())
            .adresse(UUID.randomUUID().toString())
            .emplacement(UUID.randomUUID().toString())
            .typeCarte(UUID.randomUUID().toString())
            .latitude(UUID.randomUUID().toString())
            .longitude(UUID.randomUUID().toString())
            .capacite(UUID.randomUUID().toString())
            .etat(UUID.randomUUID().toString());
    }
}
