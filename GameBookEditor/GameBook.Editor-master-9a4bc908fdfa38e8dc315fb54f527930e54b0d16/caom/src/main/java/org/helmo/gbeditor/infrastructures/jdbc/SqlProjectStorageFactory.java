package org.helmo.gbeditor.infrastructures.jdbc;

import org.helmo.gbeditor.repositories.*;
import org.helmo.gbeditor.repositories.exceptions.ConnectionFailedException;
import org.helmo.gbeditor.repositories.exceptions.JdbcDriverNotFoundException;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * La classe SqlProjectStorageFactory représente une "factory", son rôle est de créer une connexion à une BD et de retourner un objet SqlProjectStorage responsable des requêtes vers cette BD
 */
public class SqlProjectStorageFactory implements IFactory {
    private final String db;
    private final String username;
    private final String password;
    private final UpdateHandler updateHandler;

    /**
     * Le constructeur de la classe SqlProjectStorageFactory permet de créer une instance de SqlProjectStorageFactory sur base d'un objet ConnectionData et d'un UpdateHandler
     * @param connectionData Objet ConnectionData
     * @param updateHandler Objet UpdateHandler
     */
    public SqlProjectStorageFactory(ConnectionData connectionData, UpdateHandler updateHandler) {
        try {
            Class.forName(connectionData.getDriverName());
            this.db = connectionData.getDBPath();
            this.username = connectionData.getUsername();
            this.password = connectionData.getPassword();
            this.updateHandler = updateHandler;
        } catch(ClassNotFoundException e) {
            throw new JdbcDriverNotFoundException(connectionData.getDriverName());
        }
    }

    @Override
    public IRepository newStorageSession() {
        try {
            return new SqlProjectStorage(DriverManager.getConnection(db,username,password),updateHandler);
        } catch (SQLException ex) {
            throw new ConnectionFailedException("Impossible d'accéder à la base de données",ex);
        }
    }
}
