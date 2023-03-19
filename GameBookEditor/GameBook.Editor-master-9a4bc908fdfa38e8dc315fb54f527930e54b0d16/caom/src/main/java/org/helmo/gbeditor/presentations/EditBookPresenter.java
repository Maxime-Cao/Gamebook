package org.helmo.gbeditor.presentations;

import org.helmo.gbeditor.domains.CanCreateBook;
import org.helmo.gbeditor.domains.CanCreateBookPage;
import org.helmo.gbeditor.domains.CanCreateGameBookSession;
import org.helmo.gbeditor.presentations.views.ViewInterface;
import org.helmo.gbeditor.repositories.IFactory;
import org.helmo.gbeditor.repositories.IRepository;
import org.helmo.gbeditor.repositories.IUpdateHandler;
import org.helmo.gbeditor.presentations.views.CanCreateEditBookView;
import org.helmo.gbeditor.domains.exceptions.PublishBookException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  * La classe EditBookPresenter permet de gérer les interactions entre une vue de type "modification d'un livre-jeu" et d'une session utilisateur de livre-jeu
 */
public class EditBookPresenter implements Presenter {
    private final CanCreateEditBookView view;
    private final CanCreateGameBookSession currentSession;
    private final IFactory factory;
    private final IUpdateHandler updateHandler;
    private boolean reloadFromStorage = true;

    /**
     * Le constructeur de la classe EditBookPresenter permet de construire une instance de EditBookPresenter sur base d'une vue, d'une session utilisateur, d'une factory et d'un updateHandler
     * @param view Vue de type "modification d'un livre-jeu"
     * @param currentSession Session utilisateur de livre-jeu
     * @param factory Factory
     * @param updateHandler Updatehandler
     */
    public EditBookPresenter(ViewInterface view, CanCreateGameBookSession currentSession, IFactory factory, IUpdateHandler updateHandler) {
        if(view == null || currentSession == null || factory == null || updateHandler == null) {
            throw new IllegalArgumentException("Votre vue,session, factory et updateHandler doivent être correctement initialisés");
        }
        this.view = (CanCreateEditBookView) view;
        this.currentSession = currentSession;
        this.factory = factory;
        this.updateHandler = updateHandler;
        view.setPresenter(this);
    }

    /**
     * Permet de charger le contenu du livre-jeu courant
     */
    public void loadCurrentBook() {
        CanCreateBook currentBook = currentSession.getCurrentBook();
        if(currentBook != null) {
            updateHandler.setCurrentEditedBook(currentBook.getISBNRepresentation());
            GameBookViewModel bookViewModel = new GameBookViewModel(currentBook);
            view.setCurrentBookInEditView(bookViewModel);
            loadPagesAndChoices(bookViewModel,currentBook);
        }
    }


    private void loadPagesAndChoices(GameBookViewModel bookViewModel,CanCreateBook book) {
        if(reloadFromStorage) {
            try (IRepository repo = factory.newStorageSession()) {
                var pages = repo.loadPagesAndChoicesForABook(book.getISBNRepresentation());
                cleanListOfPages(book);
                addPagesInBook(book, pages);
                addPagesInView(bookViewModel);
                updateLoadStatus();
            } catch (Exception e) {
                view.displayError(e.getMessage());
            }
        } else {
            view.cleanPagesListView();
            addPagesInView(bookViewModel);
        }
    }

    private void addPagesInView(GameBookViewModel gameBookViewModel) {
        for(var page : gameBookViewModel.getPages()) {
            view.addPageInListView(page);
        }
    }

    private void addPagesInBook(CanCreateBook currentBook,List<CanCreateBookPage> pages) {
        Map<Integer,CanCreateBookPage> pagesWithoutChoices = new HashMap<>();
        int indexPage;
        for(CanCreateBookPage page : pages) {
            indexPage = currentBook.addNewPageInBook(page.getContent(),page.getNumberPage());
            if(indexPage != -1) {
                pagesWithoutChoices.put(indexPage,page);
            }
        }
        addChoicesInPages(currentBook,pagesWithoutChoices);
    }

