package org.helmo.gbeditor.presentations;

/**
 * La classe ChoiceViewModel représente un modèle de vue pour les choix des pages d'un livre-jeu
 */
public class ChoiceViewModel {
    private String textChoice;
    private int numberPageDest;

    /**
     * Le constructeur de la classe ChoiceViewModel permet de construire une instance de ChoiceViewModel sur base du texte d'un choix et de son numéro de page de destination
     * @param textChoice
     * @param numberPageDest
     */
    public ChoiceViewModel(String textChoice,int numberPageDest) {
        if(textChoice != null && !textChoice.isBlank()) {
           this.textChoice = textChoice;
           this.numberPageDest = numberPageDest;
        } else {
            throw new IllegalArgumentException("Le texte de votre choix ne doit pas être null ou vide");
        }
    }

    /**
     * Permet d'obtenir le texte d'un choix à afficher
     * @return Texte du choix à afficher
     */
    public String getTextChoice() {
        return textChoice;
    }

    /**
     * permet d'obtenir le numéro de la page de destination d'un choix à afficher
     * @return Numéro de la page de destination d'un choix à afficher
     */
    public int getNumberPageDest() {
        return numberPageDest;
    }
}
