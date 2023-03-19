package org.helmo.gbeditor.presentations;

import org.helmo.gbeditor.domains.CanCreateGameBookSession;
import org.helmo.gbeditor.presentations.views.ViewInterface;
import org.helmo.gbeditor.repositories.IFactory;
import org.helmo.gbeditor.repositories.IRepository;

/**
 *  * La classe CreateBookPresenter implémente un présenteur responsable d'interagir avec une vue de type "création d'un livre-jeu" d'un livre-jeu et des classes du modèle utiles à cette vue
 */
public class CreateBookPresenter implements Presenter {
    private final ViewInterface view;
    private final CanCreateGameBookSession currentSession;
    private final IFactory factory;

    /**
     * Le constructeur de la classe CreateBookPresenter permet de créer un objet CreateBookPresenter sur base d'une vue, d'une session utilisateur de livre-jeu et d'une factory
     * @param view Vue de type "création d'un livre-jeu"
     * @param currentSession Session actuelle d'un utilisateur de livre-jeu
     * @param factory Classe permettant la gestion et la sauvegarde de livres
     */
    public CreateBookPresenter(ViewInterface view,CanCreateGameBookSession currentSession, IFactory factory) {
        if(view == null || currentSession == null || factory == null) {
            throw new IllegalArgumentException("Votre vue, session et repository doivent être correctement initialisés");
        }
        this.view = view;
        this.currentSession = currentSession;
        this.factory = factory;
        view.setPresenter(this);
    }

    /**
     * Permet de demander à une session de livre-jeu la création d'un livre-jeu sur base d'une langue, d'un identifiant pour le livre, d'un titre de livre et d'une description
     * @param language Langue du livre
     * @param idBook Identifiant du livre
     * @param title Titre du livre
     * @param resume Description du livre
     */
    public void createGameBook(String language,int idBook,String title, String resume) {
        try {
            if(currentSession.setGameBook(language,idBook,title,resume)) {
                try(IRepository repo = factory.newStorageSession()) {
                    repo.saveABookWithoutPage(currentSession.getCurrentBook());
                    view.displayInfo("Votre livre a été créé avec succès");
                } catch (Exception exception) {
                    view.displayError(exception.getMessage());
                }
            }
        } catch(IllegalArgumentException ex) {
            view.displayError(ex.getMessage());
        }
    }

    /**
     * Permet d'obtenir depuis la session, le premier identifiant disponible pour un livre-jeu dans une langue donnée
     * @param language Langue du livre (1 chiffre de l'ISBN)
     * @return Retourne le premier identifiant disponible
     */
    public int getMinIdBook(String language) {
        return currentSession.getFirstIdBookAvailableInSession(language);
    }
}
