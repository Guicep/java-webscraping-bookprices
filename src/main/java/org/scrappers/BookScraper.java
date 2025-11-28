package org.scrappers;

import org.dto.BookDTO;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.utils.Input;

import java.util.ArrayList;

public abstract class BookScraper {

    protected ArrayList<BookDTO> books;

    public BookScraper() {
        this.books = new ArrayList<>();
    }

    public ArrayList<BookDTO> getBooks(Input input) throws Exception {
        final int MAX_PAGES = 5;

        int page = 0;
        boolean booksRemaining;
        String authorFullName = this.formatAuthor(input.getAuthorName(), input.getAuthorSurname());

        System.out.print(this.getScraperName() + " current page: | ");
        do {
            String url = this.getUrl(++page, input);
            System.out.print(page + " | ");
            Response response = Jsoup.connect(url).method(Method.GET).execute();
            Document document = Jsoup.parse(response.body());
            this.fetchBooks(response, document, authorFullName);
            booksRemaining = this.getFinishCondition(document);
        } while (booksRemaining && page != MAX_PAGES);
        System.out.println();

        return this.books;
    }

    protected String urlParameters(String parameters) {
        return parameters.replaceAll(" ", "+");
    }

    protected abstract void fetchBooks(Response response, Document document, String authorFullName) throws Exception;

    protected abstract boolean getFinishCondition(Document document);

    protected abstract String getUrl(int page, Input input);

    protected abstract String getScraperName();

    protected abstract String formatAuthor(String name, String surname);

    protected abstract BookDTO createBook(Element element, String author, String link);
}