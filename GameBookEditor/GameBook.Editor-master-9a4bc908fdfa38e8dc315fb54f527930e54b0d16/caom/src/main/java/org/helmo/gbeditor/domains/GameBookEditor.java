package org.helmo.gbeditor.domains;

import org.helmo.gbeditor.domains.exceptions.PublishBookException;

import java.util.*;

/**
 * La classe GameBookEditor permet de créer un livre-jeu
 */
public class GameBookEditor implements CanCreateBook {
    private final CanCreateGameBookCover gameBookCover;
    private final List<CanCreateBookPage> pages = new ArrayList<>();
    private boolean publishState;

    /**
     * Le constructeur principal de la classe GameBookEditor permet de construire un objet GameBookEditor sur base d'un auteur, du langage du livre, de l'identifiant du livre, du titre du livre et de sa description
     * @param author L'auteur du livre
     * @param language Langage du livre
     * @param idBook Identifiant du livre-jeu
     * @param title Titre du livre-jeu
     * @param resume Description du livre-jeu
     */
    public GameBookEditor(CanCreateAuthor author,String language,int idBook,String title, String resume) {
        gameBookCover = new GameBookCover(author,language,idBook,title,resume);
        this.publishState = false;
    }

    /**
     * Le second constructeur de la classe GameBookEditor permet de construire un objet GameBookEditor sur base d'un auteur, d'un numéro ISBN déjà formatté, du titre du livre-jeu, de la description du livre-jeu et d'un état de publication du livre
     * @param author L'auteur du livre
     * @param isbn Numéro ISBN déjà formatté
     * @param title Titre du livre-jeu
     * @param resume Description du livre-jeu
     * @param publishState Etat de publication du livre
     */
    public GameBookEditor(CanCreateAuthor author,String isbn,String title,String resume,boolean publishState) {
        this.gameBookCover = new GameBookCover(author,isbn,title,resume);
        this.publishState = publishState;
    }

    /**
     * Le constructeur par copie de la classe GameBookEditor permet de construire un objet GameBookEditor sur base d'un livre-jeu existant
     * @param book Le livre-jeu à copier
     */
    public GameBookEditor(CanCreateBook book) {
        this.gameBookCover = new GameBookCover(book);
        this.pages.addAll(book.getPages());
        this.publishState = book.isPublishState();
    }

    @Override
    public CanValidateISBN getISBN() {
        return gameBookCover.getISBN();
    }

    @Override
    public String getISBNRepresentation() {
        return gameBookCover.getISBNRepresentation();
    }

    @Override
    public String getAuthorName() {
        return gameBookCover.getAuthorName();
    }

    @Override
    public String getAuthorId() {
        return gameBookCover.getAuthorId();
    }

    @Override
    public String getBookLanguage() {
        return gameBookCover.getBookLanguage();
    }

    @Override
    public List<CanCreateBookPage> getPages() {
        return pages;
    }

    @Override
    public void cleanPagesList() {
        pages.clear();
    }

    @Override
    public String getTitle() {
        return gameBookCover.getTitle();
    }

    @Override
    public String getResume() {
        return gameBookCover.getResume();
    }

    @Override
    public CanCreateAuthor getAuthor() {
        return gameBookCover.getAuthor();
    }

    @Override
    public int getIdBook() {
        return gameBookCover.getIdBook();
    }

    @Override
    public boolean isPublishState() {
        return publishState;
    }

    @Override
    public int addNewPageInBook(String textPage,int numberPage) {
        try {
            CanCreateBookPage newPage = new Page(numberPage, textPage);
            int currentMaxNumberPage = getNumberOfPages();
            if(newPage.getNumberPage() > currentMaxNumberPage) {
                newPage.setNumberPage(currentMaxNumberPage + 1);
            }
            updateOtherPages(newPage);
            pages.add(newPage);
            sortPages();
            return pages.indexOf(newPage);
        } catch(IllegalArgumentException ex) {return -1;}
    }

