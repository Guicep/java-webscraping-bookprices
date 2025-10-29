package org.main;

import org.scrappers.AteneoBookScraper;
import org.scrappers.CuspideBookScraper;
import org.slf4j.LoggerFactory;
import org.utils.CSVBookWriter;

import org.slf4j.Logger;
import org.utils.Input;
import org.utils.InputReader;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            Input input = InputReader.getInput();
            CSVBookWriter.writeBooksToCSV(
                    AteneoBookScraper.getBooks(input.getAuthorFullName(), input.getFullTitle())
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}