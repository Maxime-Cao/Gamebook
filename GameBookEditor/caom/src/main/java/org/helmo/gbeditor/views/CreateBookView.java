package org.helmo.gbeditor.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.helmo.gbeditor.presentations.CreateBookPresenter;
import org.helmo.gbeditor.presentations.views.CanCreateBookView;
import org.helmo.gbeditor.presentations.Presenter;
import org.helmo.gbeditor.presentations.views.ViewInterface;

/**
 * La classe CreateBookView permet d'implémenter la vue de création du livre-jeu
 */
public class CreateBookView implements CanCreateBookView, JavaFXViewInterface {
    private CreateBookPresenter presenter;
    private ViewInterface mainView;
    private final int maxSizeResume = 500;
    private final int maxSizeTitle = 150;
    private final int maxIdBook = 99;
    private int minIdBook = 1;

    private final Label title = new Label("GameBook - Création d'un livre"); {
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

    private final TextField titleBookInput = new TextField(); {
        titleBookInput.getStyleClass().add("input");
        titleBookInput.lengthProperty().addListener((observable,oldValue,newValue) -> {
            if(titleBookInput.getLength() > maxSizeTitle) {
                titleBookInput.setText(titleBookInput.getText().substring(0, maxSizeTitle));
            }
        });
    }

    private final TextArea descriptionInput = new TextArea(); {
        descriptionInput.getStyleClass().add("text-area");
        descriptionInput.setWrapText(true);
        descriptionInput.lengthProperty().addListener((observable,oldValue,newValue) -> {
            if(descriptionInput.getLength() > maxSizeResume) {
                descriptionInput.setText(descriptionInput.getText().substring(0, maxSizeResume));
            }
        });
    }

    private final ComboBox<String> languageNumber = new ComboBox<>(); {
        languageNumber.getItems().addAll("Anglais: 0","Anglais: 1","Français: 2","Allemand: 3","Japonais: 4","Chine: 7");
        languageNumber.getSelectionModel().select(2);
        languageNumber.getStyleClass().add("input");
        languageNumber.setOnAction(event -> updateMinIdBookValue());
    }

    private final Spinner<Integer> idBook = new Spinner<Integer>(minIdBook,maxIdBook,minIdBook); {
        idBook.getStyleClass().add("input");
    }

    private final Button createBookButton = new Button("Créer le livre"); {
        VBox.setMargin(createBookButton,new Insets(20,0,0,0));
        createBookButton.getStyleClass().add("button");
        createBookButton.setOnAction(event -> createBook());
    }

    private void createBook() {
        String language = languageNumber.getSelectionModel().getSelectedItem();
        presenter.createGameBook(language.substring(language.length() - 1),idBook.getValue(),titleBookInput.getText(),descriptionInput.getText());
    }

    private final Button goBackMenu = new Button("Revenir au menu"); {
        VBox.setMargin(goBackMenu,new Insets(10,0,0,0));
        goBackMenu.getStyleClass().add("button");
        goBackMenu.setOnAction(event -> {
            switchScreen("WelcomeView");
            resetView();
        });
    }

    private final VBox createBookPane = new VBox(); {
        BorderPane.setMargin(createBookPane,new Insets(50,50,50,50));
        createBookPane.setAlignment(Pos.CENTER);
        createBookPane.setFillWidth(false);

        createBookPane.getChildren().add(title);
        createBookPane.getChildren().addAll(new Label("Langue du livre : "),languageNumber);
        createBookPane.getChildren().addAll(new Label("ID du livre : "),idBook);
        createBookPane.getChildren().addAll(new Label(String.format("Titre du livre (max %d caractères) : ",maxSizeTitle)),titleBookInput);
        createBookPane.getChildren().addAll(new Label(String.format("Résumé (max %d caractères) : ",maxSizeResume)),descriptionInput);
        createBookPane.getChildren().addAll(errorMessage,successMessage);
        createBookPane.getChildren().addAll(createBookButton,goBackMenu);
    }

    private void initSpinner(Spinner<Integer> spinner, int borneMin, int borneMax,int value) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(borneMin,borneMax));
        spinner.getValueFactory().setValue(value);
    }

    private void updateMinIdBookValue() {
        String language = languageNumber.getSelectionModel().getSelectedItem();
        minIdBook = presenter.getMinIdBook(language.substring(language.length() - 1));
        initSpinner(idBook,minIdBook,maxIdBook,minIdBook);
    }

    private void resetView() {
        languageNumber.getSelectionModel().select(2);
        initSpinner(idBook,minIdBook,maxIdBook,minIdBook);
        titleBookInput.setText("");
        descriptionInput.setText("");
        errorMessage.setVisible(false);
        successMessage.setVisible(false);
    }

    @Override
    public Parent getRoot() {
        updateMinIdBookValue();
        return createBookPane;
    }

    @Override
    public void setMainView(ViewInterface mainView) {
        if(mainView != null) {this.mainView = mainView;}
    }

    @Override
    public void setPresenter(Presenter p) {
        if(p != null) {presenter = (CreateBookPresenter) p;}
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
}
