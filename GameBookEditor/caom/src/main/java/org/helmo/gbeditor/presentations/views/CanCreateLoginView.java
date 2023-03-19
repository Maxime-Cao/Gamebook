package org.helmo.gbeditor.presentations.views;

import org.helmo.gbeditor.presentations.views.ViewInterface;

/**
 * L'interface LoginViewInterface met à disposition des méthodes communes pour les vues de type "page de connexion"
 */
public interface CanCreateLoginView extends ViewInterface {
    /**
     * Permet de supprimer tous les composants de la vue sauf le message d'erreur en cas d'erreur grave (impossible de se connecter à l'espace de stockage)
     */
    void deleteComponentsInView();
}
