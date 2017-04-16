
package com.crazyhitty.chdev.ks.firebasechat.models;

import java.util.List;

public class Dictionary {

    private String result;
    private List<Tuc> tuc = null;
    private String phrase;
    private String from;
    private String dest;
    private transient Authors authors;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Tuc> getTuc() {
        return tuc;
    }

    public void setTuc(List<Tuc> tuc) {
        this.tuc = tuc;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public Authors getAuthors() {
        return authors;
    }

    public void setAuthors(Authors authors) {
        this.authors = authors;
    }

}
