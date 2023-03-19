package org.helmo.gbeditor.presentations;

import org.helmo.gbeditor.domains.CanCreateBook;
import org.helmo.gbeditor.domains.CanCreateGameBookSession;
import org.helmo.gbeditor.presentations.views.ViewInterface;

/**
 * La classe AddPagePresenter permet de gérer les interactions entre une vue de type "ajout d'une page à un livre-jeu" et d'une session utilisateur de livre-jeu
 */
public class AddPagePresenter implements Presenter {
    private final ViewInterface view;
    private final CanCreateGameBookSession currentSession;

    /**
     * Le constructeur de la classe AddPagePresenter permet de créer un objet AddPagePresenter sur base d'une vue et d'une session utilisateur de livre-jeu
     * @param view Vue de type "ajout d'une page dans un livre-jeu"
     * @param currentSession Session actuelle d'un utilisateur de livre-jeu
     */
    public AddPagePresenter(ViewInterface view,CanCreateGameBookSession currentSession) {
        if(view == null || currentSession == null) {
            throw new IllegalArgumentException("Votre vue et session doivent être correctement initialisées");
        }
        this.view = view;
        this.currentSession = currentSession;
        view.setPresenter(this);
    }

    /**
     * Permet de récupérer le nombre de pages du livre-jeu courant
     * @return Nombre de pages du livre-jeu courant
     */
    public int getNumberOfPages() {
        CanCreateBook currentBook = currentSession.getCurrentBook();
        return currentBook.getNumberOfPages();
    }

    /**
     * Permet d'ajouter une page au livre-jeu courant sur base du texte de la page et de son numéro de page
     * @param text Texte de la page
     * @param positionPage Indice de la page (numéro de la page -1)
     */
    public void addNewPage(String text,int positionPage) {
        CanCreateBook currentBook = currentSession.getCurrentBook();
        if(currentBook.addNewPageInBook(text,positionPage+1) != - 1) {
            view.displayInfo("La page a bien été ajoutée au livre");
        } else {
            view.displayError("Le contenu de la page ne doit pas être vide");
        }
    }
}
