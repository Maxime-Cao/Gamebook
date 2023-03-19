package org.helmo.gbeditor.presentations;

import org.helmo.gbeditor.domains.CanCreateBookPage;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe PageViewModel permet de construire un modèle de vue pour une page d'un livre-jeu
 */
public class PageViewModel {
    private final CanCreateBookPage page;

    /**
     * Le construire de la classe PageViewModel permet de construire une instance de PageViewModel sur base d'une page d'un livre-jeu (CanCreateBookPage)
     * @param page Page du livre jeu : instance de CanCreateBookPage
     */
    public PageViewModel(CanCreateBookPage page) {
        if(page != null) {
            this.page = page;
        } else {
            throw new IllegalArgumentException("Votre instance de CanCreateBookPage ne doit pas être nulle");
        }
    }

    /**
     * Permet d'obtenir le numéro de la page
     * @return Le numéro de la page
     */
    public int getNumberPage() {
        return page.getNumberPage();
    }

    /**
     * Permet d'obtenir une version condensée du texte de la page (maximum 40 caractères)
     * @return Version condensée du texte de la page
     */
    public String getShortContentPage() {
        String content = page.getContent();
        return content.length() > 40 ? content.substring(0,40) : content;
    }

    /**
     * Permet d'obtenir le texte de la page
     * @return Texte de la page
     */
    public String getContentPage() {
        return page.getContent();
    }

    /**
     * Permet de construire un modèle de vue pour chaque choix de la page courante
     * @return Une liste de modèles de vue pour chaque choix de la page courante
     */
    public List<ChoiceViewModel> getChoices() {
        List<ChoiceViewModel> choices = new ArrayList<>();
        for(var choice : page.getFormattedChoices().entrySet()) {
            choices.add(new ChoiceViewModel(choice.getKey(),choice.getValue()));
        }
        return choices;
    }
}
