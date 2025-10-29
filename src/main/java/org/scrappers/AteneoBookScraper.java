package org.scrappers;

import java.util.concurrent.TimeUnit;

import org.dto.BookDTO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class AteneoBookScraper {

    public static ArrayList<BookDTO> getBooks(String author, String title) throws Exception {
        ArrayList<BookDTO> books = new ArrayList<>();
        int page = 1;
        Elements elements;
        do {
            String url = String.format("https://www.yenny-elateneo.com/search/page/%d/?q=%s&results_only=true&limit=12", page, urlSearchParameters(title));
            Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET).execute();
            Document document = Jsoup.parse(response.body());
            TimeUnit.SECONDS.sleep(2);
            elements = document.select("div[class^=js-item-description]");
            for (Element element : elements) {
                books.add(createBook(element, author, element.select("a[href]").first().attr("href")));
            }
            page++;
        } while (elements.size() == 12);
        return books;
    }

    private static BookDTO createBook(Element element, String author, String link) {
        String title = element.select("a[href] > div")
                .first()
                .text();
        String priceArs = element.select("div[class=item-price-container] > span:not([style])")
                .first()
                .text();

        return new BookDTO(title, author, link, priceArs);
    }

    private static String urlSearchParameters(String parameters) {
        return parameters.replaceAll(" ", "+");
    }
}
