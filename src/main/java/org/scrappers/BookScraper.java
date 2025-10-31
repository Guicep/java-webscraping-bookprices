package org.scrappers;

import org.dto.BookDTO;
import org.utils.Input;

import java.util.ArrayList;

public abstract class BookScraper {

    protected ArrayList<BookDTO> books;

    public BookScraper() {
        this.books = new ArrayList<>();
    }

    protected String urlParameters(String parameters) {
        return parameters.replaceAll(" ", "+");
    }

    protected abstract String formatAuthor(String authorName, String authorSurname);

    public abstract ArrayList<BookDTO> getBooks(Input input) throws Exception;
}
