package org.helmo.gbeditor.repositories.exceptions;

/**
 * L'exception BookNotFoundException est déclenchée lorsqu'un livre-jeu n'a pas pu être trouvé sur l'espace de stockage
 */
public class BookNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Construit l'exception BookNotFoundException sur base d'un message fourni
	 * @param message Message de l'exception
	 */
	public BookNotFoundException(String message) {
	    super(message);
    }

	/**
	 * Construit l'exception BookNotFoundException sur base d'un message fourni et d'une exception qui a été déclenchée avant celle-ci
	 * @param message Message de l'exception
	 * @param e Exception déclenchée avant celle-ci
	 */
	public BookNotFoundException(String message, Exception e) {
		super(message,e);
	}
}
