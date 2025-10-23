package org.utils;

import org.dto.BookDTO;

import java.io.FileWriter;
import java.util.ArrayList;

public class CSVBookWriter {

    public static void writeBooksToCSV(ArrayList<BookDTO> bookDTOS) throws BookException {
        try (FileWriter fileWriter = (new FileWriter("books.csv"))) {
            fileWriter.write("Title,Author,Price,Link\n");
            for (BookDTO bookDTO : bookDTOS) {
                fileWriter.write(bookDTO.toCSVString());
            }
        } catch (Exception e) {
            throw new BookException(e.getMessage());
        }
    }
}