    private void addChoicesInPages(CanCreateBook currentBook,Map<Integer,CanCreateBookPage> pagesWithoutChoices) {
        CanCreateBookPage currentPage;
        int indexPage;
        for(var page : pagesWithoutChoices.entrySet()) {
            currentPage = page.getValue();
            indexPage = page.getKey();
            for(var choice : currentPage.getChoices().entrySet()) {
                try {
                    currentBook.addNewChoiceForAPage(choice.getKey(), indexPage, choice.getValue().getNumberPage() - 1);
                } catch(IllegalArgumentException ex) {
                    // Choix non repris car non conforme selon mon objet métier currentBook
                    continue;
                }
            }
        }
    }

    private void cleanListOfPages(CanCreateBook currentBook) {
        currentBook.cleanPagesList();
        view.cleanPagesListView();
    }

    /**
     * Permet de définir si le contenu du livre doit être rechargé ou non depuis l'espace de stockage
     */
    public void updateLoadStatus() {
        reloadFromStorage = !reloadFromStorage;
    }

    /**
     * Permet de définir la page actuellement consultée/modifiée
     * @param indexPage Index de la page actuellement consultée/modifiée
     */
    public void setCurrentPage(int indexPage) {
        currentSession.updateCurrentPageNumber(indexPage);
    }

    /**
     * Permet d'obtenir le nombre de pages liées à une autre page (la page courante) depuis le livre-jeu courant
     * @return Le nombre de pages liées à la page courante
     */
    public int getNumberPagesLinked() {
        CanCreateBook currentBook = currentSession.getCurrentBook();
        return currentBook.getNbrLinkedPages(currentSession.getCurrentPageIndex());
    }

    /**
     * Permet de supprimer une page depuis le livre-jeu courant (la page courante sera supprimée)
     * @param checkChoices Booléen permettant de déterminer si les choix des autres pages doivent être supprimés également (non si aucune page n'est liée à la page courante)
     */
    public void deletePage(boolean checkChoices) {
        CanCreateBook currentBook = currentSession.getCurrentBook();
        int indexPage = currentSession.getCurrentPageIndex();
        if(currentBook.deletePage(indexPage,checkChoices)) {
            view.cleanPagesListView();
            addPagesInView(new GameBookViewModel(currentBook));
        }
    }

    /**
     * Permet de changer l'état de publication du livre-jeu courant (permet de le publier) avant de le sauvegarder
     * @return Vrai si l'état de publication du livre a bien été modifié, faux sinon.
     */
    public boolean publishBook() {
        CanCreateBook currentBook = currentSession.getCurrentBook();
        try {
            currentBook.publishBook();
            view.displayInfo("Votre livre vient de changer d'état, sauvegarde en cours...");
            return true;
        } catch(PublishBookException ex) {
            view.displayError(ex.getMessage());
            return false;
        }
    }

    /**
     * Permet de modifier la couverture d'un livre-jeu et puis de sauvegarder le livre-jeu sur l'espace de stockage
     * @param language Nouveau langage du livre-jeu
     * @param idBook Nouvel identifiant du livre-jeu
     * @param title Nouveau titre du livre-jeu
     * @param resume Nouvelle description du livre-jeu
     * @return Vrai si le livre a bien été sauvegardé, faux sinon.
     */
    public boolean saveBook(String language,int idBook,String title, String resume) {
        try(var repo = factory.newStorageSession()) {
            currentSession.updateCurrentBook(language,idBook,title,resume);
            repo.saveABookWithPagesAndChoices(currentSession.getCurrentBook());
            view.displayInfo("Votre livre a bien été sauvegardé");
            return true;
        } catch (Exception e) {
            view.displayError(e.getMessage());
        }
        return false;
    }
}
