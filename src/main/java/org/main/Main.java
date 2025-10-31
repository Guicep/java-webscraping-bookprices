package org.main;

import org.dto.BookDTO;
import org.scrappers.AteneoBookScraper;
import org.scrappers.CuspideBookScraper;
import org.slf4j.LoggerFactory;
import org.utils.CSVBookWriter;

import org.slf4j.Logger;
import org.utils.Input;
import org.utils.InputReader;

import java.util.ArrayList;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            Input input = InputReader.getInput();
            ArrayList<BookDTO> books = new ArrayList<>();
            books.addAll(AteneoBookScraper.getBooks(input));
            books.addAll(CuspideBookScraper.getBooks(input));
            CSVBookWriter.writeBooksToCSV(books);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}