package org.helmo.gbeditor.domains;

/**
 * L'interface CanCreateGameBookCover fournit des méthodes à implémenter par un objet de type "couverture de livre-jeu"
 */
public interface CanCreateGameBookCover {
    /**
     * Permet d'obtenir l'objet ISBN associé au livre-jeu
     * @return L'objet ISBN associé au livre-jeu
     */
    CanValidateISBN getISBN();

    /**
     * Permet d'obtenir la représentation (String) de l'ISBN du livre-jeu
     * @return La représentation (String) de l'ISBN du livre-jeu
     */
    String getISBNRepresentation();

    /**
     * Permet d'obtenir l'auteur d'un livre-jeu
     * @return L'auteur d'un livre-jeu
     */
    String getAuthorName();

    /**
     * Permet d'obtenir la langue d'un livre-jeu
     * @return Langue du livre-jeu (un chiffre de l'ISBN)
     */
    String getBookLanguage();

    /**
     * Permet d'obtenir l'identifiant de l'auteur du livre-jeu
     * @return Identifiant de l'auteur du livre-jeu (6 chiffres)
     */
    String getAuthorId();

    /**
     * Permet d'obtenir le titre d'un livre-jeu
     * @return Le titre d'un livre-jeu
     */
    String getTitle();

    /**
     * Permet d'obtenir la description d'un livre-jeu
     * @return La description d'un livre-jeu
     */
    String getResume();

    /**
     * Permet d'obtenir l'auteur du livre-jeu
     * @return L'auteur du livre-jeu
     */
    CanCreateAuthor getAuthor();

    /**
     * Permet d'obtenir l'identifiant du livre-jeu
     * @return Identifiant du livre-jeu (2 chiffres de 01 à 99)
     */
    int getIdBook();

    /**
     * Permet de mettre à jour la couverture d'un livre-jeu. Cela comprend : l'ISBN (langage et idBook), le titre du livre-jeu et sa description
     * @param language Langue du livre-jeu
     * @param idBook Identifiant du livre-jeu
     * @param title Titre du livre-jeu
     * @param resume Description du livre-jeu
     */
    void updateBookCover(String language, int idBook, String title, String resume);
}
