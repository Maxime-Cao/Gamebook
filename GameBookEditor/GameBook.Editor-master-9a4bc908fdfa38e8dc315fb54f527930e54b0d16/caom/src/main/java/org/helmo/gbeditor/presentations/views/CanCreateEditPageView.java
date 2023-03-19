package org.helmo.gbeditor.presentations.views;

import org.helmo.gbeditor.presentations.ChoiceViewModel;
import org.helmo.gbeditor.presentations.PageViewModel;
import org.helmo.gbeditor.presentations.views.ViewInterface;

/**
 * L'interface CanCreateEditPageView met à disposition des méthodes communes pour les vues de type "modification des pages d'un livre-jeu"
 */
public interface CanCreateEditPageView extends ViewInterface {

    /**
     * Permet d'ajouter un choix à la liste des choix de la vue
     * @param choice Choix à ajouter (modèle de vue)
     */
    void addChoiceInListView(ChoiceViewModel choice);

    /**
     * Permet de définir la valeur des champs de la vue pour la page courante sur base d'un modèle de vue
     * @param page PageViewModel (modèle de vue d'une page d'un livre-jeu)
     */
    void loadPageFields(PageViewModel page);

    /**
     * Permet d'adapter la vue en fonction du statut de publication du livre
     * @param publishState Statut de publication du livre (vrai = publié)
     */
    void adaptViewOnPublishState(boolean publishState);
}
