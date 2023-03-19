package org.helmo.gbeditor.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.helmo.gbeditor.presentations.*;
import org.helmo.gbeditor.presentations.views.CanCreateEditBookView;
import org.helmo.gbeditor.presentations.views.ViewInterface;

public class EditBookView implements JavaFXViewInterface, CanCreateEditBookView {
    private EditBookPresenter presenter;
    private ViewInterface mainView;
    private final int maxSizeResume = 500;
    private final int maxSizeTitle = 150;
    private final int maxIdBook = 99;
    private int minIdBook = 1;

    private final Label title = new Label("GameBook - Edition d'un livre"); {
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

    private final Label isbnRepresentation = new Label(""); {
        isbnRepresentation.getStyleClass().add("isbn");
    }

    private final ComboBox<String> languageNumber = new ComboBox<>(); {
        languageNumber.getItems().addAll("Anglais: 0","Anglais: 1","Français: 2","Allemand: 3","Japonais: 4","Chine: 7");
        languageNumber.getSelectionModel().select(2);
        languageNumber.getStyleClass().add("input");
    }

    private final Spinner<Integer> idBook = new Spinner<Integer>(minIdBook,maxIdBook,minIdBook); {
        idBook.getStyleClass().add("input");
    }

    private final Button saveChangesBook = new Button("Sauvegarder les modifications"); {
        HBox.setMargin(saveChangesBook,new Insets(10,0,10,0));
        saveChangesBook.getStyleClass().add("bigButton");
        saveChangesBook.setOnAction(event -> saveBook());
    }

    private final Button goBackMenu = new Button("Revenir au menu"); {
        goBackMenu.getStyleClass().add("bigButton");
        goBackMenu.setOnAction(event -> {
            onSwitchBooksView();
        });
    }

    private final Button updatePageButton = new Button("Modifier la page"); {
        VBox.setMargin(updatePageButton,new Insets(0,0,0,5));
        updatePageButton.getStyleClass().add("bigButton");
        updatePageButton.setOnAction(event -> {
            setSelectedPage();
            setInvisible(errorMessage,successMessage);
            switchScreen("EditPageView");
        });
    }

    private final Button addPageButton = new Button("Ajouter une page"); {
        VBox.setMargin(addPageButton,new Insets(0,0,10,0));
        addPageButton.getStyleClass().add("bigButton");
        addPageButton.setOnAction(event -> {
            setInvisible(errorMessage,successMessage);
            switchScreen("AddPageView");});
    }

    private final Button deletePageButton = new Button("Supprimer la page"); {
        VBox.setMargin(deletePageButton,new Insets(5,0,0,5));
        deletePageButton.getStyleClass().add("bigButton");
        deletePageButton.setOnAction(event -> {
            setInvisible(errorMessage,successMessage);
            onDeletePage();
        });
    }

    private final Button publishBookButton = new Button("Publier le livre"); {
        HBox.setMargin(publishBookButton,new Insets(10,0,0,5));
        publishBookButton.getStyleClass().add("bigButton");
        publishBookButton.setOnAction(event -> publishBook());
    }

    private final ObservableList<String> pagesList = FXCollections.observableArrayList();

    private ListView<String> pagesListView = new ListView<>(); {
        pagesListView.setItems(pagesList);
    }

    private Label emptyListMessage = new Label("Aucune page trouvée pour ce livre"); {
        VBox.setMargin(emptyListMessage,new Insets(10,0,0,10));
    }

    private final VBox pageButtonsPane = new VBox(); {
        pageButtonsPane.getChildren().addAll(updatePageButton,deletePageButton);
    }

    private final HBox pagePane = new HBox(); {
        pagePane.getChildren().addAll(pagesListView,pageButtonsPane);
    }

    private final HBox savePane = new HBox(); {
        savePane.getChildren().addAll(saveChangesBook,publishBookButton);
    }

    private final VBox editBookPane = new VBox(); {
        BorderPane.setMargin(editBookPane,new Insets(50,50,50,50));
        editBookPane.setAlignment(Pos.CENTER);
        editBookPane.setFillWidth(false);

        editBookPane.getChildren().add(goBackMenu);
        editBookPane.getChildren().add(title);
        editBookPane.getChildren().add(isbnRepresentation);
        editBookPane.getChildren().addAll(new Label("Langue du livre : "),languageNumber);
        editBookPane.getChildren().addAll(new Label("ID du livre : "),idBook);
        editBookPane.getChildren().addAll(new Label(String.format("Titre du livre (max %d caractères) : ",maxSizeTitle)),titleBookInput);
        editBookPane.getChildren().addAll(new Label(String.format("Résumé (max %d caractères) : ",maxSizeResume)),descriptionInput);
        editBookPane.getChildren().addAll(emptyListMessage,addPageButton,pagePane,successMessage,errorMessage,savePane);
    }

    @Override
    public Parent getRoot() {
        loadBookView();
        return editBookPane;
    }

    private void loadBookView() {
        presenter.loadCurrentBook();
        onUpdateLengthPagesList();
    }

    @Override
    public void setMainView(ViewInterface mainView) {
        if(mainView != null) {this.mainView = mainView;}
    }

    @Override
    public void setPresenter(Presenter p) {
        if(p != null) {presenter = (EditBookPresenter) p;}
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
    public void setCurrentBookInEditView(GameBookViewModel book) {
        isbnRepresentation.setText(String.format("ISBN : %s",book.getIsbnBook()));
        titleBookInput.setText(book.getTitleBook());
        descriptionInput.setText(book.getResumeBook());
        idBook.getValueFactory().setValue(book.getIdBook());

        for(var item : languageNumber.getItems()) {
            if(item.substring(item.length() - 1).equals(book.getLanguageBook())) {
                languageNumber.getSelectionModel().select(item);
                break;
            }
        }
        adaptDisplayWithPState(book.getPublishState());
    }

    private void adaptDisplayWithPState(String publishState) {
        if(publishState.equals("publié")) {
            setDisable(languageNumber,descriptionInput,idBook,titleBookInput);
            setInvisible(deletePageButton,addPageButton,savePane);
            updatePageButton.setText("Voir la page");
        }
    }

    @Override
    public void cleanPagesListView() {
        pagesList.clear();
        onUpdateLengthPagesList();
    }

    @Override
    public void addPageInListView(PageViewModel page) {
        pagesList.add(String.format("%d) %s",page.getNumberPage(),page.getShortContentPage()));
        onUpdateLengthPagesList();
    }

    private void initSpinner(Spinner<Integer> spinner, int borneMin, int borneMax,int value) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(borneMin,borneMax));
        spinner.getValueFactory().setValue(value);
    }

