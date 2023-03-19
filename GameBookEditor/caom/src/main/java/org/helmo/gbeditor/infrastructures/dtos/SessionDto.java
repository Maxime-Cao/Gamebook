/*
package org.helmo.gbeditor.infrastructures.dtos;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * La classe SessionDto permet de convertir une liste de livres dans un format sauvegardable
 *//*

public class SessionDto {
    private List<GameBookEditorDto> books = new ArrayList<>();

    */
/**
     * Le constructeur de la classe SessionDto permet de créer un objet SessionDto sur base d'une liste de livres
     * @param books Liste de livres sauvegardables
     *//*

    public SessionDto(List<GameBookEditorDto> books) {
        if(books != null) {
            this.books = new ArrayList<>(books);
        }
    }

    */
/**
     * Retourne la liste des livres sauvegardée
     * @return La liste des livres sauvegardée
     *//*

    public List<GameBookEditorDto> getBooks() {
        return books;
    }

    */
/**
     * Permet d'obtenir le nombre de livres présents dans la liste de livres
     * @return Le nombre de livres présents dans la liste de livres
     *//*

    public int getNumberOfBooks() {
        return books.size();
    }

}
*/
