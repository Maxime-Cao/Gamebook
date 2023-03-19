package org.helmo.gbeditor.domains;

import java.util.Locale;

/**
 * La Classe Author permet de représenter l'auteur d'un livre jeu.
 * Il dispose d'un nom, prénom et d'un identifiant
 */
public class Author implements CanCreateAuthor {
    private final String name;
    private final String firstname;
    private final String idAuthor;

    /**
     * Le constructeur de la classe Author permet de constuire un objet Author sur base d'un prénom, nom et d'un identifiant
     * @param firstname Le prénom de l'auteur
     * @param name Le nom de l'auteur
     * @param idAuthor L'identifiant de l'auteur
     */
    public Author(String firstname,String name,String idAuthor) {
        throwExceptionOnValidateNames(firstname,name);
        throwExceptionOnValidateIdAuthor(idAuthor);

        String formattedFirstname = firstname.substring(0,1).toUpperCase(Locale.ROOT) + firstname.substring(1).toLowerCase(Locale.ROOT);
        String formattedName = name.substring(0,1).toUpperCase(Locale.ROOT) + name.substring(1).toLowerCase(Locale.ROOT);

        this.idAuthor = idAuthor;
        this.firstname = formattedFirstname.trim();
        this.name = formattedName.trim();
    }

    private void throwExceptionOnValidateNames(String firstname,String name) {
        if(firstname == null || name == null || !firstname.matches("^[a-zA-Z- \\\\.]+$") || !name.matches("^[a-zA-Z- \\\\.]+$")) {
            throw new IllegalArgumentException("Veuillez fournir un prénom et nom correct pour l'utilisateur");
        }
    }

    private void throwExceptionOnValidateIdAuthor(String idAuthor) {
        if(idAuthor == null || !idAuthor.matches("^[0-9]{6}$")) {
            throw new IllegalArgumentException("L'identifiant de l'auteur doit contenir 6 chiffres");
        }
    }

    @Override
    public String getFirstname() {
        return firstname;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIdAuthor() {
        return idAuthor;
    }
}
