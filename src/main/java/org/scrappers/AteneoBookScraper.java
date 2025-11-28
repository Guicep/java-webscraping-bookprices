package org.scrappers;

import java.util.concurrent.TimeUnit;

import org.dto.BookDTO;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.utils.Input;

public class AteneoBookScraper extends BookScraper {

    public AteneoBookScraper() {
        super();
    }

    private Elements getBookElements(Document document) {
        return document.select("div[class^=js-item-description]");
    }

    @Override
    protected void fetchBooks(Response response, Document document, String authorFullName) throws Exception {
        Elements elements = this.getBookElements(document);
        for (Element element : elements) {
            Elements bookAuthor = element.select("a:not([title])");
            if (! bookAuthor.isEmpty() && bookAuthor.first().text().matches(".*" + authorFullName + ".*")) {
                this.books.add(createBook(element, bookAuthor.first().text(), element.select("a[href]").first().attr("href")));
            }
        }
        TimeUnit.SECONDS.sleep(2);
    }

    @Override
    protected boolean getFinishCondition(Document document) {
        return this.getBookElements(document).size() == 12;
    }

    @Override
    protected String getUrl(int page, Input input) {
        return String.format(
                "https://www.yenny-elateneo.com/search/page/%d/?q=%s&results_only=true&limit=12",
                page,
                this.urlParameters(input.getFullTitle())
        );
    }

    @Override
    protected String getScraperName() {
        return "Ateneo";
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

    @Override
    protected BookDTO createBook(Element element, String author, String link) {
        String title = element.select("a[href] > div")
                .first()
                .text();
        String priceArs = element.select("div[class=item-price-container] > span:not([style])")
                .first()
                .text();

        return new BookDTO(title, author, link, priceArs);
    }
}
