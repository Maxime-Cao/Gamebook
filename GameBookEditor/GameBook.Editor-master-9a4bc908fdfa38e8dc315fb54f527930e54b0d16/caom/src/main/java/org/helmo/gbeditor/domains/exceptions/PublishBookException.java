package org.helmo.gbeditor.domains.exceptions;

/**
 * L'exception PublishBookException est déclenchée lorsque le livre n'a pas pu être publié
 */
public class PublishBookException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construit l'exception PublishBookException sur base d'un message fourni
     * @param message Message de l'exception
     */
    public PublishBookException(String message) {
        super(message);
    }

    /**
     * Construit l'exception PublishBookException sur base d'un message fourni et de l'exception qui a été déclenchée avant celle-ci
     * @param message Message de l'exception
     * @param e Exception déclenchée avant celle-ci
     */
    public PublishBookException(String message,Exception e) {
        super(message,e);
    }
}
