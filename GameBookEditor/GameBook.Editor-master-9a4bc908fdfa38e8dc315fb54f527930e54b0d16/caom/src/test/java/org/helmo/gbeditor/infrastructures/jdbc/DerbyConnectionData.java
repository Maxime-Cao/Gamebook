package org.helmo.gbeditor.infrastructures.jdbc;

import org.helmo.gbeditor.repositories.ConnectionData;

public class DerbyConnectionData implements ConnectionData {
    @Override
    public String getDriverName() {
        return "org.apache.derby.jdbc.EmbeddedDriver";
    }

    @Override
    public String getUsername() {
        return "maxime";
    }

    @Override
    public String getPassword() {
        return "gbproject2022";
    }

    @Override
    public String getDBPath() {
        return "jdbc:derby:..\\ue11GameBookTest";
    }
}
