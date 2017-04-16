
package com.crazyhitty.chdev.ks.firebasechat.models;

import java.util.List;

public class Tuc {

    private List<Meaning> meanings = null;
    private Object meaningId;
    private List<Integer> authors = null;
    private Phrase phrase;

    public List<Meaning> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<Meaning> meanings) {
        this.meanings = meanings;
    }

    public Object getMeaningId() {
        return meaningId;
    }

    public void setMeaningId(Object meaningId) {
        this.meaningId = meaningId;
    }

    public List<Integer> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Integer> authors) {
        this.authors = authors;
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(Phrase phrase) {
        this.phrase = phrase;
    }

}
