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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.Anomalie;
import sn.sonatel.dsi.ins.ftsirc.domain.Diagnostic;
import sn.sonatel.dsi.ins.ftsirc.domain.ONT;
import sn.sonatel.dsi.ins.ftsirc.domain.Signal;
import sn.sonatel.dsi.ins.ftsirc.repository.DiagnosticRepository;
import sn.sonatel.dsi.ins.ftsirc.repository.ONTRepository;
import sn.sonatel.dsi.ins.ftsirc.scripts.ScriptsDiagnostic;
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

    ScriptsDiagnostic scriptsDiagnostic = new ScriptsDiagnostic();

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

    @Override
    public Page<DiagnosticDTO> findAll(Pageable pageable) {
        return null;
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

    public String diagnosticFiberCut(ONT ont) {
        try {

            if (ont != null) {
                //Verifer que les elements ne sont pas null

                String index = ont.getIndex();
                String ip = ont.getOlt().getIp();
                String ontId = ont.getOntID();
                String vendeur = ont.getOlt().getVendeur();

                String currentAlarmList = scriptsDiagnostic.getCurrentAlarms(ip, index, ontId, vendeur);
                String ontpower = scriptsDiagnostic.getRxOpticalPower(ip, index, ontId, vendeur);

                if (ontpower.equals("KO") && currentAlarmList.equals("KO")) {
                    System.out.println(
                        "{'status': 'ko', 'anomalie': 'Coupure fibre', 'etat': 'Critique', 'description': 'Coupure de fibre optique', 'Recommandation': ['Remonter l'anomalie à l'équipe intervention terrain', 'Faire une mesure de réflectometrie pour localisation du point de coupure.']}"
                    );
                } else {
                    System.out.println("{'status': 'ok', 'description': 'coupure fibre Non'}");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public String diagnosticOLTPowerUnderLimit(ONT ont) throws IOException {
        Diagnostic diagnostic = new Diagnostic();

            if (ont != null) {
                Double oltPower = scriptsDiagnostic.getOLTRxPower(ont.getOlt().getVendeur(), ont.getIndex(), ont.getOlt().getIp(), ont.getOntID());
                Double sfpclass = scriptsDiagnostic.getPowerOLT(ont.getOlt().getVendeur(), ont.getIndex(), ont.getOlt().getIp(), ont.getOntID(),Integer.parseInt( ont.getSlot()), ont.getPon());

                if(ont.getOlt().getVendeur().equalsIgnoreCase("NOKIA")){
                if(oltPower == 32768){

//                    'status': 'ko',
//                        "anomalie": "Puissance optique OLT indisponible",
//                        "etat": "Critique",
//                        "description": "Interruption du service",
//                        "Recommandation": ["Remonter l'anomalie à l'équipe intervention terrain",
//                        "Demander au technicien de faire une mesure de réflectometrie pour localisation du point de coupure."]

                    diagnostic.setOnt(ont);
//                    diagnostic.addAnomalie();

                } else if (oltPower != 32768) {
                   Double oltPower_dbm = oltPower/10;
                   if((sfpclass == 7 || sfpclass == 8) && oltPower_dbm<=-30){
//                       'status': 'ko',
//                           "anomalie": "Puissance optique OLT très faible",
//                           "signal_optique": oltpower_dbm,
//                           "etat": "Majeur",
//                           "description": "Dégradation du signal optique",
//                           "Description": "Dégradation du signal optique",
//                           "Recommandation": ["Remonter l'anomalie à l'équipe intervention terrain",
//                           "Demander au technicien de qualifier les differentes sections de la liaison pour identifier la cause de(s) la forte(s) attenuation(s) par mesures de puissance et de réflectomètrie"]

//                   diagnostic.setStatutONT();
//                       diagnostic.setSignal();
                   } else if ((sfpclass == 7 || sfpclass == 8) && (oltPower_dbm > -30 && oltPower_dbm <= -27) || oltPower_dbm > 10) {
//                       'status': 'ko',
//                           "anomalie": "Puissance optique OLT faible",
//                           "signal_optique": oltpower_dbm,
//                           "etat": "Moyen",
//                           "description": "Puissance optique admissible, pas forcément de dégradation de service",
//                           "Recommandation": ["Voir dans l'historique si existant que l'anomalie était déjà présente",
//                           "Remonter à l'équipe intervention terrain si l'anomalie est persistante"]

                   }else{
//                       'status': 'ok',
//                       'description': 'Puissance signal ONT reçu par OLT OK',
//                       'signal_optique': oltpower_dbm
                   }


                }
            } else if (ont.getOlt().getVendeur().equalsIgnoreCase("HUAWEI")) {

                    if(oltPower == 2147483647){
//                    'status': 'ko',
//                        "anomalie": "Puissance optique OLT indisponible",
//                        "etat": "Critique",
//                        "description": "Interruption du service",
//                        "Recommandation": ["Remonter l'anomalie à l'équipe intervention terrain",
//                        "Demander au technicien de faire une mesure de réflectometrie pour localisation du point de coupure."]

                        diagnostic.setOnt(ont);
//                    diagnostic.addAnomalie();

                    } else if (oltPower != 2147483647) {
                        Double oltPower_dbm = (oltPower -10000)/100;
                        if(sfpclass != 102  && oltPower_dbm<=-30){
//                       'status': 'ko',
//                           "anomalie": "Puissance optique OLT très faible",
//                           "signal_optique": oltpower_dbm,
//                           "etat": "Majeur",
//                           "description": "Dégradation du signal optique",
//                           "Description": "Dégradation du signal optique",
//                           "Recommandation": ["Remonter l'anomalie à l'équipe intervention terrain",
//                           "Demander au technicien de qualifier les differentes sections de la liaison pour identifier la cause de(s) la forte(s) attenuation(s) par mesures de puissance et de réflectomètrie"]

//                   diagnostic.setStatutONT();
//                       diagnostic.setSignal();
                        } else if ((sfpclass != 102  && (oltPower_dbm > -30 && oltPower_dbm <= -27) || oltPower_dbm > 10)
                            || (sfpclass == 102  && (oltPower_dbm < -30 || oltPower_dbm >10) )) {
//                       'status': 'ko',
//                           "anomalie": "Puissance optique OLT faible",
//                           "signal_optique": oltpower_dbm,
//                           "etat": "Moyen",
//                           "description": "Puissance optique admissible, pas forcément de dégradation de service",
//                           "Recommandation": ["Voir dans l'historique si existant que l'anomalie était déjà présente",
//                           "Remonter à l'équipe intervention terrain si l'anomalie est persistante"]

                        }else{
//                       'status': 'ok',
//                       'description': 'Puissance signal ONT reçu par OLT OK',
//                       'signal_optique': oltpower_dbm
                        }


                    }

                } }
                return "";
    }
    public String diagnosticONTPowerUnderLimit(ONT ont) throws IOException {
        Diagnostic diagnostic = new Diagnostic();

        if (ont != null) {
            Double oltPower = scriptsDiagnostic.getONTRxPower(ont.getOlt().getVendeur(), ont.getIndex(), ont.getOlt().getIp(), ont.getOntID());
            Double sfpclass = scriptsDiagnostic.getPowerOLT(ont.getOlt().getVendeur(), ont.getIndex(), ont.getOlt().getIp(), ont.getOntID(),Integer.parseInt( ont.getSlot()), ont.getPon());

            if(ont.getOlt().getVendeur().equalsIgnoreCase("NOKIA")){
                if(oltPower == 32768){

//                    'status': 'ko',
//                        "anomalie": "Puissance optique OLT indisponible",
//                        "etat": "Critique",
//                        "description": "Interruption du service",
//                        "Recommandation": ["Remonter l'anomalie à l'équipe intervention terrain",
//                        "Demander au technicien de faire une mesure de réflectometrie pour localisation du point de coupure."]

                    diagnostic.setOnt(ont);
//                    diagnostic.addAnomalie();

                } else if (oltPower != 32768) {
                    Double oltPower_dbm = oltPower*2/10;
                    if(oltPower_dbm<=-30){
//                       'status': 'ko',
//                    "anomalie": "Puissance optique ONT très faible",
//                    "signal_optique": ontpower_dbm,
//                    "etat": "Majeur",
//                    "description": "Dégradation du signal optique",
//                    "Recommandation": ["Remonter l'anomalie à l'équipe intervention terrain",
//                                       "Demander au technicien de qualifier les differentes sections de la liaison pour identifier la cause de(s) la forte(s) attenuation(s) par mesures de puissance et de réflectomètrie"]
//
//                   diagnostic.setStatutONT();
//                       diagnostic.setSignal();
                    } else if ((oltPower_dbm > -30 && oltPower_dbm <= -27) || oltPower_dbm > 10) {
//                       'status': 'ko',
//                           "anomalie": "Puissance optique ONT faible",
//                           "signal_optique": oltpower_dbm,
//                           "etat": "Moyen",
//                           "description": "Puissance optique admissible, pas forcément de dégradation de service",
//                           "Recommandation": ["Voir dans l'historique si existant que l'anomalie était déjà présente",
//                           "Remonter à l'équipe intervention terrain si l'anomalie est persistante"]

                    }else{
//                       'status': 'ok',
//                       'description': 'Puissance signal ONT OK',
//                       'signal_optique': oltpower_dbm
                    }


                }
            } else if (ont.getOlt().getVendeur().equalsIgnoreCase("HUAWEI")) {

                if(oltPower == 2147483647){
//                    'status': 'ko',
//                        "anomalie": "Puissance optique OLT indisponible",
//                        "etat": "Critique",
//                        "description": "Interruption du service",
//                        "Recommandation": ["Remonter l'anomalie à l'équipe intervention terrain",
//                        "Demander au technicien de faire une mesure de réflectometrie pour localisation du point de coupure."]

                    diagnostic.setOnt(ont);
//                    diagnostic.addAnomalie();

                } else if (oltPower != 2147483647) {
                    Double oltPower_dbm = oltPower/100;
                    if(sfpclass != 102  && oltPower_dbm<=-30){
//                       'status': 'ko',
//                           "anomalie": "Puissance optique OLT très faible",
//                           "signal_optique": oltpower_dbm,
//                           "etat": "Majeur",
//                           "description": "Dégradation du signal optique",
//                           "Description": "Dégradation du signal optique",
//                           "Recommandation": ["Remonter l'anomalie à l'équipe intervention terrain",
//                           "Demander au technicien de qualifier les differentes sections de la liaison pour identifier la cause de(s) la forte(s) attenuation(s) par mesures de puissance et de réflectomètrie"]

//                   diagnostic.setStatutONT();
//                       diagnostic.setSignal();
                    } else if ((sfpclass != 102  && (oltPower_dbm > -30 && oltPower_dbm <= -27) || oltPower_dbm > 10)
                        || (sfpclass == 102  && (oltPower_dbm < -30 || oltPower_dbm >10) )) {
//                       'status': 'ko',
//                           "anomalie": "Puissance optique ONT faible",
//                           "signal_optique": oltpower_dbm,
//                           "etat": "Moyen",
//                           "description": "Puissance optique admissible, pas forcément de dégradation de service",
//                           "Recommandation": ["Voir dans l'historique si existant que l'anomalie était déjà présente",
//                           "Remonter à l'équipe intervention terrain si l'anomalie est persistante"]

                    }else{
//                       'status': 'ok',
//                       'description': 'Puissance signal ONT reçu par OLT OK',
//                       'signal_optique': oltpower_dbm
                    }


                }

            } }
        return "";
    }
}
