package org.helmo.gbeditor.repositories;

/**
 * L'interface ConnectionData définit des corps de méthode à implémenter pour gérer la connexion à une base de données
 */
public interface ConnectionData {
	/**
	 * Permet d'obtenir les informations du driver
	 * @return Informations du driver
	 */
	String getDriverName();

	/**
	 * Permet d'obtenir le nom d'utilisateur se connectant à la BD
	 * @return Nom d'utilisateur se connectant à la BD
	 */
	String getUsername();

	/**
	 * Permet d'obtenir le mot de passe de l'utilisateur se connectant à la BD
	 * @return
	 */
	String getPassword();

	/**
	 * Permet d'obtenir l'adresse de la base de données (chemin)
	 * @return Adresse de la base de données
	 */
	String getDBPath();
}
