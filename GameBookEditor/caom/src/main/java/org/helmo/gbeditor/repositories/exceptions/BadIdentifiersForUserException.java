package org.helmo.gbeditor.repositories.exceptions;

/**
 * L'exception BadIdentifiersForUserException est déclenchée lorsqu'un utilisateur se connecte avec un nom et un prénom qui ne correspond pas au matricule
 */
public class BadIdentifiersForUserException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construit l'exception BadIdentifiersForUserException sur base d'un message fourni
     * @param message Message de l'exception
     */
    public BadIdentifiersForUserException(String message) {
        super(message);
    }

    /**
     * Construit l'exception BadIdentifiersForUserException sur base d'un message fourni et d'une exception qui a été déclenchée avant celle-ci
     * @param message Message de l'exception
     * @param e Exception déclenchée avant celle-ci
     */
    public BadIdentifiersForUserException(String message,Exception e) {
        super(message,e);
    }
}
