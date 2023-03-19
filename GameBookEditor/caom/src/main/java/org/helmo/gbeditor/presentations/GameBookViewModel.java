package org.helmo.gbeditor.presentations;

import org.helmo.gbeditor.domains.CanCreateBook;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe GameBookViewModel permet de créer un modèle de vue pour un livre-jeu
 */
public class GameBookViewModel {
    private final CanCreateBook gBook;

    /**
     * Le constructeur de la classe GameBookViewModel permet de créer une instance de GameBookViewModel sur base d'un livre-jeu
     * @param book Le livre-jeu : instance de CanCreateBook
     */
    public GameBookViewModel(CanCreateBook book) {
        if(book != null) {
            gBook = book;
        } else {
            throw new IllegalArgumentException("Votre instance de CanCreateBook ne doit pas être nulle");
        }
    }

    /**
     * Permet de récupérer le nom de l'auteur du livre-jeu
     * @return Le nom de l'auteur du livre-jeu
     */
    public String getAuthorName() {
        return gBook.getAuthorName();
    }

    /**
     * Permet de récupérer une version courte (15 caractères maximum) du titre du livre-jeu
     * @return Version courte du titre du livre-jeu
     */
    public String getShortTitleBook() {
        String title = gBook.getTitle();
        return title.length() > 15 ? title.substring(0,15) : title;
    }

    /**
     * Permet d'obtenir le titre du livre-jeu courant
     * @return
     */
    public String getTitleBook() {
        return gBook.getTitle();
    }

    /**
     * Permet de récupérer l'ISBN (représentation) du livre-jeu courant
     * @return Représentation ISBN du livre-jeu courant
     */
    public String getIsbnBook() {
        return gBook.getISBNRepresentation();
    }

    /**
     * Permet d'obtenir la description du livre-jeu courant
     * @return
     */
    public String getResumeBook() {
        return gBook.getResume();
    }

    /**
     * Permet d'obtenir la langue du livre-jeu courant
     * @return Langue du livre-jeu courant
     */
    public String getLanguageBook() {
        return gBook.getBookLanguage();
    }

    /**
     * Permet d'obtenir l'identifiant du livre-jeu
     * @return Identifiant du livre-jeu
     */
    public int getIdBook() {
        return gBook.getIdBook();
    }

    /**
     * Permet de déterminer si le livre jeu est publié ou non (l'affichage sera différent en fonction de cela)
     * @return Etat de publication du livre-jeu
     */
    public String getPublishState() {
        return gBook.isPublishState() ? "publié" : "non publié";
    }

    /**
     * Permet de récupérer des modèles de vue pour chaque page du livre-jeu courant
     * @return Une liste de modèles de vue pour chaque page du livre-jeu courant
     */
    public List<PageViewModel> getPages() {
        List<PageViewModel> pageViewModels = new ArrayList<>();
        for(var page : gBook.getPages()) {
            pageViewModels.add(new PageViewModel(page));
        }
        return pageViewModels;
    }

    /**
     * Permet d'obtenir un modèle de vue pour une page spécifiée, sur base de son index
     * @param indexPage Indice de la page dans le livre-jeu
     * @return
     */
    public PageViewModel getBookPage(int indexPage) {
        return new PageViewModel(gBook.getBookPage(indexPage));
    }


}
