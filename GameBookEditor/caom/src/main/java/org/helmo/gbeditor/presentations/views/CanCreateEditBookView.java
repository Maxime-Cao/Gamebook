package org.helmo.gbeditor.presentations.views;

import org.helmo.gbeditor.presentations.GameBookViewModel;
import org.helmo.gbeditor.presentations.PageViewModel;
import org.helmo.gbeditor.presentations.views.ViewInterface;

/**
 * L'interface CanCreateEditBookView met à disposition des méthodes communes pour les vues de type "modification d'un livre-jeu"
 */
public interface CanCreateEditBookView extends ViewInterface {
    /**
     * Affichage du contenu du livre-jeu courant sur base d'un modèle de vue
     * @param book Modèle de vue pour le livre-jeu : GameBookViewModel
     */
    void setCurrentBookInEditView(GameBookViewModel book);

    /**
     * Permet de vider la liste des pages dans la vue
     */
    void cleanPagesListView();

    /**
     * Permet d'ajouter une page à la liste des pages de la vue sur base d'un modèle de vue
     * @param page Modèle de vue : PageViewModel
     */
    void addPageInListView(PageViewModel page);
}
