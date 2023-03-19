package org.helmo.gbeditor.presentations;

import org.helmo.gbeditor.domains.CanCreateBook;
import org.helmo.gbeditor.domains.CanCreateGameBookSession;
import org.helmo.gbeditor.presentations.views.ViewInterface;

/**
 * La classe AddChoicePresenter permet de gérer les interactions entre une vue de type "ajout de choix à une page" et d'une session utilisateur de livre-jeu
 */
public class AddChoicePresenter implements Presenter {
    private final ViewInterface view;
    private final CanCreateGameBookSession currentSession;

    /**
     * Le constructeur de la classe AddChoicePresenter permet de créer un objet AddChoicePresenter sur base d'une vue et d'une session utilisateur de livre-jeu
     * @param view Vue de type "ajout d'un choix pour une page d'un livre-jeu"
     * @param currentSession Session actuelle d'un utilisateur de livre-jeu
     */
    public AddChoicePresenter(ViewInterface view,CanCreateGameBookSession currentSession) {
        if(view == null || currentSession == null) {
            throw new IllegalArgumentException("Votre vue et session doivent être correctement initialisées");
        }
        this.view = view;
        this.currentSession = currentSession;
        view.setPresenter(this);
    }

    /**
     * Permet de récupérer le nombre de pages du livre-jeu courant
     * @return Nombre de page du livre-jeu courant
     */
    public int getNumberOfPages() {
        CanCreateBook currentBook = currentSession.getCurrentBook();
        if(currentBook != null) {
            return currentBook.getNumberOfPages();
        } else {
            return 0;
        }
    }

    /**
     * Permet d'ajouter un choix (texte du choix et indice de la page de destination) à la page courante
     * @param content Texte de la page
     * @param indexPageDest Indice de la page de destination
     */
    public void addChoiceInPage(String content,int indexPageDest) {
        CanCreateBook currentBook = currentSession.getCurrentBook();
        if(currentBook != null) {
            try {
                validateIndexPageDest(indexPageDest);
                currentBook.addNewChoiceForAPage(content,currentSession.getCurrentPageIndex(),indexPageDest);
                view.displayInfo("Le choix a bien été ajouté à la page");
            } catch(IllegalArgumentException ex) {
                view.displayError(ex.getMessage());
            }
        }
    }

    private void validateIndexPageDest(int indexPageDest) {
        if(indexPageDest < 0) {
            throw new IllegalArgumentException("Veuillez sélectionner une page de destination pour le choix");
        }
    }
}
