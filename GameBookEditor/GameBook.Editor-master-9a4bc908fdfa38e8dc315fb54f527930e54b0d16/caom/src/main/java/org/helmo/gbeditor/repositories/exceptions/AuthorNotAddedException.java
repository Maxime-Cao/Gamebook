package org.helmo.gbeditor.repositories.exceptions;

/**
 * L'exception AuthorNotAddedException est déclenchée lorsque l'auteur n'a pas pu être ajouté sur l'espace de stockage
 */
public class AuthorNotAddedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construit l'exception AuthorNotAddedException sur base d'un message fourni
     * @param message Message de l'exception
     */
    public AuthorNotAddedException(String message) {
        super(message);
    }

    /**
     * Construit l'exception AuthorNotAddedException sur base d'un message et d'une autre exception qui a été déclenchée avant celle-ci
     * @param message Message de l'exception
     * @param e Exception déclenchée avant celle-ci
     */
    public AuthorNotAddedException(String message,Exception e) {
        super(message,e);
    }
}
