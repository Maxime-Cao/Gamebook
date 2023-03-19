/*
package org.helmo.gbeditor.infrastructures;

import org.assertj.core.util.VisibleForTesting;
import org.helmo.gbeditor.domains.CanCreateBook;
import org.helmo.gbeditor.domains.GameBookEditor;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

*/
/**
 * La classe JsonRepositoryTest permet de tester les méthodes de la classe JsonRepository
 *//*

@VisibleForTesting
class JsonRepositoryTest {
    private static final String PATH_RESOURCES = Paths.get("","src","test","resources").toString();

    private static final String FILENAME = "d170051.json";
    private static final String FIRST_ISBN = "2-170051-01-5";
    private static final String AUTHOR_NAME = "Maxime Cao";
    private static final String FIRST_TITLE = "Harry Potter 1";
    private static final String FIRST_RESUME = "Ceci est la description d'Harry Potter 1";

    private static final String SECOND_ISBN = "2-170051-02-3";
    private static final String SECOND_TITLE = "Harry Potter 2";

    private static final String THIRD_ISBN = "2-170051-03-1";
    private static final String THIRD_TITLE = "Harry Potter 3";
    private static final String THIRD_RESUME = "Ceci est la description d'Harry Potter 3";

    private static final String LONG_RESUME = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus pharetra elit eget auctor efficitur. Quisque finibus arcu eu tortor eleifend, cursus eleifend erat ornare. Ut vel gravida mi, id sollicitudin dui. Sed luctus turpis est, et dapibus nunc pharetra vitae. Donec ut urna viverra mi condimentum volutpat id sit amet nibh. Cras cursus, metus in consectetur vehicula, dolor nulla tristique odio, ut dignissim nisl purus sed felis. Duis lectus.";

    */
/**
     * La méthode deleteFileAndFolder permet de supprimer le fichier sur base de son chemin, si celui-ci existe. Ainsi que supprimer le dossier contenant ce fichier
     * @param filePath Chemin du fichier à supprimer
     *//*

    private void deleteFileAndFolder(Path filePath) {
        if(Files.exists(filePath)) {
            try {
                Files.delete(filePath);
                Files.delete(filePath.getParent());
            } catch (IOException e) {
                fail("Erreur durant la suppression du fichier et dossier de test");
            }
        }
    }

    */
