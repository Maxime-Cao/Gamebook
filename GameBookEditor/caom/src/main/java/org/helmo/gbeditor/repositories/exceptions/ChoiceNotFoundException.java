package org.helmo.gbeditor.repositories.exceptions;

/**
 * L'exception ChoiceNotFoundException est déclenchée lorsque le choix n'a pas été trouvé sur l'espace de stockage
 */
public class ChoiceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construit l'exception ChoiceNotFoundException sur base d'un message fourni
     * @param message Message de l'exception
     */
    public ChoiceNotFoundException(String message) {
        super(message);
    }

    /**
     * Construit l'exception ChoiceNotFoundException sur base d'un message fourni et d'une exception déclenchée avant celle-ci
     * @param message Message de l'exception
     * @param e Exception déclenchée avant celle-ci
     */
    public ChoiceNotFoundException(String message, Exception e) {
        super(message,e);
    }
}
