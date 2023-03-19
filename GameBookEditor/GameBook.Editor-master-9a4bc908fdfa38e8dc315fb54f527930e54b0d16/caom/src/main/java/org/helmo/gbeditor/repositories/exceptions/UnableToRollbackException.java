package org.helmo.gbeditor.repositories.exceptions;

import java.sql.SQLException;

/**
 * Cette exception personnalisée provient du cours de SD de Monsieur Ludewig
 * L'exception UnableToRollbackException est déclenchée lorsqu'un rollback d'une sauvegarde sur l'espace de stockage n'a pas su se faire correctement
 * @author François Ludewig
 */
public class UnableToRollbackException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    /**
     * Construit l'exception UnableToRollbackException sur base de l'exception déclenchée avant celle-ci
     * @param ex Exception déclenchée avant celle-ci
     */
	public UnableToRollbackException(SQLException ex) {
        super(ex);
    }
}
