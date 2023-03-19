package org.helmo.gbeditor.repositories.exceptions;

/**
 * Cette exception personnalisée provient du cours de SD de Monsieur Ludewig
 * L'exception JdbcDriverNotFoundException est déclenchée lorsque le driver permettant de se connecter à l'espace de stockage n'a pas été trouvé
 * @author François Ludewig
 */
public class JdbcDriverNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    /**
     * Construit l'exception JdbcDriverNotFoundException sur base du nom du driver et d'un message prédéfini
     * @param driverName Nom du driver
     */
	public JdbcDriverNotFoundException(String driverName) {
        super("Unable to load driver : "+driverName+". Is it available from the class path?");
    }
}