    private void updateOtherPages(CanCreateBookPage newPage) {
        if(pages.contains(newPage)) {
            for(int i = pages.indexOf(newPage); i < pages.size(); i++) {
                CanCreateBookPage currentPage = pages.get(i);
                pages.get(i).setNumberPage(currentPage.getNumberPage() + 1);
            }
        }
    }

    private void sortPages() {
        pages.sort((page1,page2) -> {
            return page1.getNumberPage() - page2.getNumberPage();
        });
    }

    @Override
    public int getNumberOfPages() {
        return pages.size();
    }

    @Override
    public void addNewChoiceForAPage(String content,int indexPage,int indexPageDest) {
        CanCreateBookPage[] pages = getCurrentPageAndPageDest(indexPage,indexPageDest);
        if(!pages[0].equals(pages[1])) {
            pages[0].addNewChoice(content,pages[1]);
        } else {
            throw new IllegalArgumentException("La page de destination du choix et la page courante doivent être différentes");
        }
    }

    private CanCreateBookPage[] getCurrentPageAndPageDest(int indexPage,int indexPageDest) {
        CanCreateBookPage[] pagesFound = new CanCreateBookPage[2];
        if(indexPage > -1 && indexPage <= getNumberOfPages() - 1 && indexPageDest > -1 && indexPageDest <= getNumberOfPages() - 1) {
            pagesFound[0] = pages.get(indexPage);
            pagesFound[1] = pages.get(indexPageDest);
        } else {
            throw new IllegalArgumentException("La page de destination du choix (ou courante) n'existe pas");
        }
        return pagesFound;
    }

    @Override
    public CanCreateBookPage getBookPage(int indexPage) {
        if(indexPage > -1 && indexPage <= pages.size() - 1) {
            return pages.get(indexPage);
        }
        return null;
    }

    @Override
    public boolean deleteChoiceForAPage(String content, int indexPage) {
        if(indexPage > -1 && indexPage <= getNumberOfPages() - 1) {
            CanCreateBookPage page = pages.get(indexPage);
            return page.deleteChoice(content);
        }
        return false;
    }

    @Override
    public int getNbrLinkedPages(int indexPage) {
        int linkedPages = 0;
        if(indexPage > -1 && indexPage <= getNumberOfPages() - 1) {
            for(var page : pages) {
                if(page.isLinkedToPage(indexPage)) {
                    linkedPages++;
                }
            }
        }
        return linkedPages;
    }

    @Override
    public boolean deletePage(int indexPage,boolean checkChoices) {
        if(indexPage > -1 && indexPage <= getNumberOfPages() - 1) {
            if(checkChoices) {removeChoicesLinked(indexPage);}
            pages.remove(pages.get(indexPage));
            adaptPagesOnDelete(indexPage);
            sortPages();
            return true;
        } else {
            return false;
        }
    }

    private void removeChoicesLinked(int indexPage) {
        for(var page : pages) {
            page.removeChoicesLinked(indexPage);
        }
    }

    @Override
    public void publishBook() {
        int minNumberPages = 1;
        if(getNumberOfPages() < minNumberPages) {
            throw new PublishBookException("Un livre sans page ne peut pas être publié");
        } else {
            publishState = true;
        }
    }

    @Override
    public void updateBook(String language,int idBook,String title, String resume) {
        gameBookCover.updateBookCover(language,idBook,title,resume);
    }

    private void adaptPagesOnDelete(int indexPage) {
        for(int i = indexPage; i < pages.size();i++) {
            CanCreateBookPage currentPage = pages.get(i);
            currentPage.setNumberPage(currentPage.getNumberPage() - 1);
        }
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }

        if(!(other instanceof GameBookEditor)) {
            return false;
        }

        GameBookEditor that = (GameBookEditor) other;

        return getISBN().equals(that.getISBN());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getISBN());
    }

}
