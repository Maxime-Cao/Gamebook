package org.helmo.gbeditor.domains;

import java.util.List;

/**
 * L'interface CanCreateGameBookSession fournit des méthodes à implémenter par un objet de type "session" d'un livre-jeu
 */
public interface CanCreateGameBookSession {
    /**
     * Permet de récupérer le nom de l'auteur connecté
     * @return Le nom de l'auteur connecté
     */
    String getCurrentAuthorFullName();

    /**
     * Permet d'associer l'auteur connecté à la session de livre-jeu courante
     * @param firstname Prénom de l'auteur connecté
     * @param lastname Nom de l'auteur connecté
     * @param idAuthor Identifiant de l'auteur connecté
     */
    void setAuthor(String firstname, String lastname,String idAuthor);

    /**
     * Permet de demander à la classe responsable de la création du livre-jeu, la création d'un nouveau livre sur base du langage du livre, de l'identifiant du livre, du titre du livre et de sa description
     * @param language Langage du livre-jeu
     * @param idBook Identifiant du livre-jeu
     * @param title Titre du livre-jeu
     * @param resume Description du livre-jeu
     * @return Vrai si le livre-jeu a pu être créé correctement, faux sinon.
     */
    boolean setGameBook(String language,int idBook,String title, String resume);

    /**
     * Retourne la liste des livres créés par l'auteur connecté
     * @return La liste des livres créés par l'auteur connecté
     */
    List<CanCreateBook> getBooks();

    /**
     * Permet de demander à la classe responsable de la création du livre-jeu, la création d'un nouveau livre sur base d'un ISBN déjà formatté, du titre du livre-jeu et de la description du livre-jeu
     * @param isbn Numéro ISBN déjà formatté
     * @param title Titre du livre-jeu
     * @param resume Description du livre-jeu
     * @return Vrai si le livre-jeu a pu être créé correctement, faux sinon.
     */
    boolean setGameBook(String isbn,String title, String resume,boolean publishState);

    /**
     * Permet de vider la liste des livres de la session courante
     */
    void cleanBooksList();

    /**
     * Permet de récupérer l'identifiant de l'auteur connecté
     * @return Identifiant de l'auteur connecté
     */
    String getIdAuthor();

    /**
     * Permet d'obtenir l'auteur connecté
     * @return Auteur connecté à la session courante
     */
    CanCreateAuthor getCurrentAuthor();

    /**
     * Permet d'obtenir le premier identifiant disponible pour un livre-jeu dans une langue donnée
     * @param language Langue du livre-jeu
     * @return Le premier identifiant disponible
     */
    int getFirstIdBookAvailableInSession(String language);

    /**
     * Permet de définir le livre courant de la session (utile lors de la consultation ou modification d'un livre) sur base de son indice dans la liste de livres
     * @param indexBook Indice dans la liste des livres de la session
     */
    void switchCurrentBook(int indexBook);

    /**
     * Permet d'obtenir le livre courant de la session
     * @return Le livre courant
     */
    CanCreateBook getCurrentBook();

    /**
     * Permet d'obtenir l'index de la page actuellement consultée/modifiée par l'utilisateur
     * @return La page actuellement consultée par l'utilisateur (auteur)
     */
    int getCurrentPageIndex();

    /**
     * Permet de modifier la page actuellement consultée par l'auteur sur base de son indice dans le livre-jeu
     * @param indexPage Indice de la page dans le livre-jeu
     */
    void updateCurrentPageNumber(int indexPage);

    /**
     * Permet de modifier le livre courant de la session
     * @param language Nouvelle langue du livre-jeu
     * @param idBook Nouvel identifiant du livre-jeu
     * @param title Nouveau titre du livre-jeu
     * @param resume Nouvelle description du livre-jeu
     * @return Vrai si le livre a bien été modifié, faux sinon.
     */
    boolean updateCurrentBook(String language,int idBook,String title, String resume);
}
