package org.helmo.gbeditor.domains;

/**
 * L'interface CanCreateAuthor fournit des méthodes à implémenter par un objet de type "auteur" d'un livre-jeu
 */
public interface CanCreateAuthor {
    /**
     * Permet d'obtenir le prénom d'un auteur
     * @return Le prénom d'un auteur
     */
    String getFirstname();

    /**
     * Permet d'obtenir le nom d'un auteur
     * @return Le nom d'un auteur
     */
    String getName();

    /**
     * Permet d'obtenir l'identifiant d'un auteur
     * @return L'identifiant d'un auteur
     */
    String getIdAuthor();
}
