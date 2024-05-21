package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.sonatel.dsi.ins.ftsirc.domain.OLT;
import sn.sonatel.dsi.ins.ftsirc.repository.OLTRepository;
import sn.sonatel.dsi.ins.ftsirc.service.OLTService;
import sn.sonatel.dsi.ins.ftsirc.service.dto.OLTDTO;
import sn.sonatel.dsi.ins.ftsirc.service.mapper.OLTMapper;

/**
 * Service Implementation for managing {@link sn.sonatel.dsi.ins.ftsirc.domain.OLT}.
 */
@Service
@Transactional
public class OLTServiceImpl implements OLTService {

    private final Logger log = LoggerFactory.getLogger(OLTServiceImpl.class);
    private final OLTRepository oLTRepository;

    private final OLTMapper oLTMapper;

    public OLTServiceImpl(OLTRepository oLTRepository, OLTMapper oLTMapper) {
        this.oLTRepository = oLTRepository;
        this.oLTMapper = oLTMapper;
    }

    @Override
    public OLTDTO save(OLTDTO oLTDTO) {
        log.debug("Request to save OLT : {}", oLTDTO);
        OLT oLT = oLTMapper.toEntity(oLTDTO);
        oLT = oLTRepository.save(oLT);
        return oLTMapper.toDto(oLT);
    }

    @Override
    public OLTDTO update(OLTDTO oLTDTO) {
        log.debug("Request to update OLT : {}", oLTDTO);
        OLT oLT = oLTMapper.toEntity(oLTDTO);
        oLT = oLTRepository.save(oLT);
        return oLTMapper.toDto(oLT);
    }

    @Override
    public Optional<OLTDTO> partialUpdate(OLTDTO oLTDTO) {
        log.debug("Request to partially update OLT : {}", oLTDTO);

        return oLTRepository
            .findById(oLTDTO.getId())
            .map(existingOLT -> {
                oLTMapper.partialUpdate(existingOLT, oLTDTO);

                return existingOLT;
            })
            .map(oLTRepository::save)
            .map(oLTMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OLTDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OLTS");
        return oLTRepository.findAll(pageable).map(oLTMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OLTDTO> findOne(Long id) {
        log.debug("Request to get OLT : {}", id);
        return oLTRepository.findById(id).map(oLTMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OLT : {}", id);
        oLTRepository.deleteById(id);
    }

    public void updateOLT(String path) {
        String excelFilePath = path;

        try {
            // Reading data from Excel
            FileInputStream fis = new FileInputStream(excelFilePath);
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterating over rows and inserting into database
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if(row.getCell(5) != null){
                String type_equipement = row.getCell(1).getStringCellValue();
                Number tmp_code = row.getCell(2).getNumericCellValue();
                String code_equipement = tmp_code.toString();
                String libelle = row.getCell(3).getStringCellValue();
                String adresse = row.getCell(4).getStringCellValue();
                String ip = row.getCell(5).getStringCellValue();
                String type_carte = row.getCell(7).getStringCellValue();
                String vendeur = row.getCell(8).getStringCellValue();
                String latitude = String.valueOf(row.getCell(9).getNumericCellValue());
                String longitude = String.valueOf(row.getCell(10).getNumericCellValue());
                Number temp_capacite = row.getCell(11).getNumericCellValue();
                String capacite = temp_capacite.toString();
                LocalDateTime currentDateTime = LocalDateTime.now();

                OLT olt = new OLT();

                olt.typeEquipment(type_equipement);
                olt.setLibelle(libelle);
                olt.setAdresse(adresse);
                olt.setIp(ip);
                olt.setTypeCarte(type_carte);
                olt.setVendeur(vendeur);
                olt.setLatitude(latitude);
                olt.setLongitude(longitude);
                olt.setCapacite(capacite);
                olt.setCreatedAt(LocalDate.from(currentDateTime));
                olt.setUpdatedAt(LocalDate.from(currentDateTime));
                olt.setCodeEquipment(code_equipement);
                oLTRepository.save(olt);
        }
            else{
                break;
            }
    }}  catch (IOException e) {
            throw new RuntimeException(e);
        }

    }}
