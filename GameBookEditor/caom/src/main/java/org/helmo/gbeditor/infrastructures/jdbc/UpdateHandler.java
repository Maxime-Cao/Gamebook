package org.helmo.gbeditor.infrastructures.jdbc;

import org.helmo.gbeditor.domains.CanCreateBookPage;
import org.helmo.gbeditor.domains.Page;
import org.helmo.gbeditor.repositories.IUpdateHandler;

import java.util.*;

/**
 * La classe UpdateHandler implémente l'interface IUpdateHandler et contient la liste des livres récupérés en BD, elle permet donc de maintenir la cohérence du livre en cas de mise à jour des éléments des livres
 */
public class UpdateHandler implements IUpdateHandler {
    private final SortedMap<Integer,String> booksMap = new TreeMap<>();
    private final SortedMap<Integer,CanCreateBookPage> currentBookPages = new TreeMap<>();
    private int currentEditedBookId = -1;

    @Override
    public void setCurrentEditedBook(String isbn) {
        for(var book : booksMap.entrySet()) {
            if(book.getValue().equals(isbn)) {
                this.currentEditedBookId = book.getKey();
            }
        }
    }

    /**
     * Permet d'obtenir l'identifiant en base de données du livre-jeu courant
     * @return Identifiant en BD du livre-jeu courant (actuellement modifié)
     */
    public int getCurrentEditBookId() {
        return currentEditedBookId;
    }

    /**
     * Permet de récupérer un identifiant en base de données d'un livre-jeu sur base de son ISBN
     * @param isbn ISBN du livre-jeu
     * @return Identifiant du livre-jeu en BD
     */
    public int getBookIdWithIsbn(String isbn) {
        for(var book : booksMap.entrySet()) {
            if(book.getValue().equals(isbn))  {
                return book.getKey();
            }
        }
        return -1;
    }

    /**
     * Permet d'obtenir la liste des pages récupérées en BD
     * @return Liste des pages récupérées en BD
     */
    public List<CanCreateBookPage> getPages() {
        return new ArrayList<>(currentBookPages.values());
    }

    /**
     * Permet d'obtenir une map reprenant la liste des pages récupérées en BD et les identifiants en BD de ces pages
     * @return La map reprenant la liste des pages récupérées en BD et les identifiants en BD de ces pages
     */
    public Map<Integer,CanCreateBookPage> getMapPages() {
        return currentBookPages;
    }

    /**
     * Permet de récupérer une page dans la liste des pages sur base de son identifiant
     * @param idPage Identifiant de la page
     * @return La page désirée si celle-ci existe dans la map des pages, sinon null.
     */
    public CanCreateBookPage getPageWithId(int idPage) {
        if(currentBookPages.containsKey(idPage)) {
            return currentBookPages.get(idPage);
        }
        return null;
    }

    /**
     * Permet d'obtenir l'identifiant d'une page sur base d'une instance de CanCreateBookPage
     * @param page Instance de CanCreateBookPage
     * @return Identifiant de la page si celle-ci existe dans la map des pages, sinon -1;
     */
    public int getIdPage(CanCreateBookPage page) {
       for(var pageInMap : currentBookPages.entrySet()) {
           if(pageInMap.getValue().equals(page)) {
               return pageInMap.getKey();
           }
       }
       return -1;
    }

    /**
     * Permet d'ajouter un livre-jeu à la liste des livres (ajout de son ID en BD + son ISBN)
     * @param idBook Identifiant du livre-jeu en BD
     * @param isbn ISBN du livre-jeu
     */
    public void addBook(int idBook,String isbn) {
        if(isbn != null && idBook > 0) {
            booksMap.put(idBook,isbn);
        }
    }

    /**
     * Permet d'ajouter une page sans choix à la map des pages (ajout de son ID en BD + son instance de CanCreateBookPage)
     * @param idPage Identifiant de la page en BD
     * @param page Instance de CanCreateBookPage
     */
    public void addPageWithoutChoice(int idPage,CanCreateBookPage page) {
        if(page != null && idPage > 0) {
            currentBookPages.put(idPage,new Page(page.getNumberPage(),page.getContent()));
        }
    }

    /**
     * Permet d'ajouter une page avec choix à la map des pages (ajout de son ID en BD + son instance de CanCreateBookPage)
     * @param idPage Identifiant de la page en BD
     * @param page Instance de CanCreateBookPage
     */
    public void addPage(int idPage,CanCreateBookPage page) {
        if(page != null && idPage > 0) {
            CanCreateBookPage pageToAdd =  new Page(page.getNumberPage(),page.getContent());
            for(var choice : page.getChoices().entrySet()) {
                pageToAdd.addNewChoice(choice.getKey(),choice.getValue());
            }
            currentBookPages.put(idPage,pageToAdd);
        }
    }

    /**
     * Permet de supprimer une page de la map des pages sur base de son identifiant
     * @param idPage Identifiant de la page
     */
    public void removePage(int idPage) {
        if(currentBookPages.containsKey(idPage)) {
            currentBookPages.remove(idPage);
        }
    }

    /**
     * Permet de vider la map des livres
     */
    public void clearBooksMap() {
        booksMap.clear();
    }

    /**
     * Permet de vider la map des pages
      */
    public void clearPagesMap() {
        currentBookPages.clear();
    }

    /**
     * Permet d'obtenir une page de la map des pages de l'UpdateHandler sur base d'une page de la session utilisateur de livre-jeu courante
     * @param page Instance de CanCreateBookPage : page venant de la session utilisateur de livre-jeu courante
     * @return La page trouvée dans la map des pages de l'UpdateHandler ou null si aucune page n'a été trouvée
     */
    public CanCreateBookPage getPage(CanCreateBookPage page) {
        for(var pageInList : currentBookPages.entrySet()) {
            if(pageInList.getValue().getNumberPage() == page.getNumberPage()) {
                return pageInList.getValue();
            }
        }
        return null;
    }
}
