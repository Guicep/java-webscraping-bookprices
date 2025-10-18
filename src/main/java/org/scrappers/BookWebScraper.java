package org.scrappers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import org.dto.BookDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BookWebScraper {

    public static ArrayList<BookDTO> getBooks(String authorFullName, String title) {
        // CONFIGURATION
        int page = 1;
        boolean lastPage;
        Elements books = new Elements();
        ArrayList<BookDTO> booksDTO = new ArrayList<>();

        try {
            do {
                String url = String.format("https://cuspide.com/page/%d/?s=%s&post_type=product", page, title);
                Document document = Jsoup.connect(url).get();
                TimeUnit.SECONDS.sleep(2);
                Elements elements = document.getElementsByClass("product-small box ");
                books.addAll(elements);
                page++;
                lastPage = document.select("i[class$=icon-angle-right]").isEmpty();
            } while (! lastPage);
        } catch(Exception e) {
            e.printStackTrace();
        }

        // STRUCTURE
        for (Element element : books) {
            Element data = element.select("div[class$=title-wrapper]").first();
            Element price = element.select("span[class$=price]").first();
            Element link = data.select("a[href]").first();
            Elements bookAuthor = element.select("p[class$=author-product-loop] > a[href]");
            if (! bookAuthor.isEmpty() && bookAuthor.first().text().matches(".*" + authorFullName + ".*")) {
                BookDTO bookDTO = new BookDTO(link.text(), link.attr("href"), price.text());
                booksDTO.add(bookDTO);
            }
        }
        booksDTO.sort(Comparator.comparingDouble(BookDTO::getPrice));

        return booksDTO;
    }
}
