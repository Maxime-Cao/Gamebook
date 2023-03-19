package org.helmo.gbeditor.domains;

/**
 * L'interface CanValidateISBN fournit des méthodes à implémenter permettant de valider l'ISBN d'un livre
 */
public interface CanValidateISBN {
    /**
     * Permet de retourner l'ISBN du livre-jeu sous la forme d'une chaine de caractères
     * @return L'ISBN du livre-jeu sous la forme d'une chaine de caractères
     */
    String getISBNRepresentation();

    /**
     * Permet de valider la langue d'un livre-jeu
     * @param language Langue du livre-jeu
     * @return Vrai si la langue du livre est correcte, faux sinon.
     */
    boolean validateLanguageNumber(String language);

    /**
     * Permet de valider l'identifiant du livre-jeu
     * @param numberToCheck Identifiant unique du livre-jeu à valider
     * @return Vrai si l'identifiant du livre-jeu est correct, faux sinon.
     */
    boolean validateIdBook(int numberToCheck);

    /**
     * Permet de valider l'identifiant de l'auteur du livre-jeu
     * @param matriculeToCheck Identifiant de l'auteur du livre-jeu à valider
     * @return Vrai si l'identifiant de l'auteur est correct, faux sinon.
     */
    boolean validateMatricule(String matriculeToCheck);

    /**
     * Permet de générer le code de contrôle d'un ISBN sur base de la langue du livre, de l'identifiant de l'auteur et du livre
     * @param numberToCheck ISBN sans code de contrôle
     * @return Le code de contrôle pour un ISBN
     */
    String getControlCode(String numberToCheck);
}
