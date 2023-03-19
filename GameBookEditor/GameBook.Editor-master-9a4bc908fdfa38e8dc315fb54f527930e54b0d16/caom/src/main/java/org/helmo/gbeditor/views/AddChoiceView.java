package org.helmo.gbeditor.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.helmo.gbeditor.presentations.*;
import org.helmo.gbeditor.presentations.views.CanCreateAddChoiceView;
import org.helmo.gbeditor.presentations.views.ViewInterface;

public class AddChoiceView implements CanCreateAddChoiceView, JavaFXViewInterface {
    private AddChoicePresenter presenter;
    private ViewInterface mainView;

    private final Label title = new Label("GameBook - Ajout d'un choix"); {
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

    private final Button goBackEditPageView = new Button("Revenir en arrière"); {
        goBackEditPageView.getStyleClass().add("bigButton");
        goBackEditPageView.setOnAction(event -> {
            switchScreen("EditPageView");
            resetView();
        });
    }

    private final Button addChoiceButton = new Button("Ajouter"); {
        VBox.setMargin(addChoiceButton,new Insets(10,0,10,0));
        addChoiceButton.getStyleClass().add("bigButton");
        addChoiceButton.setOnAction(event -> {
            addChoice();
        });
    }

    private final TextArea textChoice = new TextArea(); {
        textChoice.getStyleClass().add("text-area");
        textChoice.setWrapText(true);
    }

    private final ComboBox<String> targetPage = new ComboBox<>(); {
        targetPage.getStyleClass().add("input");
    }

    private final VBox addChoicePane = new VBox(); {
        BorderPane.setMargin(addChoicePane,new Insets(50,50,50,50));
        addChoicePane.setAlignment(Pos.CENTER);
        addChoicePane.setFillWidth(false);

        addChoicePane.getChildren().add(title);
        addChoicePane.getChildren().addAll(new Label("Texte du choix : "),textChoice);
        addChoicePane.getChildren().addAll(new Label("Page cible du choix : "),targetPage);
        addChoicePane.getChildren().addAll(errorMessage,successMessage);
        addChoicePane.getChildren().addAll(addChoiceButton,goBackEditPageView);
    }

    @Override
    public Parent getRoot() {
        loadTargetPages();
        return addChoicePane;
    }

    @Override
    public void setMainView(ViewInterface mainView) {
        if(mainView != null) {this.mainView = mainView;}
    }


    @Override
    public void setPresenter(Presenter p) {
        if(p != null) {presenter = (AddChoicePresenter) p;}
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

    private void resetView() {
        textChoice.setText("");
        targetPage.getItems().clear();
        errorMessage.setVisible(false);
        successMessage.setVisible(false);
    }

    private void loadTargetPages() {
        int nbrPages = presenter.getNumberOfPages();
        for(int i = 1; i<=nbrPages;i++) {
            targetPage.getItems().add(String.format("Page %d",i));
        }
    }

    private void addChoice() {
        String textPage = textChoice.getText();
        int indexPageDest = targetPage.getSelectionModel().getSelectedIndex();
        presenter.addChoiceInPage(textPage,indexPageDest);
    }
}
