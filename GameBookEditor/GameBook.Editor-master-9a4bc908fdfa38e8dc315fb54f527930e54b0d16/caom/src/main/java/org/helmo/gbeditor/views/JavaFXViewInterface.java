package org.helmo.gbeditor.views;

import javafx.scene.Parent;
import org.helmo.gbeditor.presentations.views.ViewInterface;

/**
 * L'interface JavaFXViewInterface fournit des méthodes à implémenter pour les vues de type "JavaFX"
 */
public interface JavaFXViewInterface extends ViewInterface {
    /**
     * Permet de récupérer le scenegraph d'une vue
     * @return Scenegraph d'une vue
     */
    Parent getRoot();
}
