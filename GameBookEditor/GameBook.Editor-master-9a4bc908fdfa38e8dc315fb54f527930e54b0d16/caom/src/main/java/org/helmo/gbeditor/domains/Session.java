package org.helmo.gbeditor.domains;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe Session permet de représenter une session de gestion d'un livre-jeu, d'un auteur connecté
 */
public class Session implements CanCreateGameBookSession {
    private CanCreateAuthor currentAuthor;
    private final List<CanCreateBook> books = new ArrayList<>();
    private CanCreateBook currentBook;
    private int currentPageIndex;

    @Override
    public String getCurrentAuthorFullName() {
        return currentAuthor == null ? "" : String.format("%s %s",currentAuthor.getFirstname(),currentAuthor.getName());
    }

    @Override
    public void setAuthor(String firstname, String lastname,String idAuthor) {
        this.currentAuthor = new Author(firstname,lastname,idAuthor);
    }

    private boolean checkIfBookExistsInSession(CanCreateBook book) {
        for (CanCreateBook canCreateBook : books) {
            if (canCreateBook.equals(book)) {
                throw new IllegalArgumentException(String.format("Ce livre existe déjà (ISBN existant : %s)",book.getISBNRepresentation()));
            }
        }
        return false;
    }

    @Override
    public List<CanCreateBook> getBooks() {
        ArrayList<CanCreateBook> copyBooks = new ArrayList<>();
        for(CanCreateBook book : books) {
            if(book != null) {
                copyBooks.add(new GameBookEditor(book));
            }
        }
        return copyBooks;
    }

    @Override
    public boolean setGameBook(String language,int idBook,String title, String resume) {
        try {
            CanCreateBook gamebook = new GameBookEditor(currentAuthor,language,idBook,title,resume);
            if(!checkIfBookExistsInSession(gamebook)) {
                books.add(gamebook);
                sortBooks();
                this.currentBook = gamebook;
                return true;
            }
            return false;
        } catch(IllegalArgumentException ex) {
            throw ex;
        }
    }

    @Override
    public boolean setGameBook(String isbn,String title,String resume,boolean publishState) {
        try {
            CanCreateBook gamebook = new GameBookEditor(currentAuthor,isbn,title,resume,publishState);
            if(!checkIfBookExistsInSession(gamebook)) {
                books.add(gamebook);
                sortBooks();
                this.currentBook = gamebook;
                return true;
            }
            return false;
        } catch(IllegalArgumentException ex) {
            throw ex;
        }
    }

    @Override
    public boolean updateCurrentBook(String language,int idBook,String title, String resume) {
        try {
            CanCreateBook updatedBook = new GameBookEditor(currentBook);
            updatedBook.updateBook(language,idBook,title,resume);
            if(validateCurrentBookOnUpdate(currentBook,updatedBook)) {
                books.set(books.indexOf(currentBook),updatedBook);
                this.currentBook = updatedBook;
                sortBooks();
                return true;
            }
            return false;
        } catch(IllegalArgumentException ex) {
            throw ex;
        }
    }

    private boolean validateCurrentBookOnUpdate(CanCreateBook oldBook,CanCreateBook newBook) {
        if(oldBook.getISBNRepresentation().equals(newBook.getISBNRepresentation())) {
            return true;
        } else if(books.contains(newBook)) {
            throw new IllegalArgumentException(String.format("Ce livre existe déjà (ISBN existant : %s)",newBook.getISBNRepresentation()));
        } else {
            return true;
        }
    }

    @Override
    public void cleanBooksList() {
        books.clear();
    }

    @Override
    public String getIdAuthor() {
        return currentAuthor == null ? "" : currentAuthor.getIdAuthor();
    }

    private void sortBooks() {
        books.sort((book1, book2) -> {
            return book1.getISBNRepresentation().compareTo(book2.getISBNRepresentation());
        });
    }

    @Override
    public CanCreateAuthor getCurrentAuthor() {
        return new Author(currentAuthor.getFirstname(),currentAuthor.getName(),currentAuthor.getIdAuthor());
    }

    @Override
    public int getFirstIdBookAvailableInSession(String language) {
        List<CanCreateBook> booksFound = getAllBooksInSpecifiedLanguage(language);
        for(int i = 1;i<=booksFound.size();i++) {
            if(i != booksFound.get(i-1).getIdBook()) {
                return i;
            }
        }
        return booksFound.size() + 1;
    }

    @Override
    public void switchCurrentBook(int indexBook) {
        if(indexBook < 0) {
            return;
        }
        if(books.size() >= indexBook) {
            currentBook = books.get(indexBook);
        }
    }

    @Override
    public CanCreateBook getCurrentBook() {
        return currentBook;
    }

    @Override
    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    @Override
    public void updateCurrentPageNumber(int indexPage) {
        this.currentPageIndex = indexPage;
    }


    private List<CanCreateBook> getAllBooksInSpecifiedLanguage(String language) {
        List<CanCreateBook> booksFound = new ArrayList<>();
        for(CanCreateBook book : books) {
            if(book.getBookLanguage().equals(language)) {
                booksFound.add(book);
            }
        }
        return booksFound;
    }
}
