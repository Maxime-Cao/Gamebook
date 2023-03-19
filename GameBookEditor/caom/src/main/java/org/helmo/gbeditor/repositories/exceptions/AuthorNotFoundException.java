package org.helmo.gbeditor.repositories.exceptions;

/**
 * L'exception AuthorNotFoundException est déclenchée lorsque l'auteur n'a pas été trouvé sur l'espace de stockage
 */
public class AuthorNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Permet de construire l'exception AuthorNotFoundException sur base d'un message fourni
     * @param message Message de l'exception
     */
    public AuthorNotFoundException(String message) {
        super(message);
    }

    /**
     * Permet de construire l'exceptin AuthorNotFoundException sur base d'un message fourni et d'une exception qui a été déclenchée avant celle-ci
     * @param message Message de l'exception
     * @param e Exception déclenchée avant celle-ci
     */
    public AuthorNotFoundException(String message,Exception e) {
        super(message,e);
    }
}
