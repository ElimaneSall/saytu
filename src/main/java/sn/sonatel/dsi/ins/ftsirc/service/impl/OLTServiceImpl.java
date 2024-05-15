package sn.sonatel.dsi.ins.ftsirc.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public void updateOLT(String path) {
         String excelFilePath = path;

            try {
                // Establishing connection to MySQL database
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/saytubackend", "root", "");

                // Prepared statement to truncate OLT table
                //            PreparedStatement pstmt = con.prepareStatement("TRUNCATE TABLE OLT");
                //            pstmt.executeUpdate();

                // Reading data from Excel
                FileInputStream fis = new FileInputStream(excelFilePath);
                Workbook workbook = WorkbookFactory.create(fis);
                Sheet sheet = workbook.getSheetAt(0);

                // Iterating over rows and inserting into database
                for (int rowIndex = 1; rowIndex < sheet.getLastRowNum() + 1; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
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

                    // Prepared statement to insert data into OLT table
                    PreparedStatement pstmt = con.prepareStatement(
                        "INSERT INTO OLT (type_equipment,  libelle, adresse, ip, type_carte, vendeur, latitude, longitude, capacite, created_at, updated_at, code_equipment) VALUES ( ?, ?, ?, ?,?,?,?,?,?,?,?,?)"
                    );
                    pstmt.setString(1, type_equipement);
                    //                pstmt.setString(2, code_equipement);
                    pstmt.setString(2, libelle);
                    pstmt.setString(3, adresse);
                    pstmt.setString(4, ip);
                    pstmt.setString(5, type_carte);
                    pstmt.setString(6, vendeur);
                    pstmt.setString(7, latitude);
                    pstmt.setString(8, longitude);
                    pstmt.setString(9, capacite);
                    pstmt.setObject(10, currentDateTime);
                    pstmt.setObject(11, currentDateTime);
                    pstmt.setString(12, code_equipement);
                    pstmt.executeUpdate();
                }

                // Closing resources
                workbook.close();
                fis.close();
                //pstmt.close();
                con.close();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
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
}
