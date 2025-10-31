package org.utils;

public class Input {
    private String authorName;
    private String authorSurname;
    private String fullTitle;

    public Input(String authorName, String authorSurname, String fullTitle) {
        this.authorName = authorName;
        this.authorSurname = authorSurname;
        this.fullTitle = fullTitle;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorSurname() {
        return authorSurname;
    }

    public String getFullTitle() {
        return fullTitle;
    }
}
