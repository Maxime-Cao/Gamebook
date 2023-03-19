/*
package org.helmo.gbeditor.infrastructures;

import org.helmo.gbeditor.domains.CanCreateBook;
import org.helmo.gbeditor.domains.GameBookEditor;
import org.helmo.gbeditor.infrastructures.dtos.GameBookEditorDto;
import org.helmo.gbeditor.infrastructures.dtos.SessionDto;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * La classe Mapping permet de convertir des objets concrets du livre-jeu en objets DTO (sauvegardables) et inversément
 *//*

public class Mapping {
    */
/**
     * Permet d'obtenir un objet SessionDto sur base d'une liste de livres
     * @param books Liste de livres
     * @return Un objet SessionDto contenant la liste de livres
     *//*

    public SessionDto toDto(List<CanCreateBook> books) {
        if(books == null) {
            throw new IllegalArgumentException("Veuillez fournir une liste correcte de livres");
        }
        List<GameBookEditorDto> booksList = new ArrayList<>();

        for(CanCreateBook book : books) {
            if(book != null) {
                booksList.add(new GameBookEditorDto(book.getAuthorName(), book.getISBNRepresentation(), book.getTitle(), book.getResume()));
            }
        }

        return new SessionDto(booksList);
    }

    */
/**
     * Permet d'obtenir une liste de livres sur base d'un objet SessionDto récupéré
     * @param books L'objet SessionDto à convertir
     * @return Une liste de livres
     *//*

    public List<CanCreateBook> fromDto(SessionDto books) {
        if(books == null || books.getBooks() == null) {
            return new ArrayList<CanCreateBook>();
        }
        return buildListOfBooks(books);
    }

    private List<CanCreateBook> buildListOfBooks(SessionDto books) {
        List<CanCreateBook> newBooks = new ArrayList<>();
        for(GameBookEditorDto book: books.getBooks()) {
            if(book != null) {
                try {
                    addNewGameBook(book,newBooks);
                } catch (IllegalArgumentException e) {
                    // Ne rien faire, le livre n'est pas pris en compte
                }
            }
        }
        return newBooks;
    }

    private void addNewGameBook(GameBookEditorDto book,List<CanCreateBook> newBooks) {
        CanCreateBook newBook = new GameBookEditor(book.getAuthorName(),book.getIsbn(),book.getTitle(),book.getResume());
        newBooks.add(newBook);
    }
}
*/
