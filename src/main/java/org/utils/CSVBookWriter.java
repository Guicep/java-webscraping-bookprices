package org.utils;

import org.dto.BookDTO;

import java.io.FileWriter;
import java.util.ArrayList;

public class CSVBookWriter {

    public static void writeBooksToCSV(ArrayList<BookDTO> bookDTOS) {
        try (FileWriter fileWriter = (new FileWriter("books.csv"))) {
            fileWriter.write("Title,Author,Price,Link\n");
            for (BookDTO bookDTO : bookDTOS) {
                fileWriter.write(
                        bookDTO.getTitle()
                                +","+ bookDTO.getAuthor()
                                +","+ bookDTO.getPrice()
                                +","+ bookDTO.getLink()
                                +"\n"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
