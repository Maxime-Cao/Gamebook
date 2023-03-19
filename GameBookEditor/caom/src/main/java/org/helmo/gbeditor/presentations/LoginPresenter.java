package org.helmo.gbeditor.presentations;

import org.helmo.gbeditor.domains.CanCreateAuthor;
import org.helmo.gbeditor.domains.CanCreateGameBookSession;
import org.helmo.gbeditor.presentations.views.ViewInterface;
import org.helmo.gbeditor.repositories.exceptions.BadIdentifiersForUserException;
import org.helmo.gbeditor.repositories.IFactory;
import org.helmo.gbeditor.repositories.IRepository;
import org.helmo.gbeditor.presentations.views.CanCreateLoginView;

/**
 * La classe LoginPresenter implémente un présenteur responsable d'interagir avec une vue de type "page de connexion" d'un livre-jeu et des classes du modèle utiles à cette vue
 */
public class LoginPresenter implements Presenter {
    private final CanCreateLoginView view;
    private final CanCreateGameBookSession currentSession;
    private final IFactory factory;

    /**
     * Le constructeur de la classe LoginPresenter permet de créer un objet LoginPresenter sur base d'une vue et d'une session utilisateur de livre-jeu
     * @param view Vue de type "page de connexion"
     * @param currentSession Session actuelle d'un utilisateur de livre-jeu
     */
    public LoginPresenter(ViewInterface view, CanCreateGameBookSession currentSession, IFactory factory) {
        if(view == null || currentSession == null || factory == null) {
            throw new IllegalArgumentException("Votre vue,session et factory doivent être correctement initialisées");
        }
        this.view = (CanCreateLoginView) view;
        this.currentSession = currentSession;
        this.factory = factory;
        view.setPresenter(this);
    }

    /**
     * Permet de spécifier le prénom, le nom et le matricule de l'auteur connecté (qui sera associé à la session de livre-jeu)
     * @param firstname Le prénom de l'auteur
     * @param lastname Le nom de l'auteur
     * @param idAuthor L'identifiant de l'auteur
     * @return Vrai si l'utilisateur a bien été créé et associé à la session, faux sinon.
     */
    public boolean setAuthor(String firstname,String lastname,String idAuthor) {
        try(IRepository repository = factory.newStorageSession()) {
            currentSession.setAuthor(firstname,lastname,idAuthor);
            CanCreateAuthor author = currentSession.getCurrentAuthor();
            repository.addAuthor(author.getIdAuthor(),author.getFirstname(),author.getName());
            return true;
        } catch(IllegalArgumentException | BadIdentifiersForUserException e) {
            view.displayError(e.getMessage());
            return false;
        } catch(Exception ex) {
            view.deleteComponentsInView();
            view.displayError(ex.getMessage());
            return false;
        }
    }
}
