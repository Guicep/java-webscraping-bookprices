package org.utils;

import org.dto.BookDTO;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSVBookWriter {

    public static void writeBooksToCSV(ArrayList<BookDTO> bookDTOS) {
        try (FileWriter fileWriter = (new FileWriter("books.csv"))) {
            fileWriter.write("Title,Price,Link\n");
            for (BookDTO bookDTO : bookDTOS) {
                fileWriter.write(bookDTO.getTitle()+","+ bookDTO.getPrice()+","+ bookDTO.getLink()+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
