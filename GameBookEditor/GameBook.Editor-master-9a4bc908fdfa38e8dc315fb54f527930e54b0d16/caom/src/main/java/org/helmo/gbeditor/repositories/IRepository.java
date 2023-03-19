package org.helmo.gbeditor.repositories;

import org.helmo.gbeditor.domains.CanCreateBook;
import org.helmo.gbeditor.domains.CanCreateBookPage;

import java.io.IOException;
import java.util.List;

/**
 * L'interface IRepository fournit des méthodes à implémenter qui permettent le stockage d'une liste de livres
 */
public interface IRepository extends AutoCloseable {
    /**
     * Permet de sauvegarder une liste de livres (peu importe la méthode de stockage choisie)
     * @param books
     */
    void saveBooks(List<CanCreateBook> books) throws IOException;

    /**
     * Permet de récupérer la liste des livres déjà sauvegardées
     * @return La liste des livres sauvegardés
     */
    List<CanCreateBook> loadAllBooks() throws Exception;

    /**
     * Permet d'obtenir la liste des livres sauvegardées d'un auteur sur base de son identifiant
     * @param idAuthor Identifiant de l'auteur
     * @return La liste des livres sauvegardés
     */
    List<CanCreateBook> loadAllBooksWithIdAuthor(String idAuthor);

    /**
     * Permet d'ajouter un auteur dans l'espace de stockage sur base de son identifiant, nom et prénom
     * @param idAuthor Identifiant de l'auteur
     * @param firstname Prénom de l'auteur
     * @param lastname Nom de l'auteur
     */
    void addAuthor(String idAuthor,String firstname,String lastname);

    /**
     * Permet de sauvegarder un livre sans ses pages et ses choix sur l'espace de stockage
     * @param book Le livre à sauvegarder
     */
    void saveABookWithoutPage(CanCreateBook book);

    /**
     * Permet de sauvegarder un livre avec ses pages et ses choix sur l'espace de stockage
     * @param book Le livre à sauvegarder
     */
    void saveABookWithPagesAndChoices(CanCreateBook book);

    /**
     * Permet d'obtenir la liste des pages avec choix d'un livre sur base de son ISBN, depuis l'espace de stockage
     * @param isbn ISBN du livre
     * @return Liste des pages avec choix du livre souhaité
     */
    List<CanCreateBookPage> loadPagesAndChoicesForABook(String isbn);
}
