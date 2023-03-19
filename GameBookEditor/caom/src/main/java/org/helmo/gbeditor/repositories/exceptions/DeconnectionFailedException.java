package org.helmo.gbeditor.repositories.exceptions;

import java.sql.SQLException;


/**
 * Cette exception personnalisée provient du cours de SD de Monsieur Ludewig
 * L'exception DeconnectionFailedException est déclenchée lorsqu'un problème a été rencontré lors de la déconnexion de l'espace de stockage
 * @author François Ludewig
 */
public class DeconnectionFailedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Construit l'exception DeconnectionFailedException sur base d'un message fourni et d'une exception déclenchée avant celle-ci
	 * @param message Message de l'exception
	 * @param ex Exception déclenchée avant celle-ci
	 */
	public DeconnectionFailedException(String message,SQLException ex) {
		super(message,ex);
    }
}
