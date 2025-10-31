package org.scrappers;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.dto.BookDTO;
import org.jsoup.Connection.Method;
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

    public ArrayList<BookDTO> getBooks(Input input) throws Exception {
        String authorFullName = this.formatAuthor(input.getAuthorName(), input.getAuthorSurname());
        String url = String.format(
                "https://cuspide.com/?s=%s&post_type=product", this.urlParameters(input.getFullTitle()));
        Response response = Jsoup.connect(url).method(Method.GET).execute();
        TimeUnit.SECONDS.sleep(2);
        Document document = Jsoup.parse(response.body());
        if (! document.select("body[class*=single-product]").isEmpty()) {
            Elements bookAuthor = document.select("span > a[href]");
            String author = (! bookAuthor.isEmpty())? bookAuthor.first().text() : "";
            this.books.add(createBook(document, author, response.url().toString()));
            return this.books;
        }

        int page = 1;
        boolean lastPage;
        do {
            Elements links = document.select("div[class^=product-small box] > div > div > a[href]");
            for (Element link : links) {
                Document doc = Jsoup.connect(link.attr("href")).get();
                Elements bookAuthor = doc.select("span > a[href]");
                if (! bookAuthor.isEmpty() && bookAuthor.first().text().matches(".*" + authorFullName + ".*")) {
                    this.books.add(createBook(doc, bookAuthor.first().text(), link.attr("href")));
                }
                TimeUnit.SECONDS.sleep(2);
            }
            lastPage = document.select("i[class$=icon-angle-right]").isEmpty();
            if (! lastPage) {
                page++;
                url = String.format(
                        "https://cuspide.com/page/%d/?s=%s&post_type=product",
                        page,
                        this.urlParameters(input.getFullTitle())
                );
                document = Jsoup.connect(url).get();
                TimeUnit.SECONDS.sleep(2);
            }
        } while (! lastPage);

        return this.books;
    }

    @Override
    protected String formatAuthor(String authorName, String authorSurname) {
        return String.format("%s, %s", authorSurname.toUpperCase(), authorName.toUpperCase());
    }

    private BookDTO createBook(Element element, String author, String link) {
        String title = element.select("h1[class^=product-title]")
                .first()
                .text();
        String priceArs = element.select("div[class=price-wrapper] > p > span > bdi")
                .first()
                .text();

        return new BookDTO(title, author, link, priceArs);
    }
}
