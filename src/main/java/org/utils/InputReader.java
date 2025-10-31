package org.utils;

import java.util.Scanner;

public class InputReader {
    public static Input getInput() throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the author name: ");
        String authorName = scanner.nextLine();
        System.out.println("Enter the author surname: ");
        String authorSurname = scanner.nextLine();
        System.out.println("Enter the book title: ");
        String title = scanner.nextLine();
        scanner.close();

        String fullTitle = String.format("%s %s %s",title, authorName, authorSurname)
                .trim()
                .replaceAll("\\s+", " ");

        if (fullTitle.isEmpty()) {
            throw new Exception("Title field can't be empty");
        }

        return new Input(authorName, authorSurname, fullTitle);
    }
}
