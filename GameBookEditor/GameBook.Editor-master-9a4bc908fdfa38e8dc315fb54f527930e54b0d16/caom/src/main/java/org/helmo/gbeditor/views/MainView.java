package org.helmo.gbeditor.views;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.helmo.gbeditor.presentations.*;
import org.helmo.gbeditor.presentations.views.CanCreateMainView;
import org.helmo.gbeditor.presentations.views.ViewInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * La classe MainView permet d'implémenter la vue principale du livre-jeu
 */
public class MainView implements CanCreateMainView, JavaFXViewInterface {
    private MainPresenter presenter;
    private final Map<String,JavaFXViewInterface> screens = new HashMap<>();

    private final BorderPane header = new BorderPane();
    {
        header.getStyleClass().add("header");
        header.setVisible(false);
    }

    private final BorderPane mainPane = new BorderPane();
    {
        mainPane.setTop(header);
    }

    /**
     * Le constructeur de la classe MainView prend en paramètres plusieurs vues qui implémentent la même interface. Il permet de construire un objet MainView qui sera la vue principale du livre-jeu
     * @param views Les vues du livre-jeu (exceptée la vue principale)
     */
    public MainView(JavaFXViewInterface ...views) {
        try {
            for (JavaFXViewInterface currentView : views) {
                screens.put(currentView.getClass().getSimpleName(), currentView);
                currentView.setMainView(this);
            }
        } catch(NullPointerException ex) {
            throw new IllegalArgumentException("Toutes vos vues doivent être une instance de ViewInterface");
        }
    }

    private void setScreen(Parent screen)
    {
       if(screen != null) {mainPane.setCenter(screen);}
    }

    @Override
    public Parent getRoot() {
        return mainPane;
    }

    @Override
    public void setMainView(ViewInterface mainView) {

    }

    @Override
    public void setPresenter(Presenter p) {
        if(p != null) {presenter = (MainPresenter) p;}
    }

    @Override
    public void switchScreen(String viewName) {
        if(screens.containsKey(viewName)) {
            setScreen(screens.get(viewName).getRoot());
            if(!viewName.equals("LoginView")) {updateNameHeader(presenter.getAuthorName());}
        }
    }

    private void updateNameHeader(String name) {
        Label userName = new Label("Connecté en tant que : " + name);
        header.setCenter(userName);
        header.setVisible(true);
    }

    @Override
    public void displayInfo(String info) {

    }

    @Override
    public void displayError(String error) {

    }
}
