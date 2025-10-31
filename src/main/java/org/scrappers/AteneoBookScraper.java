package org.scrappers;

import java.util.concurrent.TimeUnit;

import org.dto.BookDTO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.utils.Input;

import java.util.ArrayList;

public class AteneoBookScraper extends BookScraper {

    public AteneoBookScraper() {
        super();
    }

    @Override
    public ArrayList<BookDTO> getBooks(Input input) throws Exception {
        String authorFullName = this.formatAuthor(input.getAuthorName(), input.getAuthorSurname());
        int page = 1;
        Elements elements;
        do {
            String url = String.format(
                    "https://www.yenny-elateneo.com/search/page/%d/?q=%s&results_only=true&limit=12",
                    page,
                    this.urlParameters(input.getFullTitle())
            );
            Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET).execute();
            TimeUnit.SECONDS.sleep(2);
            Document document = Jsoup.parse(response.body());
            elements = document.select("div[class^=js-item-description]");
            for (Element element : elements) {
                Elements bookAuthor = element.select("a:not([title])");
                if (! bookAuthor.isEmpty() && bookAuthor.first().text().matches(".*" + authorFullName + ".*")) {
                    this.books.add(createBook(element, bookAuthor.first().text(), element.select("a[href]").first().attr("href")));
                }
            }
            page++;
        } while (elements.size() == 12);
        return this.books;
    }

    @Override
    protected String formatAuthor(String name, String surname) {
        String capitalName = "";
        String capitalSurname = "";
        if (! name.isEmpty()) {
            capitalName = name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        if (! surname.isEmpty()) {
            capitalSurname = surname.substring(0, 1).toUpperCase() + surname.substring(1);
        }
        return String.format("%s %s", capitalName, capitalSurname);
    }

    private BookDTO createBook(Element element, String author, String link) {
        String title = element.select("a[href] > div")
                .first()
                .text();
        String priceArs = element.select("div[class=item-price-container] > span:not([style])")
                .first()
                .text();

        return new BookDTO(title, author, link, priceArs);
    }
}
