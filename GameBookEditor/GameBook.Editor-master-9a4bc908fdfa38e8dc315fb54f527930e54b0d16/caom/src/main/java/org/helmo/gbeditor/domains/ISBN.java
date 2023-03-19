package org.helmo.gbeditor.domains;

import java.util.Objects;

/**
 * Permet de construire le numéro ISBN d'un livre-jeu sur base du language du livre, de l'indentifant de l'auteur et du livre et d'un code de contrôle
 */
public class ISBN implements CanValidateISBN {
    private final String isbnString;

    /**
     * Permet de construire un objet ISBN sur base du language du livre, de l'indentifant de l'auteur et du livre et d'un code de contrôle
     * @param language Langue du livre
     * @param idAuthor Identifiant de l'auteur
     * @param idBook Identifiant du livre-jeu
     */
    public ISBN(String language,String idAuthor,int idBook) {

        validateISBN(language,idAuthor,idBook);

        String ISBNToControl = String.format("%s%s%02d",language,idAuthor,idBook);
        this.isbnString = String.format("%s-%s-%02d-%s",language,idAuthor,idBook,getControlCode(ISBNToControl));
    }

    /**
     * Ce constructeur par copie permet de créer une copie d'un objet ISBN existant
     * @param isbnToCopy Le numéro ISBN à copier
     */
    public ISBN(CanValidateISBN isbnToCopy) {
        if(isbnToCopy == null) {
            throw new IllegalArgumentException("Le numéro ISBN doit être dans un état cohérent et non null");
        }
        this.isbnString = isbnToCopy.getISBNRepresentation();
    }

    /**
     * Permet de construire un objet ISBN sur base d'un numéro ISBN déjà formatté
     * @param isbn Numéro ISBN déjà formatté
     */
    public ISBN(String isbn) {
        throwExceptionOnValidateStringISBN(isbn);
        int idBook = Integer.parseInt(isbn.substring(9,11));
        String language = isbn.substring(0,1);
        String idAuthor = isbn.substring(2,8);
        validateISBN(language,idAuthor,idBook);
        String isbnToControl = String.format("%s%s%02d",language,idAuthor,idBook);
        throwExceptionOnValidateControlCode(isbnToControl,isbn.substring(isbn.length() - 1));
        this.isbnString = String.format("%s-%s-%02d-%s",language,idAuthor,idBook,getControlCode(isbnToControl));
    }

    /**
     * Retourne le numéro ISBN associé à un livre-jeu, sous la forme d'une chaine de caractères
     * @return Le numéro ISBN d'un livre-jeu (String)
     */
    @Override
    public String getISBNRepresentation() {
        return isbnString;
    }

    private void validateISBN(String language,String idAuthor,int idBook) {
        throwExceptionOnValidateBlankFields(language,idAuthor);
        throwExceptionOnValidateLanguage(language);
        throwExceptionOnValidateIdAuthor(idAuthor);
        throwExceptionOnValidateIdBook(idBook);
    }

    private void throwExceptionOnValidateBlankFields(String language,String idAuthor) {
        if(language == null || idAuthor == null || language.isBlank() || idAuthor.isBlank()) {
            throw new IllegalArgumentException("Tous les champs doivent être remplis");
        }
    }

    private void throwExceptionOnValidateStringISBN(String isbn) {
        throwExceptionOnValidateLengthFormattedISBN(isbn);
        throwExceptionOnValidateTypeIdBook(isbn);
    }

    private void throwExceptionOnValidateControlCode(String isbnToControl,String givenControlCode) {
        String calculatedControlCode = getControlCode(isbnToControl);
        if(!calculatedControlCode.equals(givenControlCode)) {
            throw new IllegalArgumentException("Le code de contrôle n'est pas correct");
        }
    }

    private void throwExceptionOnValidateLengthFormattedISBN(String isbn) {
        if(isbn == null || isbn.length() != 13) {
            throw new IllegalArgumentException("Le numéro ISBN ne contient pas le bon nombre de caractères");
        }
    }

    private void throwExceptionOnValidateTypeIdBook(String isbn) {
        try {
            Integer.parseInt(isbn.substring(9,11));
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("L'identifiant du livre doit être un nombre");
        }
    }

    private void throwExceptionOnValidateLanguage(String language) {
        if(!validateLanguageNumber(language)) {
            throw new IllegalArgumentException("Le numéro du groupe linguistique doit être compris entre 0 et 4 ou égal à 7");
        }
    }

    private void throwExceptionOnValidateIdAuthor(String idAuthor) {
        if (!validateMatricule(idAuthor)) {
            throw new IllegalArgumentException("L'identifiant de l'auteur n'est pas correct");
        }
    }

    private void throwExceptionOnValidateIdBook(int idBook) {
        if(!validateIdBook(idBook)) {
            throw new IllegalArgumentException("L'identifiant du livre doit être compris entre 1 et 99");
        }
    }

    @Override
    public boolean validateLanguageNumber(String language) {
        try {
            int number = Integer.parseInt(language);
            return (number >= 0 && number < 5) || number == 7;
        } catch(NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public boolean validateIdBook(int numberToCheck) {
        try {
            return numberToCheck >= 1 && numberToCheck < 100;
        } catch(NumberFormatException ex) {
            return false;
        }
    }

    @Override
    public boolean validateMatricule(String matriculeToCheck) {
        return matriculeToCheck.matches("^[0-9]{6}$");
    }

    @Override
    public final String getControlCode(String isbnToCheck) {
        checkLengthControlCode(isbnToCheck);
        try {
            int somme = calcuteSumControlCode(isbnToCheck);
            somme = 11 - (somme % 11);
            return somme == 10 ? "X" : somme == 11 ? "0" : String.valueOf(somme);
        } catch(NumberFormatException ex) {
            throw new IllegalArgumentException("L'ISBN ne doit contenir que des chiffres");
        }
    }

    private int calcuteSumControlCode(String isbnToCheck) {
        int somme = 0;
        int poids = 10;
        for(int i = 0; i < 9; i++,poids--) {
            somme += poids * Integer.parseInt(isbnToCheck.substring(i,i+1));
        }
        return somme;
    }

    private void checkLengthControlCode(String isbnToCheck) {
        if(isbnToCheck == null || isbnToCheck.length() != 9) {
            throw new IllegalArgumentException("Le calcul du code de contrôle se fait uniquement sur un ISBN à 9 chiffres");
        }
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }

        if(!(other instanceof ISBN)) {
            return false;
        }

        ISBN that = (ISBN) other;

        return this.isbnString.equals(that.getISBNRepresentation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.isbnString);
    }
}
