package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        try {
            int page = 1;
            boolean lastPage;
            Elements books = new Elements();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the author name and/or surname: ");
            String author = scanner.nextLine();
            author = author.trim().replaceAll(" ", "+");
            scanner.close();
            do {
                String url = String.format("https://cuspide.com/page/%d/?s=%s&post_type=product", page, author);
                Document document = Jsoup.connect(url).get();
                Elements elements = document.getElementsByClass("product-small box ");
                books.addAll(elements);
                lastPage = document.select("i[class$=icon-angle-right]").isEmpty();
                page++;
                TimeUnit.SECONDS.sleep(3);
            } while (! lastPage);

            try (FileWriter fileWriter = (new FileWriter("books.csv"))) {
                fileWriter.write("Title,Author,Price\n");
                for (Element element : books) {
                    Element title = element.select("div[class$=title-wrapper]").first();
                    Element link = title.select("a[href]").first();
                    Element price = element.select("span[class$=price]").first();
                    fileWriter.write(link.text()+","+price.text()+","+link.attr("href")+"\n");
                }
            } catch (IOException e) {
                System.out.println("Error writing to CSV file: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}