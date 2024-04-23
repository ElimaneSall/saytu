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
        String excelFilePath = "/Users/nfl/Documents/Sonatel/SAYTU/OLT_OSN_22.04.24.xlsx";

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
            for (Row row : sheet) {
                String type_equipement = row.getCell(1).getStringCellValue();
                String code_equipement = row.getCell(2).getStringCellValue();
                String libelle = row.getCell(3).getStringCellValue();
                String adresse = row.getCell(4).getStringCellValue();
                String ip = row.getCell(5).getStringCellValue();
                String type_carte = row.getCell(6).getStringCellValue();
                String vendeur = row.getCell(7).getStringCellValue();
                String latitude = row.getCell(8).getStringCellValue();
                String longitude = row.getCell(9).getStringCellValue();
                String capacite = row.getCell(10).getStringCellValue();
                LocalDateTime currentDateTime = LocalDateTime.now();

                // Prepared statement to insert data into OLT table
                PreparedStatement pstmt = con.prepareStatement(
                    "INSERT INTO OLT (type_equipment, code_equipment, libelle, adresse, ip, type_carte, vendeur, latitude, longitude, capacite, created_at, updated_at) VALUES (?, ?, ?, ?,?,?,?,?,?,?,?,?)"
                );
                pstmt.setString(1, type_equipement);
                pstmt.setString(2, code_equipement);
                pstmt.setString(3, libelle);
                pstmt.setString(4, adresse);
                pstmt.setString(5, ip);
                pstmt.setString(6, type_carte);
                pstmt.setString(7, vendeur);
                pstmt.setString(8, latitude);
                pstmt.setString(9, longitude);
                pstmt.setString(10, capacite);
                pstmt.setObject(11, currentDateTime);
                pstmt.setObject(12, currentDateTime);
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
