package org.helmo.gbeditor.domains;

import java.util.*;

/**
 * La classe Page implémente l'interface CanCreateBookPage et permet la création et la gestion d'une page d'un livre-jeu
 */
public class Page implements CanCreateBookPage {
    private int numberPage;
    private final String content;
    private final Map<String,CanCreateBookPage> choices = new HashMap<>();

    /**
     * Le constructeur de la classe Page permet de construire une instance de Page sur base d'un numéro de page et d'un texte pour la page
     * @param numberPage Numéro de la page
     * @param content Texte de la page
     */
    public Page(int numberPage,String content) {
        this.numberPage = Math.max(numberPage, 1);
        if(content == null || content.isBlank()) {
            throw new IllegalArgumentException("Le texte de la page ne doit pas être null ou vide");
        } else {
            this.content = content.trim();
        }
    }

    @Override
    public void addNewChoice(String content,CanCreateBookPage page) {
        checkNullOrEmptyChoice(content,page);
        checkIfChoiceIsCorrect(content);
        choices.put(content,page);
    }

    private void checkNullOrEmptyChoice(String content,CanCreateBookPage page) {
        if(content == null || content.isBlank() || page == null) {
            throw new IllegalArgumentException("Le texte du choix ne peut pas être vide");
        }
    }

    private void checkIfChoiceIsCorrect(String content) {
        for(var choice : choices.entrySet()) {
            if(choice.getKey().equals(content)) {
                throw new IllegalArgumentException("Un autre choix de la page existe déjà avec le même texte");
            }
        }
    }

    @Override
    public boolean deleteChoice(String content) {
        if(choices.containsKey(content)) {
            choices.remove(content);
            return true;
        }
        return false;
    }

    @Override
    public int getNumberPage() {
        return numberPage;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Map<String,CanCreateBookPage> getChoices() {
        return choices;
    }

    @Override
    public Map<String,Integer> getFormattedChoices() {
        Map<String,Integer> formattedChoices = new HashMap<>();
        for(var choice : choices.entrySet()) {
            formattedChoices.put(choice.getKey(),choice.getValue().getNumberPage());
        }
        return formattedChoices;
    }

    @Override
    public void setNumberPage(int newNumberPage) {
        this.numberPage = Math.max(1,newNumberPage);
    }

    @Override
    public boolean isLinkedToPage(int indexPage) {
        for(var choice : choices.entrySet()) {
            if(choice.getValue().getNumberPage() - 1 == indexPage) {
                return true;
            }
        }
        return false;
    }

    // Source pour le removeIf : https://stackoverflow.com/questions/602636/why-is-a-concurrentmodificationexception-thrown-and-how-to-debug-it
    @Override
    public void removeChoicesLinked(int indexPage) {
        choices.values().removeIf(value -> value.getNumberPage() - 1 == indexPage);
    }

    @Override
    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }
        if(!(other instanceof Page)) {
            return false;
        }

        Page that = (Page) other;

        return this.numberPage == that.getNumberPage();
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberPage);
    }
}
