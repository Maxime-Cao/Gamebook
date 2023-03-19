package org.helmo.gbeditor.presentations;

import org.helmo.gbeditor.domains.CanCreateBook;
import org.helmo.gbeditor.domains.CanCreateGameBookSession;
import org.helmo.gbeditor.presentations.views.ViewInterface;
import org.helmo.gbeditor.repositories.IFactory;
import org.helmo.gbeditor.repositories.IRepository;
import org.helmo.gbeditor.presentations.views.CanCreateWelcomeView;

import java.util.List;

/**
 * La classe WelcomePresenter implémente un présenteur responsable d'interagir avec une vue de type "page d'accueil", une session utilisateur de livre-jeu et une factory
 */
public class WelcomePresenter implements Presenter {
    private final CanCreateWelcomeView view;
    private final CanCreateGameBookSession currentSession;
    private final IFactory factory;

    /**
     * Le constructeur de la classe WelcomePresenter permet de créer un objet WelcomePresenter sur base d'une vue, d'une session utilisateur de livre-jeu et d'une factory
     * @param view Vue de type "page d'accueil" d'un livre-jeu
     * @param currentSession Session actuelle d'un utilisateur de livre-jeu
     * @param factory Classe permettant la gestion et la sauvegarde de livres
     */
    public WelcomePresenter(ViewInterface view, CanCreateGameBookSession currentSession, IFactory factory) {
        if(view == null || currentSession == null || factory == null) {
            throw new IllegalArgumentException("Votre vue, session et factory doivent être correctement initialisés");
        }
        this.view = (CanCreateWelcomeView) view;
        this.currentSession = currentSession;
        this.factory = factory;

        view.setPresenter(this);
    }

    /**
     * Permet de récupérer les livres sauvegardés et de les associer à la liste de livres de la session courante
     * @return Vrai si les livres ont pu être chargé et associé à la session courante, faux sinon.
     */
    public boolean loadBooks() {
        try(IRepository repo = factory.newStorageSession()) {
            List<CanCreateBook> books =  repo.loadAllBooksWithIdAuthor(currentSession.getIdAuthor());
            cleanListsOfBooks();
            addBooksInSession(books);
            addBooksInView();
            return true;
        } catch (Exception e) {
            view.displayError(e.getMessage());
            return false;
        }
    }

    /**
     * Permet de définir le livre-jeu courant dans la session actuelle sur base de son index
     * @param indexBook Index du livre-jeu dans la liste de livre de la session
     */
    public void switchCurrentBook(int indexBook) {
        currentSession.switchCurrentBook(indexBook);
    }

    private void cleanListsOfBooks() {
        currentSession.cleanBooksList();
        view.cleanBooksListView();
    }

    private void addBooksInSession(List<CanCreateBook> books) {
        for(CanCreateBook book : books) {
            currentSession.setGameBook(book.getISBNRepresentation(),book.getTitle(),book.getResume(),book.isPublishState());
        }
    }

    private void addBooksInView() {
        for(CanCreateBook book : currentSession.getBooks()) {
            view.addBookInListView(new GameBookViewModel(book));
        }
    }
}
