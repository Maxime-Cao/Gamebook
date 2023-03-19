package org.helmo.gbeditor.infrastructures.jdbc;

import org.helmo.gbeditor.repositories.ConnectionData;

/**
 * La classe MySQLConnectionData implémente l'interface ConnectionData et permet la connexion à une base de données de type "MySql"
 */
public class MySQLConnectionData implements ConnectionData {
    @Override
    public String getDriverName() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public String getUsername() {
        return "in20b1054";
    }

    @Override
    public String getPassword() {
        return "8319";
    }

    @Override
    public String getDBPath() {
        return "jdbc:mysql://192.168.128.13:3306/in20b1054?useSSL=false&serverTimeZone=UTC";
    }
}
