package org.utils;

public class Input {
    private String authorFullName;
    private String fullTitle;

    public Input(String authorFullName, String fullTitle) {
        this.authorFullName = authorFullName;
        this.fullTitle = fullTitle;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public String getFullTitle() {
        return fullTitle;
    }
}
