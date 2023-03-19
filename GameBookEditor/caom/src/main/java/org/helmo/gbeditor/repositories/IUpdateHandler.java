package org.helmo.gbeditor.repositories;

/**
 * L'interface IUpdateHandler fournit une méthode permettant à un Presenter (ou autre) de fournir le livre actuellement consulté dans la session
 */
public interface IUpdateHandler {
    /**
     * Permet de définir le livre actuellement consulté sur base de son ISBN
     * @param isbn ISBN du livre actuellement consulté
     */
    void setCurrentEditedBook(String isbn);
}
