package org.helmo.gbeditor.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.helmo.gbeditor.presentations.*;
import org.helmo.gbeditor.presentations.views.CanCreateWelcomeView;
import org.helmo.gbeditor.presentations.views.ViewInterface;

/**
 * La classe WelcomeView permet d'obtenir une vue qui est la page d'accueil du livre-jeu
 */
public class WelcomeView implements CanCreateWelcomeView, JavaFXViewInterface {
    private WelcomePresenter presenter;
    private ViewInterface mainView;

    private final Label title = new Label("GameBook - Menu"); {
        VBox.setMargin(title,new Insets(0,0,20,0));
        title.getStyleClass().add("title");
    }


    private final Label errorMessage = new Label(""); {
        errorMessage.setVisible(false);
        errorMessage.getStyleClass().add("errorMessage");
    }

    private final Label successMessage = new Label(""); {
        successMessage.setVisible(false);
        successMessage.getStyleClass().add("successMessage");
    }

    private final Button createBookButton = new Button("Créer un livre-jeu"); {
        VBox.setMargin(createBookButton,new Insets(0,0,10,0));
        createBookButton.getStyleClass().add("button");
        createBookButton.setOnAction(event -> switchScreen("CreateBookView"));
    }

    private final Button editBookButton = new Button("Modifier ce livre"); {
        VBox.setMargin(editBookButton,new Insets(10,0,10,0));
        editBookButton.getStyleClass().add("button");
        editBookButton.setOnAction(event -> {
            updateSelectedBook();
            switchScreen("EditBookView");
        });
    }

    private final ObservableList<String> booksList = FXCollections.observableArrayList();

    private ListView<String> booksListView = new ListView<>(); {
        booksListView.setItems(booksList);
    }

    private Label emptyListMessage = new Label("Aucun livre trouvé pour cet auteur");

    private final VBox welcomePane = new VBox(); {
        BorderPane.setMargin(welcomePane,new Insets(50,50,50,50));
        welcomePane.setAlignment(Pos.CENTER);
        welcomePane.setFillWidth(false);
        welcomePane.getChildren().add(title);
        welcomePane.getChildren().add(successMessage);
        welcomePane.getChildren().add(errorMessage);
        welcomePane.getChildren().add(createBookButton);
        welcomePane.getChildren().add(emptyListMessage);
        welcomePane.getChildren().add(booksListView);
        welcomePane.getChildren().add(editBookButton);
    }

    @Override
    public Parent getRoot() {
        loadBooks();
        return welcomePane;
    }

    @Override
    public void setMainView(ViewInterface mainView) {
        if(mainView != null) {this.mainView = mainView;}
    }

    @Override
    public void setPresenter(Presenter p) {
        if(p != null) {
            presenter = (WelcomePresenter) p;
        }
    }

    private void loadBooks() {
        if(!presenter.loadBooks()) {
            welcomePane.getChildren().clear();
            welcomePane.getChildren().add(errorMessage);
        } else {
            onUpdateLengthBooksList();
        }
    }

    private void onUpdateLengthBooksList() {
        if(booksList.size() == 0) {
            booksListView.setVisible(false);
            editBookButton.setVisible(false);
            emptyListMessage.setVisible(true);
        } else {
            booksListView.setVisible(true);
            editBookButton.setVisible(true);
            emptyListMessage.setVisible(false);
            updateSelectedBookInList(0);
        }
    }

    private void updateSelectedBookInList(int index) {
        if(index < booksList.size()) {
            booksListView.getSelectionModel().select(index);
        }
    }

    private void updateSelectedBook() {
        int indexBook = booksListView.getSelectionModel().getSelectedIndex();
        presenter.switchCurrentBook(indexBook);
    }

    @Override
    public void displayInfo(String info) {
        successMessage.setText(info);
        successMessage.setVisible(true);
        errorMessage.setVisible(false);
    }

    @Override
    public void displayError(String error) {
        errorMessage.setText(error);
        errorMessage.setVisible(true);
        successMessage.setVisible(false);
    }

    @Override
    public void switchScreen(String viewName) {
        mainView.switchScreen(viewName);
    }

    @Override
    public void addBookInListView(GameBookViewModel book) {
        booksList.add(String.format("Titre : %s\nISBN : %s\nAuteur : %s\nEtat : %s", book.getShortTitleBook(), book.getIsbnBook(), book.getAuthorName(),book.getPublishState()));
        onUpdateLengthBooksList();
    }

    @Override
    public void cleanBooksListView() {
        booksList.clear();
    }
}
