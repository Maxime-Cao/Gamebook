package org.helmo.gbeditor.repositories;

/**
 * L'interface IFactory fournit une méthode qui permet de créer un objet capable d'exploiter l'espace de stockage choisi
 */
public interface IFactory {
    /**
     * Permet de créer une connexion à l'espace de stockage et de retourner un objet IRepository qui permettra d'exploiter cette connexion (requêtes)
     * @return Objet IRepository
     */
    IRepository newStorageSession();
}
