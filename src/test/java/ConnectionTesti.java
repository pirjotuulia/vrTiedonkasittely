
import data.domain.connection.ConnectionSeeker;
import data.domain.connection.database.Dcd;
import java.sql.SQLException;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class ConnectionTesti {

//    @Test
//    public void testaaYhteys() {
//        Dcd dcd = new Dcd();
//        assertEquals("Connection saatu", dcd.connect());
//    }
    
    @Test
    public void shortCodeIdlla() throws SQLException {
        ConnectionSeeker cs = new ConnectionSeeker();
        String shortcode = cs.shortCodeFinder(741);
        assertEquals(shortcode, "LH");
    }
}
