package sn.sonatel.dsi.ins.ftsirc.scripts.inventaires;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.apache.poi.ss.usermodel.*;

public class InventaireOLT {

    public static void main(String[] args) {
        String excelFilePath = "C:\\Users\\Surface\\Desktop\\Sonatel_2023\\saytou\\saytu-project\\saytu-backend\\OLT_OSN_22.04.24.xlsx";

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
}
