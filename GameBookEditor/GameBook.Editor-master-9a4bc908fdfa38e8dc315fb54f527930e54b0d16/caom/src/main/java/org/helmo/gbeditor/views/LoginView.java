package org.helmo.gbeditor.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.helmo.gbeditor.presentations.*;
import org.helmo.gbeditor.presentations.views.CanCreateLoginView;
import org.helmo.gbeditor.presentations.views.ViewInterface;

/**
 * La classe LoginView permet d'implémenter la vue "page de connexion" du livre-jeu
 */
public class LoginView implements CanCreateLoginView, JavaFXViewInterface {
    private LoginPresenter presenter;
    private ViewInterface mainView;
    private final int maxSizeMatricule = 6;

    private final TextField firstnameInput = new TextField(); {
        firstnameInput.getStyleClass().add("input");
    }

    private final TextField nameInput = new TextField(); {
        nameInput.getStyleClass().add("input");
    }

    private final TextField idAuthorInput = new TextField(); {
        idAuthorInput.getStyleClass().add("input");
        idAuthorInput.lengthProperty().addListener((observable,oldValue,newValue) -> {
            int lengthidAuthorInput = idAuthorInput.getLength();
            String contentidAuthorInput = idAuthorInput.getText();

            if(lengthidAuthorInput > maxSizeMatricule) {
                idAuthorInput.setText(contentidAuthorInput.substring(0, maxSizeMatricule));
            }
            if(lengthidAuthorInput > 0 && !contentidAuthorInput.matches("^[0-9]+")) {
                idAuthorInput.setText(contentidAuthorInput.substring(0,lengthidAuthorInput - 1));
            }
        });
    }

    private final Label errorMessage = new Label();{
        errorMessage.getStyleClass().add("errorMessage");
        errorMessage.setVisible(false);
    }

    private final Button connectButton = new Button("Connexion"); {
        VBox.setMargin(connectButton,new Insets(20,0,0,0));
        connectButton.getStyleClass().add("button");
        connectButton.setOnAction(event -> setAuthor());
    }

    private void setAuthor() {
        String firstname = firstnameInput.getText();
        String lastname = nameInput.getText();
        String idAuthor = idAuthorInput.getText();

        if(presenter.setAuthor(firstname,lastname,idAuthor)) {
            switchScreen("WelcomeView");
        }
    }

    private final Label title = new Label("GameBook - Connexion"); {
        VBox.setMargin(title,new Insets(0,0,20,0));
        title.getStyleClass().add("title");
    }

    private final VBox loginPane = new VBox();
    {
        BorderPane.setMargin(loginPane,new Insets(50,50,50,50));
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setFillWidth(false);

        loginPane.getChildren().add(title);
        loginPane.getChildren().addAll(new Label("Votre prénom : "),firstnameInput);
        loginPane.getChildren().addAll(new Label("Votre nom : "),nameInput);
        loginPane.getChildren().addAll(new Label("Votre identifiant : "),idAuthorInput);
        loginPane.getChildren().add(errorMessage);
        loginPane.getChildren().add(connectButton);
    }

    @Override
    public Parent getRoot() {
        return loginPane;
    }

    @Override
    public void setMainView(ViewInterface mainView) {
        if(mainView != null) {this.mainView = mainView;}
    }

    @Override
    public void setPresenter(Presenter p) {
        if(p != null) {presenter = (LoginPresenter) p;}
    }

    @Override
    public void displayInfo(String info) {

    }

    @Override
    public void displayError(String error) {
        errorMessage.setText(error);
        errorMessage.setVisible(true);
    }

    @Override
    public void switchScreen(String viewName) {
        mainView.switchScreen(viewName);
    }

    @Override
    public void deleteComponentsInView() {
        loginPane.getChildren().clear();
        loginPane.getChildren().add(errorMessage);
    }
}
