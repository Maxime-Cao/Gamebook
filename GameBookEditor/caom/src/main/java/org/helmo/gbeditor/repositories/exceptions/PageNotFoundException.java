package org.helmo.gbeditor.repositories.exceptions;

/**
 * L'exception PageNotFoundException est déclenchée lorsqu'une page d'un livre-jeu n'a pas été trouvée sur l'espace de stockage
 */
public class PageNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construit l'exception PageNotFoundException sur base d'un message fourni
     * @param message Message de l'exception
     */
    public PageNotFoundException(String message) {
        super(message);
    }

    /**
     * Construit l'exception PageNotFoundException sur base d'un message fourni et d'une exception qui a été déclenchée avant celle-ci
     * @param message Message de l'exception
     * @param e Exception déclenchée avant celle-ci
     */
    public PageNotFoundException(String message,Exception e) {
        super(message,e);
    }
}
