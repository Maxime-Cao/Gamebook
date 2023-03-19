package org.helmo.gbeditor.repositories.exceptions;

/**
 * Cette exception personnalisée provient du cours de SD de Monsieur Ludewig
 * L'exception UnableToSaveException est déclenchée lorsqu'un problème a été rencontré lors d'une sauvegarde sur l'espace de stockage
 * @author François Ludewig
 */
public class UnableToSaveException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    /**
     * Construit l'exception UnableToSaveException sur base d'une exception qui a été déclenchée avant celle-ci
     * @param ex Exception déclenchée avant celle-ci
     */
	public UnableToSaveException(Exception ex) {
        super(ex);
    }
}
