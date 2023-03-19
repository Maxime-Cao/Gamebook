package org.helmo.gbeditor.domains;

import java.util.Map;

/**
 * Cette interface définit des méthodes permettant de gérer une page d'un livre-jeu
 */
public interface CanCreateBookPage {
    /**
     * Permet d'ajouter un choix à la page courante sur base d'un texte et de la page de destination du choix
     * @param content Texte du choix
     * @param page Page de destination du choix
     */
    void addNewChoice(String content,CanCreateBookPage page);

    /**
     * Permet d'obtenir le numéro de la page
     * @return Le numéro de la page
     */
    int getNumberPage();

    /**
     * Permet d'obtenir le texte de la page
     * @return Le texte de la page
     */
    String getContent();

    /**
     * Permet de modifier le numéro de la page
     * @param newNumberPage Nouveau numéro de page
     */
    void setNumberPage(int newNumberPage);

    /**
     * Permet de récupérer une map reprenant les différents choix de la page (texte de chaque choix + page de destination)
     * @return Map reprenant les différents choix de la page
     */
    Map<String,CanCreateBookPage> getChoices();

    /**
     * Permet de récupérer une map reprenant les différents choix de la page sous la forme : texte du choix et numéro de la page ciblée par le choix
     * @return Map reprenant les différents choix de la page
     */
    Map<String,Integer> getFormattedChoices();

    /**
     * Permet de supprimer un choix de la page sur base du texte du choix
     * @param content Texte du choix
     * @return Vrai si le choix a bien été supprimé de la page, faux sinon.
     */
    boolean deleteChoice(String content);

    /**
     * Permet de déterminer si au moins un choix de la page est lié à une autre page
     * @param indexPage Indice de la page à vérifier pour chaque choix
     * @return Vrai si la page courante est liée avec au moins un choix à une autre page, faux sinon.
     */
    boolean isLinkedToPage(int indexPage);

    /**
     * Permet de supprimer tous les choix liés à une page déterminé par son indice
     * @param indexPage Indice du choix
     */
    void removeChoicesLinked(int indexPage);
}
