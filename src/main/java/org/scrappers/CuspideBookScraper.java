package org.scrappers;

import java.util.concurrent.TimeUnit;

import org.dto.BookDTO;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.utils.Input;

public class CuspideBookScraper extends BookScraper {

    public CuspideBookScraper() {
        super();
    }

    @Override
    protected void fetchBooks(Response response, Document document, String authorFullName) throws Exception {
        if (! document.select("body[class*=single-product]").isEmpty()) {
            Elements bookAuthor = document.select("span > a[href]");
            String author = (! bookAuthor.isEmpty())? bookAuthor.first().text() : "";
            this.books.add(createBook(document, author, response.url().toString()));
        }
        Elements links = document.select("div[class^=product-small box] > div > div > a[href]");
        for (Element link : links) {
            Document doc = Jsoup.connect(link.attr("href")).get();
            TimeUnit.SECONDS.sleep(2);
            Elements bookAuthor = doc.select("span > a[href]");
            if (! bookAuthor.isEmpty() && bookAuthor.first().text().matches(".*" + authorFullName + ".*")) {
                this.books.add(createBook(doc, bookAuthor.first().text(), link.attr("href")));
            }
        }
    }

    @Override
    protected boolean getFinishCondition(Document document) {
        return ! document.select("i[class$=icon-angle-right]").isEmpty();
    }

    @Override
    protected String getUrl(int page, Input input) {
        return String.format(
                "https://cuspide.com/page/%d/?s=%s&post_type=product",
                page,
                this.urlParameters(input.getFullTitle())
        );
    }

    @Override
    protected String getScraperName() {
        return "Cuspide";
    }

    @Override
    protected String formatAuthor(String name, String surname) {
        return String.format("%s, %s", surname.toUpperCase(), name.toUpperCase());
    }

    @Override
    protected BookDTO createBook(Element element, String author, String link) {
        String title = element.select("h1[class^=product-title]")
                .first()
                .text();
        String priceArs = element.select("div[class=price-wrapper] > p > span > bdi")
                .first()
                .text();

        return new BookDTO(title, author, link, priceArs);
    }
}
