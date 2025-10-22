package org.dto;

public class BookDTO {
    private String title;
    private String link;
    private String author;
    private double price;

    public BookDTO(String title, String author, String link, String price) {
        this.title = title;
        this.link = link;
        this.author = String.format("\"%s\"",author);
        this.price = this.transformPrice(price);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLink() {
        return link;
    }

    public double getPrice() {
        return price;
    }

    private double transformPrice(String price) {
        String formatedPrice = price
                .replace("$", "")
                .replace(".", "")
                .replace(",", ".");
        return Double.parseDouble(formatedPrice);
    }

    public String toString() {
        return this.title + " " + this.price + " " + this.link;
    }

    public String toCSVString() {
        return this.title + "," + this.author + "," + this.price + "," + this.link + "\n";
    }
}
