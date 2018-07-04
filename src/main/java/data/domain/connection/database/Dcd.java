/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.domain.connection.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class Dcd {

    public Connection connect() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/yhteydet?useSSL=false&serverTimezone=UTC",
                            "root", "salasana");
//            System.out.println("Connection saatu");
            return con;
        } catch (SQLException ex) {
            System.out.println("Tämä");
            Logger.getLogger(Dcd.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
