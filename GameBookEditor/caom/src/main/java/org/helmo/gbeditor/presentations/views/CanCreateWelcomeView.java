package org.helmo.gbeditor.presentations.views;

import org.helmo.gbeditor.presentations.GameBookViewModel;
import org.helmo.gbeditor.presentations.views.ViewInterface;

/**
 * L'interface WelcomeViewInterface fournit des méthodes à implémenter pour les vues de type "écran d'accueil" d'un livre-jeu
 */
public interface CanCreateWelcomeView extends ViewInterface {
    /**
     * Permet d'ajouter un livre-jeu à la liste des livres de la vue sur base d'un modèle de vue
     * @param book Modèle de vue : GameBookViewModel
     */
    void addBookInListView(GameBookViewModel book);

    /**
     * Permet de vider la liste de livres de la vue
     */
    void cleanBooksListView();
}
