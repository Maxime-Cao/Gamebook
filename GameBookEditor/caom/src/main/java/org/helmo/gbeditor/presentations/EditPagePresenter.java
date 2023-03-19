package org.helmo.gbeditor.presentations;

import org.helmo.gbeditor.domains.CanCreateBook;
import org.helmo.gbeditor.domains.CanCreateGameBookSession;
import org.helmo.gbeditor.presentations.views.ViewInterface;
import org.helmo.gbeditor.presentations.views.CanCreateEditPageView;

import java.util.List;

/**
 * La classe EditPagePresenter permet de gérer les interactions entre une vue de type "modification d'une page d'un livre-jeu" et des objets du domaine utiles à cette tâche
 */
public class EditPagePresenter implements Presenter {
    private final CanCreateEditPageView view;
    private final CanCreateGameBookSession currentSession;

    /**
     * Le constructeur de la page EditPagePresenter permet de créer une instance de EditPagePresenter sur base d'une vue et d'une session utilisateur de livre-jeu
     * @param view Vue de type "modification d'une page d'un livre-jeu"
     * @param currentSession Session utilisateur d'un livre-jeu
     */
    public EditPagePresenter(ViewInterface view, CanCreateGameBookSession currentSession) {
        if(view == null || currentSession == null) {
            throw new IllegalArgumentException("Votre vue et session doivent être correctement initialisées");
        }
        this.view = (CanCreateEditPageView) view;
        this.currentSession = currentSession;
        view.setPresenter(this);
    }

    /**
     * Permet de récupérer les pages et les choix du livre-jeu courant
     */
    public void loadPageAndChoices() {
        CanCreateBook currentBook = currentSession.getCurrentBook();
        if(currentBook != null) {
            GameBookViewModel bookViewModel = new GameBookViewModel(currentBook);
            loadPageAndChoicesInView(bookViewModel);
        }
    }

    private void loadPageAndChoicesInView(GameBookViewModel gameBookViewModel) {
           PageViewModel pageViewModel = gameBookViewModel.getBookPage(currentSession.getCurrentPageIndex());
           loadPageInView(pageViewModel);
           loadChoicesInView(pageViewModel.getChoices());
           adaptViewOnPublishState(gameBookViewModel.getPublishState());
    }

    private void loadPageInView(PageViewModel page) {
        view.loadPageFields(page);
    }

    private void loadChoicesInView(List<ChoiceViewModel> choices) {
        for(var choice : choices) {
            view.addChoiceInListView(choice);
        }
    }

    /**
     * Permet de supprimer un choix de la page courante sur base du texte du choix
     * @param content Texte du choix
     * @return Vrai si le choix a bien été supprimé, faux sinon.
     */
    public boolean deleteChoice(String content) {
        CanCreateBook currentBook = currentSession.getCurrentBook();
        return currentBook.deleteChoiceForAPage(content,currentSession.getCurrentPageIndex());
    }

    private void adaptViewOnPublishState(String publishState) {
        if("publié".equals(publishState)) {
            view.adaptViewOnPublishState(true);
        }
    }
}
