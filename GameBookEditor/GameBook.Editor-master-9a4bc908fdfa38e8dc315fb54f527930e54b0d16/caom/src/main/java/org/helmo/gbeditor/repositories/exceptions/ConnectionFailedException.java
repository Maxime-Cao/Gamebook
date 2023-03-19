package org.helmo.gbeditor.repositories.exceptions;

/**
 * Cette exception personnalisée provient du cours de SD de Monsieur Ludewig.
 * Elle est déclenchée lorsque la connexion à l'espace de stockage n'a pas pu se faire
 * @author François Ludewig
 */
public class ConnectionFailedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    /**
     * Construit l'exception ConnectionFailedException sur base d'un message fourni et d'une exception déclenchée avant celle-ci
     * @param s Message de l'exception
     * @param ex Exception déclenchée avant celle-ci
     */
	public ConnectionFailedException(String s, Exception ex) {
        super(s, ex);
    }
}
