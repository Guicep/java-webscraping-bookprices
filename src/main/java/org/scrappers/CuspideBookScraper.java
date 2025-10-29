package org.scrappers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import org.dto.BookDTO;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CuspideBookScraper {

    public static ArrayList<BookDTO> getBooks(String author, String title) throws Exception {
        ArrayList<BookDTO> books = new ArrayList<>();
        String url = String.format("https://cuspide.com/?s=%s&post_type=product", urlSearchParameters(title));
        Response response = Jsoup.connect(url).method(Method.GET).execute();
        Document document = Jsoup.parse(response.body());
        TimeUnit.SECONDS.sleep(2);
        if (! document.select("body[class*=single-product]").isEmpty()) {
            books.add(createBook(document, author, response.url().toString()));
            return books;
        }

        int page = 1;
        boolean lastPage;
        do {
            Elements links = document.select("div[class^=product-small box] > div > div > a[href]");
            for (Element link : links) {
                Document doc = Jsoup.connect(link.attr("href")).get();
                Element bookAuthor = doc.select("span > a[href]").first();
                if (bookAuthor.hasText() && bookAuthor.text().matches(".*" + author + ".*")) {
                    books.add(createBook(doc, author, link.attr("href")));
                }
                TimeUnit.SECONDS.sleep(2);
            }
            lastPage = document.select("i[class$=icon-angle-right]").isEmpty();
            if (! lastPage) {
                page++;
                url = String.format("https://cuspide.com/page/%d/?s=%s&post_type=product", page, urlSearchParameters(title));
                document = Jsoup.connect(url).get();
                TimeUnit.SECONDS.sleep(2);
            }
        } while (! lastPage);
        // Esto seria trabajo del que lo escribe, CSVWriter, etc.
        books.sort(Comparator.comparingDouble(BookDTO::getPrice));

        return books;
    }

    private static BookDTO createBook(Element element, String author, String link) {
        String title = element.select("h1[class^=product-title]")
                .first()
                .text();
        String priceArs = element.select("div[class=price-wrapper] > p > span > bdi")
                .first()
                .text();

        return new BookDTO(title, author, link, priceArs);
    }

    private static String urlSearchParameters(String parameters) {
        return parameters.replaceAll(" ", "+");
    }
}
