package com.student.gutenbergbookexplorer;

import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GutenbergBookExplorer {

    private static final int MAX_READING_LIST = 20;
    private static Book[] catalog = new Book[50];
    private static int catalogCount = 0;

    private static Book[] readingList = new Book[MAX_READING_LIST];
    private static int readingCount = 0;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        do {
            printMenu();
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine().trim();

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number from 0 to 7.\n");
                choice = -1;
                continue;
            }

            switch (choice) {
                case 1:
                    exploreBooks();
                    break;
                case 2:
                    System.out.print("Enter title keyword to search: ");
                    String titleKeyword = scanner.nextLine().trim();
                    if (titleKeyword.isEmpty()) {
                        System.out.println("Search keyword cannot be empty.\n");
                    } else {
                        searchBook(titleKeyword);
                    }
                    break;
                case 3:
                    System.out.print("Enter author keyword to search: ");
                    String authorKeyword = scanner.nextLine().trim();
                    if (authorKeyword.isEmpty()) {
                        System.out.println("Search keyword cannot be empty.\n");
                    } else {
                        searchBook(authorKeyword, true);
                    }
                    break;
                case 4:
                    addBook();
                    break;
                case 5:
                    viewReadingList();
                    break;
                case 6:
                    removeBook();
                    break;
                case 7:
                    displayBookDetails();
                    break;
                case 0:
                    System.out.println("Exiting Gutenberg Book Explorer. Goodbye, CEO Joel!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select an option from 0 to 7.\n");
                    break;
            }
        } while (choice != 0);
    }

    private static void printMenu() {
        System.out.println("===");
        System.out.println("GUTENBERG BOOK EXPLORER");
        System.out.println("1. Explore Books");
        System.out.println("2. Search by Title");
        System.out.println("3. Search by Author");
        System.out.println("4. Add Book to Reading List");
        System.out.println("5. View Reading List");
        System.out.println("6. Remove Book");
        System.out.println("7. Display Book Details");
        System.out.println("0. Exit");
    }

    // 1. Explore Books using JSoup (retrieves at least 10 books)
    static void exploreBooks() {
        System.out.println("\nConnecting to Project Gutenberg...");
        try {
            Document doc = Jsoup.connect("https://www.gutenberg.org/browse/scores/top")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .timeout(15000)
                    .get();

            Elements links = doc.select("a[href*=/ebooks/]");
            catalogCount = 0;

            for (Element link : links) {
                if (catalogCount >= catalog.length) {
                    break;
                }

                String rawText = link.text().trim();
                String href = link.absUrl("href").trim();

                if (rawText.isEmpty() || href.isEmpty()) {
                    continue;
                }
                if (rawText.equalsIgnoreCase("Top 100") || rawText.toLowerCase().startsWith("top ")) {
                    continue;
                }
                if (href.contains(".epub") || href.contains(".mobi") || href.contains(".html") || href.contains(".txt") || href.contains("#")) {
                    continue;
                }

                boolean duplicate = false;
                for (int i = 0; i < catalogCount; i++) {
                    if (catalog[i].getUrl().equalsIgnoreCase(href)) {
                        duplicate = true;
                        break;
                    }
                }
                if (duplicate) {
                    continue;
                }

                String cleaned = rawText.replaceAll("\\s*\\(\\d+\\)$", "").trim();
                String title = cleaned;
                String author = "Unknown Author";

                int byIndex = cleaned.lastIndexOf(" by ");
                if (byIndex != -1) {
                    title = cleaned.substring(0, byIndex).trim();
                    String parsedAuthor = cleaned.substring(byIndex + 4).trim();
                    if (!parsedAuthor.isEmpty()) {
                        author = parsedAuthor;
                    }
                }

                catalog[catalogCount] = new Book(title, author, href);
                catalogCount++;
            }

            if (catalogCount == 0) {
                System.out.println("Connected to Gutenberg, but 0 book links matched. Page title: " + doc.title() + "\n");
                return;
            }

            int displayLimit = Math.max(10, Math.min(15, catalogCount));
            System.out.println("\n--- Top Books from Project Gutenberg ---");
            for (int i = 0; i < displayLimit && i < catalogCount; i++) {
                System.out.println((i + 1) + ". " + catalog[i].getTitle() + " | Author: " + catalog[i].getAuthor());
            }
            System.out.println();

        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getClass().getSimpleName() + " - " + e.getMessage() + "\n");
        }
    }

    // Overloaded searchBook #1: Search by title
    static void searchBook(String title) {
        searchBook(title, false);
    }

    // Overloaded searchBook #2: Search by author when true, otherwise by title
    static void searchBook(String keyword, boolean searchByAuthor) {
        if (catalogCount == 0) {
            System.out.println("No books loaded yet. Please run '1. Explore Books' first.\n");
            return;
        }

        String lowerKeyword = keyword.toLowerCase();
        int matches = 0;

        System.out.println("\n--- Search Results (" + (searchByAuthor ? "Author" : "Title") + ": \"" + keyword + "\") ---");
        for (int i = 0; i < catalogCount; i++) {
            String target = searchByAuthor ? catalog[i].getAuthor() : catalog[i].getTitle();
            if (target.toLowerCase().contains(lowerKeyword)) {
                matches++;
                System.out.println("[" + (i + 1) + "] " + catalog[i].getTitle() + " — " + catalog[i].getAuthor());
            }
        }

        if (matches == 0) {
            System.out.println("No matching books found.\n");
        } else {
            System.out.println("Total matches: " + matches + "\n");
        }
    }

    // 4. Add a selected book to the fixed-size reading list array (max 20)
    static void addBook() {
        if (readingCount >= MAX_READING_LIST) {
            System.out.println("Reading list is full (maximum of " + MAX_READING_LIST + " books). Remove a book first.\n");
            return;
        }

        if (catalogCount == 0) {
            System.out.println("Catalog is empty. Please run '1. Explore Books' first.\n");
            return;
        }

        System.out.println("\n--- Available Books (1 to " + catalogCount + ") ---");
        for (int i = 0; i < catalogCount; i++) {
            System.out.println((i + 1) + ". " + catalog[i].getTitle() + " (" + catalog[i].getAuthor() + ")");
        }

        System.out.print("Enter the book number to add: ");
        String input = scanner.nextLine().trim();
        int index;
        try {
            index = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.\n");
            return;
        }

        if (index < 0 || index >= catalogCount) {
            System.out.println("Book number out of range.\n");
            return;
        }

        Book selected = catalog[index];
        for (int i = 0; i < readingCount; i++) {
            if (readingList[i].getUrl().equalsIgnoreCase(selected.getUrl())) {
                System.out.println("This book is already in your reading list.\n");
                return;
            }
        }

        readingList[readingCount] = selected;
        readingCount++;
        System.out.println("Added \"" + selected.getTitle() + "\" to your reading list (" + readingCount + "/" + MAX_READING_LIST + ").\n");
    }

    // 5. View all saved books using a for loop
    static void viewReadingList() {
        if (readingCount == 0) {
            System.out.println("Your reading list is currently empty.\n");
            return;
        }

        System.out.println("\n--- Personal Reading List (" + readingCount + "/" + MAX_READING_LIST + ") ---");
        for (int i = 0; i < readingCount; i++) {
            System.out.println((i + 1) + ". " + readingList[i].getTitle() + " — " + readingList[i].getAuthor());
        }
        System.out.println();
    }

    // 6. Remove a selected book from the reading list
    static void removeBook() {
        if (readingCount == 0) {
            System.out.println("Your reading list is empty. Nothing to remove.\n");
            return;
        }

        viewReadingList();
        System.out.print("Enter the number of the book to remove: ");
        String input = scanner.nextLine().trim();
        int index;
        try {
            index = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.\n");
            return;
        }

        if (index < 0 || index >= readingCount) {
            System.out.println("Invalid selection.\n");
            return;
        }

        String removedTitle = readingList[index].getTitle();
        for (int i = index; i < readingCount - 1; i++) {
            readingList[i] = readingList[i + 1];
        }
        readingList[readingCount - 1] = null;
        readingCount--;

        System.out.println("Removed \"" + removedTitle + "\" from your reading list.\n");
    }

    // 7. Display title, author, and Gutenberg URL
    static void displayBookDetails() {
        if (catalogCount == 0 && readingCount == 0) {
            System.out.println("No books available yet. Run '1. Explore Books' first.\n");
            return;
        }

        System.out.println("\nView details from:");
        System.out.println("1. Explored Gutenberg Books");
        System.out.println("2. My Reading List");
        System.out.print("Choose source: ");
        String sourceChoice = scanner.nextLine().trim();

        if (sourceChoice.equals("1")) {
            if (catalogCount == 0) {
                System.out.println("No explored books available.\n");
                return;
            }
            for (int i = 0; i < catalogCount; i++) {
                System.out.println((i + 1) + ". " + catalog[i].getTitle());
            }
            System.out.print("Enter book number: ");
            int idx = parseIndex(scanner.nextLine().trim(), catalogCount);
            if (idx != -1) {
                printSingleBookDetails(catalog[idx]);
            }
        } else if (sourceChoice.equals("2")) {
            if (readingCount == 0) {
                System.out.println("Your reading list is empty.\n");
                return;
            }
            for (int i = 0; i < readingCount; i++) {
                System.out.println((i + 1) + ". " + readingList[i].getTitle());
            }
            System.out.print("Enter book number: ");
            int idx = parseIndex(scanner.nextLine().trim(), readingCount);
            if (idx != -1) {
                printSingleBookDetails(readingList[idx]);
            }
        } else {
            System.out.println("Invalid source option.\n");
        }
    }

    private static int parseIndex(String input, int maxSize) {
        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx < 0 || idx >= maxSize) {
                System.out.println("Number out of range.\n");
                return -1;
            }
            return idx;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.\n");
            return -1;
        }
    }

    private static void printSingleBookDetails(Book book) {
        System.out.println("\n--- Book Details ---");
        System.out.println("Title : " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("URL   : " + book.getUrl());
        System.out.println();
    }
}