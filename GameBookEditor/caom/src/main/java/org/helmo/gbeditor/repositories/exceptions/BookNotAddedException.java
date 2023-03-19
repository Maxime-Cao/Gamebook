package org.helmo.gbeditor.repositories.exceptions;

/**
 * L'exception BookNotAddedException est déclenchée lorsqu'un livre-jeu n'a pas pu être sauvegardé sur l'espace de stockage
 */
public class BookNotAddedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construit l'exception BookNotAddedException sur base d'un message fourni
     * @param message Message de l'exception
     */
    public BookNotAddedException(String message) {
        super(message);
    }

    /**
     * Construit l'exception BookNotAddedException sur base d'un message fourni et d'une exception qui a été déclenchée avant celle-ci
     * @param message Message de l'exception
     * @param e Exception déclenchée avant celle-ci
     */
    public BookNotAddedException(String message,Exception e) {
        super(message,e);
    }

}
