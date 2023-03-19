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
import org.helmo.gbeditor.presentations.views.CanCreateAddPageView;
import org.helmo.gbeditor.presentations.views.ViewInterface;

public class AddPageView implements CanCreateAddPageView, JavaFXViewInterface {
    private AddPagePresenter presenter;
    private ViewInterface mainView;

    private final Label title = new Label("GameBook - Ajout d'une page"); {
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

    private final TextArea textPage = new TextArea(); {
        textPage.getStyleClass().add("text-area");
        textPage.setWrapText(true);
    }

    private final ComboBox<String> positionPage = new ComboBox<>(); {
        positionPage.getStyleClass().add("input");
    }

    private final Button goBackEditBookView = new Button("Revenir en arrière"); {
        goBackEditBookView.getStyleClass().add("bigButton");
        goBackEditBookView.setOnAction(event -> {
            switchScreen("EditBookView");
            resetView();
        });
    }

    private final Button addPageButton = new Button("Ajouter"); {
        VBox.setMargin(addPageButton,new Insets(10,0,10,0));
        addPageButton.getStyleClass().add("bigButton");
        addPageButton.setOnAction(event -> {
            addPage();
        });
    }

    private final VBox addPagePane = new VBox(); {
        BorderPane.setMargin(addPagePane,new Insets(50,50,50,50));
        addPagePane.setAlignment(Pos.CENTER);
        addPagePane.setFillWidth(false);

        addPagePane.getChildren().add(title);
        addPagePane.getChildren().addAll(new Label("Texte de la page : "),textPage);
        addPagePane.getChildren().addAll(new Label("Position de la page : "),positionPage);
        addPagePane.getChildren().addAll(errorMessage,successMessage);
        addPagePane.getChildren().addAll(addPageButton,goBackEditBookView);
    }

    @Override
    public Parent getRoot() {
        setComboBox();
        return addPagePane;
    }

    @Override
    public void setMainView(ViewInterface mainView) {
        if(mainView != null) {this.mainView = mainView;}
    }


    @Override
    public void setPresenter(Presenter p) {
        if(p != null) {presenter = (AddPagePresenter) p;}
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
        textPage.setText("");
        positionPage.getItems().clear();
        errorMessage.setVisible(false);
        successMessage.setVisible(false);
    }

    private void setComboBox() {
        int numberPages = presenter.getNumberOfPages();
        positionPage.getItems().clear();

        if(numberPages == 0) {
            positionPage.getItems().add("Au début du livre");
            positionPage.getSelectionModel().select(0);
        } else {
            for(int i = 1; i <= numberPages; i++) {
                positionPage.getItems().add(String.format("Avant la page %d",i));
            }
            positionPage.getItems().add("A la fin du livre");
            positionPage.getSelectionModel().select(numberPages);
        }
    }

    private void addPage() {
        presenter.addNewPage(textPage.getText(),positionPage.getSelectionModel().getSelectedIndex());
        setComboBox();
    }
}
