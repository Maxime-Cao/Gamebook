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
import org.helmo.gbeditor.presentations.views.CanCreateEditPageView;
import org.helmo.gbeditor.presentations.views.ViewInterface;

public class EditPageView implements CanCreateEditPageView, JavaFXViewInterface {
    private EditPagePresenter presenter;
    private ViewInterface mainView;

    private final Label title = new Label("GameBook - Page d'un livre"); {
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

    private final TextField numberPage = new TextField(); {
        numberPage.getStyleClass().add("input");
        numberPage.setEditable(false);
    }

    private final TextArea textPage = new TextArea(); {
        textPage.getStyleClass().add("text-area");
        textPage.setEditable(false);
        textPage.setWrapText(true);
    }

    private final Button goBackEditBookView = new Button("Revenir en arrière"); {
        goBackEditBookView.getStyleClass().add("bigButton");
        goBackEditBookView.setOnAction(event -> {
            switchScreen("EditBookView");
            resetView();
        });
    }

    private final Button addChoiceButton = new Button("Ajouter un choix"); {
        VBox.setMargin(addChoiceButton,new Insets(0,0,10,0));
        addChoiceButton.getStyleClass().add("bigButton");
        addChoiceButton.setOnAction(event -> {
            switchScreen("AddChoiceView");
            resetView();
        });
    }

    private final Button deleteChoiceButton = new Button("Supprimer le choix"); {
        VBox.setMargin(deleteChoiceButton,new Insets(0,0,0,5));
        deleteChoiceButton.getStyleClass().add("bigButton");
        deleteChoiceButton.setOnAction(event -> {
            deleteChoice();
        });
    }

    private final Button saveUpdateButton = new Button("Modifier"); {
        VBox.setMargin(saveUpdateButton,new Insets(10,0,0,0));
        saveUpdateButton.getStyleClass().add("bigButton");
        saveUpdateButton.setOnAction(event -> {
            switchScreen("EditBookView");
            resetView();
        });
    }

    private final ObservableList<String> choicesList = FXCollections.observableArrayList();

    private ListView<String> choicesListView = new ListView<>(); {
        choicesListView.setItems(choicesList);
    }

    private Label emptyListMessage = new Label("Aucun choix trouvé pour cette page"); {
        VBox.setMargin(emptyListMessage,new Insets(10,0,0,10));
    }

    private final VBox choiceButtonsPane = new VBox(); {
        choiceButtonsPane.getChildren().add(deleteChoiceButton);
    }

    private final HBox choicePane = new HBox(); {
        choicePane.getChildren().addAll(choicesListView,choiceButtonsPane);
    }

    private final VBox editPagePane = new VBox(); {
        BorderPane.setMargin(editPagePane,new Insets(50,50,50,50));
        editPagePane.setAlignment(Pos.CENTER);
        editPagePane.setFillWidth(false);

        editPagePane.getChildren().add(goBackEditBookView);
        editPagePane.getChildren().add(title);
        editPagePane.getChildren().addAll(new Label("Texte de la page : "),textPage);
        editPagePane.getChildren().addAll(new Label("Numéro de la page : "),numberPage);
        editPagePane.getChildren().addAll(emptyListMessage,addChoiceButton,choicePane,successMessage,errorMessage,saveUpdateButton);
    }

    @Override
    public void setMainView(ViewInterface mainView) {
        if(mainView != null) {this.mainView = mainView;}
    }

    @Override
    public void setPresenter(Presenter p) {
        if(p != null) {presenter = (EditPagePresenter) p;}
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
    public Parent getRoot() {
        loadPageView();
        return editPagePane;
    }

    @Override
    public void addChoiceInListView(ChoiceViewModel choice) {
        choicesList.add(String.format("Texte du choix :\n%s\nPage ciblée : %d",choice.getTextChoice(),choice.getNumberPageDest()));
        onUpdateLengthChoicesList();
    }

    public void loadPageFields(PageViewModel page) {
        textPage.setText(page.getContentPage());
        numberPage.setText(String.valueOf(page.getNumberPage()));
    }

    @Override
    public void adaptViewOnPublishState(boolean publishState) {
        if(publishState) {
            choicePane.getChildren().remove(choiceButtonsPane);
            setInvisible(addChoiceButton,saveUpdateButton);
        }
    }

    private void setVisible(Parent ...components) {
        for(var component : components) {
            component.setVisible(true);
        }
    }

    private void setInvisible(Parent ...components) {
        for(var component : components) {
            component.setVisible(false);
        }
    }

    private void loadPageView() {
        presenter.loadPageAndChoices();
        onUpdateLengthChoicesList();
    }

    private void onUpdateLengthChoicesList() {
        choicePane.getStyleClass().clear();
        if(choicesList.size() == 0) {
            choicePane.setVisible(false);
            choicePane.getStyleClass().add("listEmpty");
            emptyListMessage.setVisible(true);
        } else {
            choicePane.setVisible(true);
            emptyListMessage.setVisible(false);
            updateSelectedChoiceInPage(0);
        }
    }

    private void updateSelectedChoiceInPage(int index) {
        choicesListView.getSelectionModel().select(index);
    }

    private void resetView() {
        textPage.setText("");
        numberPage.setText("");
        choicesList.clear();
        setInvisible(errorMessage,successMessage);
        if(!choicePane.getChildren().contains(choiceButtonsPane)) {
            choicePane.getChildren().add(1,choiceButtonsPane);
        }
        setVisible(addChoiceButton,saveUpdateButton);
    }

    private void deleteChoice() {
        String textChoice = choicesListView.getSelectionModel().getSelectedItem();
        if(textChoice != null) {
            textChoice = textChoice.substring(textChoice.indexOf("\n") + 1);
            textChoice = textChoice.substring(0, textChoice.lastIndexOf("\n"));
            if (presenter.deleteChoice(textChoice)) {
                choicesList.remove(choicesListView.getSelectionModel().getSelectedIndex());
                onUpdateLengthChoicesList();
            }
        }
    }
}
