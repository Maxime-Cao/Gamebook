package org.helmo.gbeditor.infrastructures.jdbc;

import org.helmo.gbeditor.domains.*;
import org.helmo.gbeditor.repositories.*;
import org.helmo.gbeditor.repositories.exceptions.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * La classe SqlProjectStorage implémente l'interface IRepository et AutoCloseable, ce qui permet la connexion et l'envoi de requêtes vers une base de données.
 * Elle reprend toutes les requêtes permettant de charger et sauvegarder des livres
 */
public class SqlProjectStorage implements AutoCloseable, IRepository {
    private final Connection connection;
    private final UpdateHandler updateHandler;
    private final String authorNotFound = "L'auteur du livre n'a pas pu être trouvé";
    private final String bookNotFound = "Le livre n'a pas pu être trouvé";

    /**
     * Le constructeur de la classe SqlProjectStorage permet de construire une instance de SqlProjectStorage sur base d'un objet Connection et d'un UpdateHandler
     * @param con Objet Connection, responsable de la connexion à la BD
     * @param updateHandler Objet UpdateHandler
     */
    public SqlProjectStorage(Connection con,UpdateHandler updateHandler) {
        this.connection = con;
        this.updateHandler = updateHandler;
    }

    @Override
    public void close() throws Exception {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new DeconnectionFailedException("Impossible de se déconnecter de la base de données",ex);
        }
    }

    //////////////////////// Modification du contenu de la BD (insert,update,delete) ////////////////////////////
    @Override
    public void addAuthor(String idAuthor,String firstname,String lastname) {
        try {
            checkIfAuthorExistsInDB(idAuthor,firstname,lastname);
        } catch(AuthorNotFoundException ex) {
            try(PreparedStatement query = connection.prepareStatement("INSERT INTO gb_author VALUES(?,?,?)")) {
                query.setString(1,idAuthor);
                query.setString(2,lastname);
                query.setString(3,firstname);

                query.executeUpdate();
            } catch (SQLException e) {
                throw new AuthorNotAddedException("L'auteur n'a pas pu être sauvegardé",e);
            }
        }
    }
    private void checkIfAuthorExistsInDB(String idAuthor,String firstname,String name) {
        try(PreparedStatement query = connection.prepareStatement("SELECT * FROM gb_author WHERE idAuthor = ?")) {
            query.setString(1,idAuthor);
            try(ResultSet result = query.executeQuery()) {
                onAuthorFoundInDB(result,firstname,name);
            }
        } catch (SQLException ex) {
            throw new AuthorNotFoundException(authorNotFound,ex);
        }
    }

    private void onAuthorFoundInDB(ResultSet result,String firstname,String name) throws SQLException {
        if(!result.next()) {
            throw new AuthorNotFoundException(authorNotFound);
        } else if(!firstname.equals(result.getString("firstname")) || !name.equals(result.getString("name"))) {
            throw new BadIdentifiersForUserException("Veuillez vérifier les informations renseignées pour l'auteur spécifié");
        }
    }

    @Override
    public void saveABookWithoutPage(CanCreateBook book) {
        if(book != null) {
            try {
                checkIfBookExistsInDB(book);
            } catch(BookNotFoundException ex) {
                addBook(book);
            }
        }
    }

    private void checkIfBookExistsInDB(CanCreateBook book) {
        try(PreparedStatement query = connection.prepareStatement("SELECT * FROM gb_book WHERE isbn = ?")) {
            query.setString(1,book.getISBNRepresentation());
            try(ResultSet result = query.executeQuery()) {
                if(!result.next()) {
                    throw new BookNotFoundException(bookNotFound);
                }
            }
        } catch (SQLException ex) {
            throw new BookNotFoundException(bookNotFound,ex);
        }
    }

    private void addBook(CanCreateBook book) {
        try (PreparedStatement query = connection.prepareStatement("INSERT INTO gb_book(isbn,title,resume,publishState,idAuthor) VALUES(?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
            query.setString(1, book.getISBNRepresentation());
            query.setString(2, book.getTitle());
            query.setString(3, book.getResume());
            query.setBoolean(4, false);
            query.setString(5, book.getAuthorId());
            query.executeUpdate();
            try(ResultSet generatedKeys = query.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    updateHandler.addBook(generatedKeys.getInt(1), book.getISBNRepresentation());
                }
            }
        } catch (SQLException ex) {
            throw new BookNotAddedException("Le livre n'a pas pu être sauvegardé");
        }
    }

    @Override
    public void saveBooks(List<CanCreateBook> books) throws IOException { }

    @Override
    public void saveABookWithPagesAndChoices(CanCreateBook book) {
        if(book == null) {
            throw new IllegalArgumentException("Veuillez fournir un livre correct");
        }
        onUpdateBook(book);
    }

    private void onUpdateBook(CanCreateBook book) {
        Transaction
                .from(connection)
                .commit((con) -> saveBook(book))
                .onRollback((ex->{throw new UnableToSaveException(ex);}))
                .execute();
    }

    private void saveBook(CanCreateBook book) throws SQLException {
        int bookId = updateHandler.getCurrentEditBookId();
        if(bookId != -1 && book != null) {
            updateBook(bookId,book.getISBNRepresentation(), book.getTitle(), book.getResume(), book.isPublishState());
            savePagesAndChoices(book.getPages(),bookId);
        }
    }

    private void updateBook(int idBook,String newIsbn,String title,String resume,boolean publishState) throws SQLException {
        try(PreparedStatement query = connection.prepareStatement("UPDATE gb_book SET isbn = ?, title = ?, resume = ?, publishState = ? WHERE idBook = ?")) {
            query.setString(1,newIsbn);
            query.setString(2,title);
            query.setString(3,resume);
            query.setBoolean(4,publishState);
            query.setInt(5,idBook);

            query.executeUpdate();
            updateHandler.addBook(idBook,newIsbn);
        }
    }

    private void savePagesAndChoices(List<CanCreateBookPage> pagesInSession,int idBook) throws SQLException {
        savePages(pagesInSession,idBook);
        saveChoices(pagesInSession);
    }

    private void savePages(List<CanCreateBookPage> pagesInSession,int idBook) throws SQLException {
        List<CanCreateBookPage> pagesInDB = updateHandler.getPages();
        updateDbPages(pagesInSession,pagesInDB,idBook);
        removeDbPages(pagesInSession,pagesInDB);
    }

    private void updateDbPages(List<CanCreateBookPage> pagesInSession,List<CanCreateBookPage> pagesInDB,int idBook) throws SQLException {
        for(var pageInSession : pagesInSession) {
            if(pagesInDB.contains(pageInSession)) {
                updatePageForABook(pageInSession,updateHandler.getIdPage(pageInSession));
            } else {
                addPageInABook(pageInSession,idBook);
            }
        }
    }

    private void removeDbPages(List<CanCreateBookPage> pagesInSession,List<CanCreateBookPage> pagesInDB) throws SQLException {
        int idPage;
        for(var pageInDB : pagesInDB) {
            if(!pagesInSession.contains(pageInDB)) {
                idPage = updateHandler.getIdPage(pageInDB);
                deletePageInABook(idPage);
                updateHandler.removePage(idPage);
            }
        }
    }

    private void updatePageForABook(CanCreateBookPage page,int idPage) throws SQLException {
        try(PreparedStatement query = connection.prepareStatement("UPDATE gb_page SET textPage = ?, numberPage = ? WHERE idPage = ?")) {
            query.setString(1,page.getContent());
            query.setInt(2,page.getNumberPage());
            query.setInt(3,idPage);

            query.executeUpdate();
        }
    }

    private void addPageInABook(CanCreateBookPage page,int idBook) throws SQLException {
        try(PreparedStatement query = connection.prepareStatement("INSERT INTO gb_page (numberPage,textPage,idBook) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
            query.setInt(1,page.getNumberPage());
            query.setString(2,page.getContent());
            query.setInt(3,idBook);

            query.executeUpdate();

            try(ResultSet generatedKeys = query.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    updateHandler.addPageWithoutChoice(generatedKeys.getInt(1), page);
                }
            }
        }
    }

    private void deletePageInABook(int idPage) throws SQLException {
        try(PreparedStatement query = connection.prepareStatement("DELETE FROM gb_page WHERE idPage = ?")) {
            query.setInt(1,idPage);

            query.executeUpdate();
        }
    }

    private void saveChoices(List<CanCreateBookPage> pagesInSession) throws SQLException {
        List<CanCreateBookPage> pagesInDB = updateHandler.getPages();
        CanCreateBookPage currentPageDB;
        for(var pageInSession : pagesInSession) {
            currentPageDB = updateHandler.getPage(pageInSession);
            saveChoicesForAPage(updateHandler.getIdPage(currentPageDB),pageInSession.getChoices(),currentPageDB.getChoices());
            onChangeInPages(pagesInDB,pageInSession);
        }
    }

    private void saveChoicesForAPage(int idPage,Map<String,CanCreateBookPage> choicesInSession,Map<String,CanCreateBookPage> choicesInDB) throws SQLException {
        for(var choiceInSession : choicesInSession.entrySet()) {
            String textChoice = choiceInSession.getKey();
            int idPageDest = updateHandler.getIdPage(choiceInSession.getValue());
            if(choicesInDB.containsKey(choiceInSession.getKey())) {
                updateChoice(textChoice,idPage,idPageDest);
            } else {
                addChoice(textChoice,idPage,idPageDest);
            }
        }

        for(var choiceInBD : choicesInDB.entrySet()) {
            if(!choicesInSession.containsKey(choiceInBD.getKey())) {
                removeChoice(choiceInBD.getKey(),idPage);
            }
        }
    }

    private void updateChoice(String textChoice,int idPage,int idPageDest) throws SQLException {
        try(PreparedStatement query = connection.prepareStatement("UPDATE gb_choice SET idPageDest = ? WHERE idPage = ? AND textChoice = ?")) {
            query.setInt(1,idPageDest);
            query.setInt(2,idPage);
            query.setString(3,textChoice);

            query.executeUpdate();
        }
    }

    private void addChoice(String textChoice,int idPage,int idPageDest) throws SQLException {
        try(PreparedStatement query = connection.prepareStatement("INSERT INTO gb_choice(textChoice,idPage,idPageDest) VALUES (?,?,?)")) {
            query.setString(1,textChoice);
            query.setInt(2,idPage);
            query.setInt(3,idPageDest);

            query.executeUpdate();
        }
    }

    private void removeChoice(String textchoice, int idPage) throws SQLException {
        try(PreparedStatement query = connection.prepareStatement("DELETE FROM gb_choice WHERE idPage = ? AND textChoice = ?")) {
            query.setInt(1,idPage);
            query.setString(2,textchoice);

            query.executeUpdate();
        }
    }

    private void onChangeInPages(List<CanCreateBookPage> pagesInDB, CanCreateBookPage currentPageSession) {
        if(pagesInDB.contains(currentPageSession)){
            updateHandler.addPage(updateHandler.getIdPage(currentPageSession),currentPageSession);
        }
    }

    ///////////////////////////////////// Chargement du contenu de la BD /////////////////////////////////////

    @Override
    public List<CanCreateBook> loadAllBooks() throws Exception {
        List<CanCreateBook> books = new ArrayList<>();
        updateHandler.clearBooksMap();
        try(PreparedStatement query = connection.prepareStatement("SELECT idBook,isbn,title,resume,publishState,idAuthor FROM gb_book")) {
            try(ResultSet result = query.executeQuery()) {
                onAllBooksFound(books,result);
                return books;
            }
        } catch (SQLException e) {
            throw new BookNotFoundException(bookNotFound,e);
        }
    }

    private void onAllBooksFound(List<CanCreateBook> books,ResultSet result) throws SQLException {
        while(result.next()) {
            try {
                CanCreateAuthor author = getAuthorWithId(result.getString("idAuthor"));
                books.add(new GameBookEditor(author, result.getString("isbn"), result.getString("title"), result.getString("resume"),result.getBoolean("publishState")));
                updateHandler.addBook(result.getInt("idBook"),result.getString("isbn"));
            } catch(IllegalArgumentException | AuthorNotFoundException ex) {
                // Le livre n'est pas repris car il est n'est pas conforme selon l'objet métier GameBookEditor
                continue;
            }
        }
    }

    @Override
    public List<CanCreateBook> loadAllBooksWithIdAuthor(String idAuthor) {
        List<CanCreateBook> books = new ArrayList<>();
        updateHandler.clearBooksMap();
        CanCreateAuthor author = onGetAuthorWithId(idAuthor);
        if(author == null) {return books;}
        try(PreparedStatement query = connection.prepareStatement("SELECT idBook,isbn,title,resume,publishState FROM gb_book WHERE idAuthor = ?")) {
            query.setString(1,idAuthor);
            try(ResultSet result = query.executeQuery()) {
                onBooksFoundForAuthor(author,books,result);
                return books;
            }
        } catch (SQLException e) {
            throw new BookNotFoundException(bookNotFound,e);
        }
    }

    private CanCreateAuthor onGetAuthorWithId(String idAuthor) {
        CanCreateAuthor author;
        try {
            author = getAuthorWithId(idAuthor);
        } catch(AuthorNotFoundException ex) {
            return null;
        }
        return author;
    }

    private void onBooksFoundForAuthor(CanCreateAuthor author,List<CanCreateBook> books,ResultSet result) throws SQLException {
        while(result.next()) {
            try {
                String isbn = result.getString("isbn");
                books.add(new GameBookEditor(author,isbn, result.getString("title"), result.getString("resume"),result.getBoolean("publishState")));
                updateHandler.addBook(result.getInt("idBook"),isbn);
            } catch(IllegalArgumentException ex) {
                // Le livre n'est pas repris car il n'est pas conforme selon mon objet métier GameBookEditor
                continue;
            }
        }
    }

    @Override
    public List<CanCreateBookPage> loadPagesAndChoicesForABook(String isbn) {
        int bookId = updateHandler.getBookIdWithIsbn(isbn);
        List<CanCreateBookPage> pages = new ArrayList<>();
        updateHandler.clearPagesMap();

        if(bookId != -1) {
            try {
                loadPagesForABook(bookId);
                pages = updateHandler.getPages();
                loadChoicesForPages(updateHandler.getMapPages());
            } catch (PageNotFoundException ex) {
                return pages;
            }
        }
        return pages;
    }


    private void loadPagesForABook(int idBook) {
        try(PreparedStatement query = connection.prepareStatement("SELECT idPage,numberPage,textPage FROM gb_page WHERE idBook = ?")) {
            query.setInt(1,idBook);
            try(ResultSet result = query.executeQuery()) {
                while(result.next()) {
                    try {
                        CanCreateBookPage page = new Page(result.getInt("numberPage"), result.getString("textPage"));
                        updateHandler.addPage(result.getInt("idPage"),page);
                    } catch(IllegalArgumentException ex) {
                        // La page n'est pas reprise car elle n'est pas conforme (erreur durant la création de l'objet Page)
                        continue;
                    }
                }
            }
        } catch (SQLException ex) {
            throw new PageNotFoundException("Aucune page n'a été trouvé pour le livre spécifié");
        }
    }

    private void loadChoicesForPages(Map<Integer,CanCreateBookPage> pages) {
        for(var page : pages.entrySet()) {
            try {
                getChoicesForAPage(page.getKey(), page.getValue());
            } catch(ChoiceNotFoundException ex) {
                // Aucun choix n'a été trouvé pour la page (ce qui est possible), je ne laisse pas l'exception remontée et je passe à la page suivante
               continue;
            }
        }
    }

    private void getChoicesForAPage(int idPage,CanCreateBookPage page) {
        try(PreparedStatement query = connection.prepareStatement("SELECT textChoice,idPageDest FROM gb_choice WHERE idPage = ?")) {
            query.setInt(1,idPage);
            try(ResultSet result = query.executeQuery()) {
                while(result.next()) {
                    try {
                        CanCreateBookPage pageDest = updateHandler.getPageWithId(result.getInt("idPageDest"));
                        if(pageDest != null) {
                            page.addNewChoice(result.getString("textChoice"), pageDest);
                        }
                    } catch(IllegalArgumentException e) {
                        // Le choix ne sera pas ajouté à la page car sa page de destination n'existe pas ou son texte n'est pas conforme
                        continue;
                    }
                }
            }
        } catch (SQLException ex) {
            throw new ChoiceNotFoundException("Aucun choix n'a été trouvé pour cette page",ex);
        }
    }

    private CanCreateAuthor getAuthorWithId(String idAuthor) {
        CanCreateAuthor author = null;
        try(PreparedStatement query = connection.prepareStatement("SELECT name,firstname FROM gb_author WHERE idAuthor = ?")) {
            query.setString(1,idAuthor);
            try(ResultSet result = query.executeQuery()) {
                if(result.next()) {
                    author = new Author(result.getString("firstname"),result.getString("name"),idAuthor);
                } else {
                    throw new AuthorNotFoundException(authorNotFound);
                }
            }
        } catch (SQLException e) {
            throw new AuthorNotFoundException(authorNotFound,e);
        }
        return author;
    }
}