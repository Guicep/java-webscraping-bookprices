package org.main;

import org.scrappers.BookWebScraper;
import org.utils.CSVBookWriter;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // INPUT
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the author name: ");
        String authorName = scanner.nextLine();
        System.out.println("Enter the author surname: ");
        String authorSurname = scanner.nextLine();
        System.out.println("Enter the book title: ");
        String title = scanner.nextLine();
        scanner.close();

        try {
            title = String.format("%s %s %s",title, authorName, authorSurname)
                    .trim()
                    .replaceAll("\\s+", " ")
                    .replaceAll(" ", "+");
            if (title.isEmpty()) {
                throw new Exception("No fields were filled");
            }
            String authorFullName = String.format("%s, %s", authorSurname.toUpperCase(), authorName.toUpperCase());
            CSVBookWriter.writeBooksToCSV(BookWebScraper.getBooks(authorFullName, title));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}