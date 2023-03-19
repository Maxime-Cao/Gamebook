package org.helmo.gbeditor.presentations.views;

import org.helmo.gbeditor.presentations.Presenter;

/**
 * L'interface ViewInterface fournit des méthodes communes à implémenter par les vues du livre-jeu
 */
public interface ViewInterface {
    /**
     * Permet de définir la vue principale d'une autre vue
     * @param mainView Vue principale
     */
    void setMainView(ViewInterface mainView);

    /**
     * Permet d'attacher un presenter à une vue
     * @param p Presenter à attacher à la vue
     */
    void setPresenter(Presenter p);

    /**
     * Permet d'afficher à l'écran (à l'utilisateur) un message d'information selon un style déterminé
     * @param info Message d'information
     */
    void displayInfo(String info);

    /**
     * Permet d'afficher à l'écran (à l'utilisateur) un message d'erreur selon un style déterminé
     * @param error Message d'erreur
     */
    void displayError(String error);

    /**
     * Permet de passer d'une vue à une autre sur base d'un nom de vue
     * @param viewName Nom de la vue vers laquelle on souhaite switcher
     */
    void switchScreen(String viewName);
}
