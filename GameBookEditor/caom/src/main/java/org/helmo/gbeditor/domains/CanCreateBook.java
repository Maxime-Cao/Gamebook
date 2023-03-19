package org.helmo.gbeditor.domains;

import java.util.List;

/**
 * L'interface CanCreateBook fournit des méthodes à implémenter par un objet de type "livre-jeu"
 */
public interface CanCreateBook {
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
     * Permet de récupérer l'auteur d'un livre-jeu
      * @return L'auteur d'un livre-jeu
     */
    CanCreateAuthor getAuthor();

    /**
     * Permet d'obtenir l'identifiant du livre-jeu (2 chiffres)
     * @return 2 chiffres - Identifiant du livre-jeu
     */
    int getIdBook();

    /**
     * Permet d'obtenir l'état de publication du livre-jeu
     * @return L'état de publication du livre-jeu
     */
    boolean isPublishState();

    /**
     * Permet d'obtenir l'identifiant de l'auteur du livre-jeu
     * @return L'identifiant de l'auteur (6 chiffres)
     */
    String getAuthorId();

    /**
     * Permet d'obtenir la langue du livre-jeu
     * @return Langue du livre-jeu (1 chiffre de l'ISBN)
     */
    String getBookLanguage();

    /**
     * Permet de récupérer la liste des pages du livre-jeu
     * @return Liste des pages du livre-jeu
     */
    List<CanCreateBookPage> getPages();

    /**
     * Permet de vider la liste des pages d'un livre-jeu
     */
    void cleanPagesList();

    /**
     * Permet d'ajouter une page dans un livre-jeu sur base d'un contenu et d'un numéro de page
     * @param textPage Contenu de la page
     * @param numberPage Numéro de la page
     * @return L'index de la page ajoutée dans la liste des pages du livre-jeu
     */
    int addNewPageInBook(String textPage,int numberPage);

    /**
     * Permet d'obtenir le nombre de pages du livre-jeu
     * @return Nombre de pages du livre-jeu
     */
    int getNumberOfPages();

    /**
     * Permet d'ajouter à un choix dans une page du livre-jeu sur base de l'index de la page courante, de l'index de la page de destination du choix et du texte du choix
     * @param content Texte du choix
     * @param indexPage Index de la page courante dans la liste de pages du livre-jeu
     * @param indexPageDest Index de la page de destination dans la liste de pages du livre-jeu
     */
    void addNewChoiceForAPage(String content,int indexPage,int indexPageDest);

    /**
     * Permet de récupérer une page du livre-jeu sur base de son indice dans la liste des pages du livre-jeu
     * @param indexPage Indice de la page dans la liste des pages du livre-jeu
     * @return La page correspondant à l'indice fourni
     */
    CanCreateBookPage getBookPage(int indexPage);

    /**
     * Permet de supprimer un choix dans une page du livre-jeu sur base de l'indice de la page courante dans la liste des pages du livre-jeu et du texte du choix
     * @param content Texte du choix
     * @param indexPage Indice de la page
     * @return Vrai si la page a bien été supprimée, faux sinon.
     */
    boolean deleteChoiceForAPage(String content,int indexPage);

    /**
     * Permet d'obtenir le nombre de pages dont un ou plusieurs choix font référence à une page (déterminé par l'index de la page fourni)
     * @param indexPage Index de la page
     * @return Le nombre de pages qui font référence à une autre page dans leurs choix
     */
    int getNbrLinkedPages(int indexPage);

    /**
     * Permet de supprimer une page de la liste des pages du livre-jeu sur base de son indice
     * @param indexPage Indice de la page à supprimer du livre-jeu
     * @param checkChoices Booléen permettant de déterminer si les choix des autres pages doivent être supprimés ou non, s'ils contiennent une référence vers la page supprimée (faux si getNbrLinkedPages = 0)
     * @return Vrai si la page a bien été supprimée du livre-jeu, faux sinon.
     */
    boolean deletePage(int indexPage,boolean checkChoices);

    /**
     * Permet de publier le livre (l'état de publication du livre passe à vrai)
     */
    void publishBook();

    /**
     * Permet de mettre à jour un livre-jeu. Les mises-à-jour peuvent s'appliquer à l'isbn (language et idBook),le titre du livre et sa description
     * @param language Langage du livre-jeu
     * @param idBook Identifiant du livre-jeu (2 chiffres)
     * @param title Titre du livre-jeu
     * @param resume Description du livre-jeu
     */
    void updateBook(String language,int idBook,String title, String resume);
}
