package org.main;

import org.dto.BookDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.utils.CSVBookWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        try {
            int page = 1;
            boolean lastPage;
            Elements books = new Elements();
            Scanner scanner = new Scanner(System.in);
            ArrayList<BookDTO> booksDTO = new ArrayList<>();
            System.out.println("Enter the author name and/or surname: ");
            String author = scanner.nextLine();
            author = author.trim().replaceAll(" ", "+");
            scanner.close();
            do {
                String url = String.format("https://cuspide.com/page/%d/?s=%s&post_type=product", page, author);
                Document document = Jsoup.connect(url).get();
                TimeUnit.SECONDS.sleep(3);
                Elements elements = document.getElementsByClass("product-small box ");
                books.addAll(elements);
                page++;
                lastPage = document.select("i[class$=icon-angle-right]").isEmpty();
            } while (! lastPage);

            for (Element element : books) {
                Element data = element.select("div[class$=title-wrapper]").first();
                Element price = element.select("span[class$=price]").first();
                Element link = data.select("a[href]").first();
                BookDTO bookDTO = new BookDTO(link.text(), link.attr("href"), price.text());
                booksDTO.add(bookDTO);
            }

            booksDTO.sort(Comparator.comparingDouble(BookDTO::getPrice));
            CSVBookWriter.writeBooksToCSV(booksDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}