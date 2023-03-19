package org.helmo.gbeditor.presentations;

import org.helmo.gbeditor.domains.CanCreateGameBookSession;
import org.helmo.gbeditor.presentations.views.ViewInterface;

/**
 * La classe MainPresenter implémente un présenteur responsable d'interagir avec une vue de type "vue principale" d'un livre-jeu et des classes du modèle utiles à cette vue
 */
public class MainPresenter implements Presenter {
    private final CanCreateGameBookSession currentSession;

    /**
     * Le constructeur de la classe MainPresenter permet de créer un objet MainPresenter sur base d'une vue et d'une session utilisateur de livre-jeu
     * @param view Vue principale du livre-jeu
     * @param currentSession Session actuelle d'un utilisateur de livre-jeu
     */
    public MainPresenter(ViewInterface view, CanCreateGameBookSession currentSession) {
        if(view == null || currentSession == null) {
            throw new IllegalArgumentException("Votre vue et session doivent être correctement initialisées");
        }
        this.currentSession = currentSession;

        view.setPresenter(this);

        view.switchScreen("LoginView");
    }

    /**
     * Permet d'obtenir le prénom et le nom de l'auteur connecté
     * @return Le prénom et le nom de l'auteur connecté
     */
    public String getAuthorName() {
        return currentSession.getCurrentAuthorFullName();
    }
}