/**
     * La méthode cleanFile permet de supprimer le contenu d'un fichier sur base de son chemin, si celui-ci existe
     * @param path Le fichier dont le contenu doit être supprimé
     *//*

    private void cleanFile(Path path) {
        if(Files.exists(path)) {
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING)) {
            } catch (IOException e) {
                fail("Erreur pendant la suppression des éléments du fichier");
            }
        }
    }

    @Test
    @VisibleForTesting
    void createRepositoryWithNullPath() {
        // Dans ce test, on tente de créer un objet JsonRepository avec un Path "null"
        try {
            new JsonRepository(null);
            fail("La création d'un objet JsonRepository avec un chemin 'null' devrait déclencher une exception");
        } catch(Exception e) {
            assertEquals("Le chemin du fichier ne doit pas être nul et doit correspondre à un fichier .json",e.getMessage());
        }
    }

    @Test
    @VisibleForTesting
    void createRepositoryWithNotAFilePath() {
        // Dans ce test, on tente de créer un objet JsonRepository avec un Path qui n'est pas un chemin vers un fichier .json
        try {
            new JsonRepository(Paths.get(PATH_RESOURCES));
            fail("La création d'un objet JsonRepository avec un chemin qui n'est pas celui d'un fichier Json devrait déclencher une exception");
        } catch(Exception e) {
            assertEquals("Le chemin du fichier ne doit pas être nul et doit correspondre à un fichier .json",e.getMessage());
        }
    }

    @Test
    @VisibleForTesting
    void saveCorrectListOfBooks() {
        // Dans ce test, on sauvegarde une liste correcte de livres dans un fichier et un dossier qui existent déjà
        Path filePath = Paths.get(PATH_RESOURCES,"saveBooksOkWithFileAndFolderExist",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> booksList = new ArrayList<>();
            booksList.add(new GameBookEditor(AUTHOR_NAME,FIRST_ISBN,FIRST_TITLE,FIRST_RESUME));
            booksList.add(new GameBookEditor(AUTHOR_NAME,SECOND_ISBN,SECOND_TITLE,LONG_RESUME));
            booksList.add(new GameBookEditor(AUTHOR_NAME,THIRD_ISBN,THIRD_TITLE,THIRD_RESUME));

            assertTrue(Files.exists(filePath));
            assertEquals(0, new File(filePath.toString()).length());

            repository.saveBooks(booksList);

            assertTrue(new File(filePath.toString()).length() > 0);
        } catch(Exception e) {
            fail("La méthode save ne doit pas déclencher d'exception car la liste de livres à sauvegarder est correcte et le fichier existe et est accessible");
        } finally {
            cleanFile(filePath);
        }
    }
    @Test
    @VisibleForTesting
    void saveBooksWithFileAndFolderDoNotExist() {
        // Ce test prouve que le dossier et fichier sont créés grâce à la méthode save s'ils n'existent pas
        Path filePath = Paths.get(PATH_RESOURCES,"saveBooksOkWithFileAndFolderDoNotExist",FILENAME).toAbsolutePath();
        try {
            Path folderPath = filePath.getParent();
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> booksList = new ArrayList<>();
            booksList.add(new GameBookEditor(AUTHOR_NAME,FIRST_ISBN,FIRST_TITLE,FIRST_RESUME));
            booksList.add(new GameBookEditor(AUTHOR_NAME,SECOND_ISBN,SECOND_TITLE,LONG_RESUME));
            booksList.add(new GameBookEditor(AUTHOR_NAME,THIRD_ISBN,THIRD_TITLE,THIRD_RESUME));

            assertTrue(Files.notExists(filePath));
            assertTrue(Files.notExists(folderPath));

            repository.saveBooks(booksList);

            assertTrue(Files.exists(filePath));
            assertTrue(Files.exists(folderPath));
            assertTrue(new File(filePath.toString()).length() > 0);

            deleteFileAndFolder(filePath);
        } catch(Exception e) {
            fail("La méthode save ne doit pas déclencher d'exception car la liste de livres à sauvegarder est correcte et le fichier et dossier seront créés par la méthode save");
        } finally {
            deleteFileAndFolder(filePath);
        }
    }

    @Test
    @VisibleForTesting
    void saveBooksWithNullListOfBooks()
    {
        // Dans ce test, on essaye de sauvegarder une liste de livres "null" dans un fichier
        Path filePath = Paths.get(PATH_RESOURCES,"saveBooksNotOkWithNullListOfBooks",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);

            repository.saveBooks(null);

            fail("La méthode save doit déclencher une exception quand la liste de livres à sauvegarder est égale à null");
        } catch(Exception e) {
            assertTrue(Files.notExists(filePath));
            assertEquals("Veuillez fournir une liste correcte de livres",e.getMessage());
        } finally {
            deleteFileAndFolder(filePath);
        }
    }

    @Test
    @VisibleForTesting
    void saveBooksWithOnlyNullBooksInList() {
        // Dans ce test, on essaye de sauvegarder une liste de livres qui ne contient que des livres "null", dans un fichier
        Path filePath = Paths.get(PATH_RESOURCES,"saveBooksOkWithOnlyNullBooksInList",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> booksList = new ArrayList<>();
            booksList.add(null);
            booksList.add(null);

            repository.saveBooks(booksList);

            assertTrue(Files.exists(filePath));
            assertEquals(0,new File(filePath.toString()).length());
        } catch(Exception e) {
            fail("La méthode save ne doit pas déclencher d'exception si la liste de livres contient des éléments null, ils ne doivent pas être écrits dans le fichier");
        } finally {
            deleteFileAndFolder(filePath);
        }
    }
    @Test
    @VisibleForTesting
    void saveBooksWithNullBookAndCorrectBook() {
        // Dans ce test, on essaye de sauvegarder une liste de livres qui contient un livre-jeu correct et une référence "null"
        Path filePath = Paths.get(PATH_RESOURCES,"saveBooksOkWithNullAndCorrectBook",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> booksList = new ArrayList<>();
            booksList.add(null);
            booksList.add(new GameBookEditor(AUTHOR_NAME,FIRST_ISBN,FIRST_TITLE,FIRST_RESUME));

            repository.saveBooks(booksList);

            assertTrue(new File(filePath.toString()).length() > 0);
        } catch(Exception e) {
            fail("La méthode save ne doit pas déclencher d'exception lorsqu'on souhaite sauvegarder une liste des livres qui contient des livres corrects et null, les livres null ne seront pas repris dans le fichier");
        } finally {
            deleteFileAndFolder(filePath);
        }
    }

    @Test
    @VisibleForTesting
    void loadCorrectListOfBooks() {
        Path filePath = Paths.get(PATH_RESOURCES,"loadBooksOkWithCorrectListOfBooks",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> collectedBooks =  repository.loadAllBooks();

            assertNotNull(collectedBooks);
            assertEquals(2,collectedBooks.size());

            CanCreateBook firstBook = collectedBooks.get(0);
            CanCreateBook secondBook = collectedBooks.get(1);

            assertEquals(AUTHOR_NAME,firstBook.getAuthorName());
            assertEquals(FIRST_ISBN,firstBook.getISBNRepresentation());
            assertEquals(FIRST_TITLE,firstBook.getTitle());
            assertEquals(FIRST_RESUME,firstBook.getResume());

            assertEquals(AUTHOR_NAME,secondBook.getAuthorName());
            assertEquals( SECOND_ISBN,secondBook.getISBNRepresentation());
            assertEquals(SECOND_TITLE,secondBook.getTitle());
            assertEquals("Résumé d'Harry Potter 2",secondBook.getResume());

        } catch(Exception e) {
            fail("La méthode load ne devrait pas déclencher d'exception car le fichier est accessible et tous les livres sont dans un format correct");
        }
    }

    @Test
    @VisibleForTesting
    void loadBooksWithErrorSyntaxInFile() {
        Path filePath = Paths.get(PATH_RESOURCES,"loadBooksNotOkWithErrorSyntaxInFile",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            repository.loadAllBooks();
            fail("La méthode load devrait déclencher une exception car il y a une erreur de syntaxe dans le fichier contenant les livres");
        } catch(Exception e) {
            assertEquals("Erreur durant la lecture du fichier JSON. Veuillez vérifier la syntaxe des éléments du fichier",e.getMessage());
        }
    }

    @Test
    @VisibleForTesting
    void loadBooksWithMissingFieldInABook() {
        Path filePath = Paths.get(PATH_RESOURCES,"loadBooksOkWithMissingFieldInABook",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> collectedBooks = repository.loadAllBooks();

            assertNotNull(collectedBooks);
            assertEquals(0,collectedBooks.size());
        } catch(Exception e) {
            fail("La méthode load ne devrait pas déclencher d'exception lorsqu'un livre dans le fichier a un attribut manquant. Le livre ne devrait juste pas être repris dans la liste retournée par la méthode load");
        }
    }

    @Test
    @VisibleForTesting
    void loadBooksWithBadTypeForAFieldInABook() {
        Path filePath = Paths.get(PATH_RESOURCES,"loadBooksOkWithBadTypeForAFieldInABook",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> collectedBooks = repository.loadAllBooks();

            assertNotNull(collectedBooks);
            assertEquals(1,collectedBooks.size());

            CanCreateBook collectedBook = collectedBooks.get(0);

            assertEquals(AUTHOR_NAME,collectedBook.getAuthorName());
            assertEquals(FIRST_ISBN,collectedBook.getISBNRepresentation());
            assertEquals("123",collectedBook.getTitle());
            assertEquals(FIRST_RESUME,collectedBook.getResume());
        } catch(Exception e) {
            fail("La méthode load ne devrait pas déclencher d'exception lorsqu'un livre dans le fichier a un attribut d'un mauvais type.Le livre ne devrait juste pas être repris dans la liste retournée par la méthode load, sauf si sa conversion automatique en string est valide comme valeur pour l'attribut concerné");
        }
    }

    @Test
    @VisibleForTesting
    void loadBooksOkWithInvalidTitleInABook() {
        Path filePath = Paths.get(PATH_RESOURCES,"loadBooksOkWithInvalidTitleInABook",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> collectedBooks = repository.loadAllBooks();

            assertNotNull(collectedBooks);
            assertEquals(0,collectedBooks.size());
        } catch(Exception e) {
            fail("La méthode load ne devrait pas déclencher d'exception lorsqu'un livre dans le fichier a un titre invalide.Le livre ne devrait juste pas être repris dans la liste retournée par la méthode load");
        }
    }

    @Test
    @VisibleForTesting
    void loadBooksOkWithInvalidIsbnInABook() {
        Path filePath = Paths.get(PATH_RESOURCES,"loadBooksOkWithInvalidIsbnInABook",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> collectedBooks = repository.loadAllBooks();

            assertNotNull(collectedBooks);
            assertEquals(0,collectedBooks.size());
        } catch(Exception e) {
            fail("La méthode load ne devrait pas déclencher d'exception lorsqu'un livre dans le fichier a un isbn invalide.Le livre ne devrait juste pas être repris dans la liste retournée par la méthode load");
        }
    }

    @Test
    @VisibleForTesting
    void loadBooksOkWithInvalidAuthorNameInABook() {
        Path filePath = Paths.get(PATH_RESOURCES,"loadBooksOkWithInvalidAuthorNameInABook",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> collectedBooks = repository.loadAllBooks();

            assertNotNull(collectedBooks);
            assertEquals(0,collectedBooks.size());
        } catch(Exception e) {
            fail("La méthode load ne devrait pas déclencher d'exception lorsqu'un livre dans le fichier a un auteur invalide.Le livre ne devrait juste pas être repris dans la liste retournée par la méthode load");
        }
    }

    @Test
    @VisibleForTesting
    void loadBooksOkWithInvalidResumeInABook() {
        Path filePath = Paths.get(PATH_RESOURCES,"loadBooksOkWithInvalidResumeInABook",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> collectedBooks = repository.loadAllBooks();

            assertNotNull(collectedBooks);
            assertEquals(0,collectedBooks.size());
        } catch(Exception e) {
            fail("La méthode load ne devrait pas déclencher d'exception lorsqu'un livre dans le fichier a un résumé invalide.Le livre ne devrait juste pas être repris dans la liste retournée par la méthode load");
        }
    }

    @Test
    @VisibleForTesting
    void loadBooksOkWithFileDoesNotExist() {
        Path filePath = Paths.get(PATH_RESOURCES,"loadBooksOkWithFileDoesNotExist",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> collectedBooks = repository.loadAllBooks();

            assertNotNull(collectedBooks);
            assertEquals(0,collectedBooks.size());
        } catch(Exception e) {
            fail("La méthode load ne devrait pas déclencher d'exception lorsque le fichier n'existe pas. Une liste vide devrait être retournée");
        }
    }

    @Test
    @VisibleForTesting
    void loadBooksOkWithExtraFieldsInABook() {
        Path filePath = Paths.get(PATH_RESOURCES,"loadBooksOkWithExtraFieldsInABook",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> collectedBooks = repository.loadAllBooks();

            assertNotNull(collectedBooks);

            CanCreateBook book = collectedBooks.get(0);

            assertNotNull(book);
            assertEquals(AUTHOR_NAME,book.getAuthorName());
            assertEquals(FIRST_ISBN,book.getISBNRepresentation());
            assertEquals(FIRST_TITLE,book.getTitle());
            assertEquals(FIRST_RESUME,book.getResume());

        } catch(Exception e) {
            fail("La méthode load ne devrait pas déclencher d'exception quand un livre dans le fichier contient un ou plusieurs champs en trop");
        }
    }
    @Test
    @VisibleForTesting
    void loadBooksWithEmptyFile() {
        Path filePath = Paths.get(PATH_RESOURCES,"loadBooksWithEmptyFile",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> collectedBooks = repository.loadAllBooks();

            assertNotNull(collectedBooks);
            assertEquals(0,collectedBooks.size());
        } catch(Exception e) {
            fail("La méthode load ne devrait pas déclencher d'exception quand le fichier ne contient aucun livre. Une liste vide devrait être retournée par la méthode");
        }
    }

    @Test
    @VisibleForTesting
    void loadBooksOkWithAnInvalidAndAValidBook() {
        Path filePath = Paths.get(PATH_RESOURCES,"loadBooksOkWithAnInvalidAndAValidBook",FILENAME).toAbsolutePath();
        try {
            JsonRepository repository = new JsonRepository(filePath);
            List<CanCreateBook> collectedBooks = repository.loadAllBooks();

            assertNotNull(collectedBooks);
            assertEquals(1,collectedBooks.size());

            CanCreateBook book = collectedBooks.get(0);

            assertNotNull(book);
            assertEquals(AUTHOR_NAME,book.getAuthorName());
            assertEquals(FIRST_ISBN,book.getISBNRepresentation());
            assertEquals(FIRST_TITLE,book.getTitle());
            assertEquals(FIRST_RESUME,book.getResume());

        } catch(Exception e) {
            fail("La méthode load ne devrait pas déclencher d'exception quand le fichier contient un livre mal formatté et un autre correctement formatté. Le livre correctement formatté devrait faire partie de la liste retournée");
        }
    }
}
*/
