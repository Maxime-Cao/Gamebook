/*
package org.helmo.gbeditor.infrastructures;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.helmo.gbeditor.domains.CanCreateBook;
import org.helmo.gbeditor.infrastructures.dtos.SessionDto;
import org.helmo.gbeditor.repositories.IRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * La classe JsonRepository permet de stocker des livres dans un fichier JSON
 *//*

public class JsonRepository implements IRepository {
    private final String matricule = "d170051";
    private final Path folderPath;
    private final Path filePath;
    */
/**
     * Le constructeur sans paramètre de la classe JsonRepository permet de construire un objet JsonRepository avec un Path par défaut
     *//*

    // Source pour le System.getProperty(...) : https://stackoverflow.com/questions/585534/what-is-the-best-way-to-find-the-users-home-directory-in-java
    public JsonRepository() {
        this.folderPath = Paths.get(System.getProperty("user.home"),"ue36").toAbsolutePath();
        this.filePath = Paths.get(folderPath.toString(),matricule + ".json");
    }

    */
/**
     * Ce constructeur de la classe JsonRepository permet de construire un objet JsonRepository avec sur base d'un Path fourni en argument
     * @param filePath
     *//*

    public JsonRepository(Path filePath) {
        filePath = filePath == null ? null : filePath.toAbsolutePath();
        if(filePath != null && filePath.toString().endsWith(".json")) {
            this.folderPath = filePath.getParent();
            this.filePath = filePath;
        } else {
            throw new IllegalArgumentException("Le chemin du fichier ne doit pas être nul et doit correspondre à un fichier .json");
        }
    }

    @Override
    public void saveBooks(List<CanCreateBook> books) throws IOException {
        Mapping mapper = new Mapping();
        SessionDto booksDto = mapper.toDto(books);
        Gson gson = new Gson();

        createDirectoryForSaving();

        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)) {
            if(booksDto != null && booksDto.getNumberOfBooks() > 0) {
                gson.toJson(booksDto, bufferedWriter);
            }
        } catch (JsonIOException | IOException e) {
            throw new IOException("Problème d'écriture durant la sauvegarde des livres");
        }
    }

    @Override
    public List<CanCreateBook> loadAllBooks() throws Exception {
        Mapping map = new Mapping();
        Gson gson = new Gson();
        List<CanCreateBook> books = new ArrayList<>();

        createDirectoryForSaving();

        if(Files.exists(filePath)) {
            try(BufferedReader bufferedReader = Files.newBufferedReader(filePath,StandardCharsets.UTF_8)) {
                SessionDto booksCollected = gson.fromJson(bufferedReader,SessionDto.class);
                books = map.fromDto(booksCollected);
            } catch(JsonSyntaxException e) {
                throw new JsonSyntaxException("Erreur durant la lecture du fichier JSON. Veuillez vérifier la syntaxe des éléments du fichier");
            } catch (JsonIOException | IOException e) {
                throw new IOException("Erreur durant la lecture du fichier JSON");
            }
        }
        return books;
    }


    private void createDirectoryForSaving() throws IOException {
        if(Files.notExists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                throw new IOException("Erreur durant la création du dossier de sauvegarde");
            }
        }
    }
}
*/
