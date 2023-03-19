package org.helmo.gbeditor.domains;

/**
 * La classe GameBookCover permet de créer la couverture d'un livre-jeu
 */
public class GameBookCover implements CanCreateGameBookCover {
    private final CanCreateAuthor author;
    private CanValidateISBN isbn;
    private String title;
    private String resume;

    /**
     * Le constructeur principal de la classe GameBookCover permet de construire un objet GameBookCover sur base d'un auteur, du langage du livre, de l'identifiant du livre, du titre du livre et de sa description
     * @param author L'auteur du livre
     * @param language Langage du livre
     * @param idBook Identifiant du livre-jeu
     * @param title Titre du livre-jeu
     * @param resume Description du livre-jeu
     */
    public GameBookCover(CanCreateAuthor author,String language,int idBook,String title, String resume) {
        if(author == null) {
            throw new IllegalArgumentException("Veuillez renseigner un auteur correct pour le livre");
        }
        CanValidateISBN newIsbn = new ISBN(language,author.getIdAuthor(),idBook);

        validateCreationGameBook(title,resume);

        this.author = author;
        this.isbn = newIsbn;
        this.title = title.trim();
        this.resume = resume.trim();
    }

    /**
     * Le second constructeur de la classe GameBookCover permet de construire un objet GameBookCover sur base d'un auteur, d'un numéro ISBN déjà formatté, du titre du livre-jeu et de la description du livre-jeu
     * @param author L'auteur du livre
     * @param isbn Numéro ISBN déjà formatté
     * @param title Titre du livre-jeu
     * @param resume Description du livre-jeu
     */
    public GameBookCover(CanCreateAuthor author,String isbn,String title,String resume) {
        if(author == null) {
            throw new IllegalArgumentException("Veuillez renseigner un auteur correct pour le livre");
        }
        CanValidateISBN newIsbn = new ISBN(isbn);

        validateCreationGameBook(title,resume);

        this.author = author;
        this.isbn = newIsbn;
        this.title = title.trim();
        this.resume = resume.trim();
    }

    /**
     * Le constructeur par copie de la classe GameBookCover permet de construire un objet GameBookCover sur base d'un livre-jeu existant
     * @param book Le livre-jeu à copier
     */
    public GameBookCover(CanCreateBook book) {
        if(book == null) {
            throw new IllegalArgumentException("Le livre-jeu doit être dans un état cohérent et non null");
        }
        this.author = book.getAuthor();
        this.isbn = book.getISBN();
        this.title = book.getTitle();
        this.resume = book.getResume();
    }

    private void validateCreationGameBook(String title,String resume) {
        throwExceptionOnValidateTitle(title);
        throwExceptionOnValidateResume(resume);
    }

    private void throwExceptionOnValidateTitle(String title) {
        if(title == null || title.length() < 1 || title.length() > 150) {
            throw new IllegalArgumentException("Votre titre doit contenir au minimum 1 caractère et au maximum 150 caractères");
        }
    }

    private void throwExceptionOnValidateResume(String resume) {
        if(resume == null || resume.isBlank() || resume.length() < 1 || resume.length() > 500) {
            throw new IllegalArgumentException("Votre résumé doit contenir au moins 1 caractère et au maximum 500 caractères");
        }
    }

    @Override
    public CanValidateISBN getISBN() {
        return new ISBN(isbn);
    }

    @Override
    public String getISBNRepresentation() {
        return isbn.getISBNRepresentation();
    }

    @Override
    public String getAuthorName() {
        return String.format("%s %s",author.getFirstname(),author.getName());
    }

    @Override
    public String getAuthorId() {
        return author.getIdAuthor();
    }

    @Override
    public String getBookLanguage() {
        String isbnRep = getISBNRepresentation();
        return isbnRep.substring(0,1);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getResume() {
        return resume;
    }

    @Override
    public CanCreateAuthor getAuthor() {
        return new Author(author.getFirstname(),author.getName(),author.getIdAuthor());
    }

    @Override
    public int getIdBook() {
        String isbnRep = getISBNRepresentation();
        return Integer.parseInt(isbnRep.substring(9,11));
    }

    @Override
    public void updateBookCover(String language, int idBook, String title, String resume) {
        CanValidateISBN newIsbn = new ISBN(language,author.getIdAuthor(),idBook);

        validateCreationGameBook(title,resume);

        this.isbn = newIsbn;
        this.title = title.trim();
        this.resume = resume.trim();
    }
}
