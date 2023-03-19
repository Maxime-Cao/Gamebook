package org.helmo.gbeditor.infrastructures.jdbc;

import org.helmo.gbeditor.repositories.ConnectionData;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.fail;

public class ConnectionTest {
    @Test
    public void getDerbyConnection() {
        ConnectionData connectionData = new DerbyConnectionData();

        try {
            Class.forName(connectionData.getDriverName());
        } catch (ClassNotFoundException e) {
            fail("Driver not found : " + e.getMessage());
        }

        try(Connection co = DriverManager.getConnection(connectionData.getDBPath(),connectionData.getUsername(),connectionData.getPassword())) {

        } catch (SQLException ex) {
            fail("Create connection failed : " + ex.getMessage());
        }
    }
}