    private void resetView() {
        initSpinner(idBook,minIdBook,maxIdBook,minIdBook);
        titleBookInput.setText("");
        descriptionInput.setText("");
        updatePageButton.setText("Modifier la page");
        cleanPagesListView();
        setInvisible(errorMessage,successMessage);
        setVisible(deletePageButton,addPageButton,savePane);
        setNonDisable(languageNumber,descriptionInput,idBook,titleBookInput);
    }

    private void setDisable(Parent ...components) {
        for(var component : components) {
            component.setDisable(true);
        }
    }

    private void setNonDisable(Parent ...components) {
        for(var component : components) {
            component.setDisable(false);
        }
    }

    private void setVisible(Parent ...components) {
        for(var component : components) {
            component.setVisible(true);
        }
    }

    private void setInvisible(Parent ... components) {
        for(var component : components) {
            component.setVisible(false);
        }
    }

    private void onUpdateLengthPagesList() {
        pagePane.getStyleClass().clear();
        if(pagesList.size() == 0) {
            pagePane.setVisible(false);
            pagePane.getStyleClass().add("listEmpty");
            emptyListMessage.setVisible(true);
        } else {
            pagePane.setVisible(true);
            emptyListMessage.setVisible(false);
            updateSelectedPageInBook(0);
        }
    }

    private void updateSelectedPageInBook(int index) {
        pagesListView.getSelectionModel().select(index);
    }

    private void onSwitchBooksView() {
        switchScreen("WelcomeView");
        resetView();
        presenter.updateLoadStatus();
    }

    private void setSelectedPage() {
        int indexPage = pagesListView.getSelectionModel().getSelectedIndex();
        presenter.setCurrentPage(indexPage);
    }

    private void onDeletePage() {
        setSelectedPage();
        int nbrPages = presenter.getNumberPagesLinked();
        if(nbrPages > 0) {
            // Source pour Alert : https://stackoverflow.com/questions/8309981/how-to-create-and-show-common-dialog-error-warning-confirmation-in-javafx-2
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, String.format("Cette page est référencée dans %d autre(s) page(s), êtes-vous sûr ?",nbrPages), ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if(alert.getResult() == ButtonType.YES) {
                presenter.deletePage(true);
            }
        } else {
            presenter.deletePage(false);
        }
    }

    private void publishBook() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Êtes-vous sûr de vouloir publier ce livre ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES) {
            if(presenter.publishBook()) {
                if(saveBook()) {
                    adaptDisplayWithPState("publié");
                }
            }
        }
    }

    private boolean saveBook() {
        String language = languageNumber.getSelectionModel().getSelectedItem();
        return presenter.saveBook(language.substring(language.length() - 1),idBook.getValue(),titleBookInput.getText(),descriptionInput.getText());
    }
}
