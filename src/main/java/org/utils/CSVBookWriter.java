package org.utils;

import org.dto.BookDTO;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;

public class CSVBookWriter {

    public static void writeBooksToCSV(ArrayList<BookDTO> books) throws BookException {

        books.sort(Comparator.comparingDouble(BookDTO::getPrice));

        try (FileWriter fileWriter = (new FileWriter("books.csv"))) {
            fileWriter.write("Title,Author,Price,Link\n");
            for (BookDTO book : books) {
                fileWriter.write(book.toCSVString());
            }
        } catch (Exception e) {
            throw new BookException(e.getMessage());
        }
    }
}
