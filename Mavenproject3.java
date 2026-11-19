package com.mycompany.mavenproject3;

import java.util.Locale;
import java.util.Scanner;

public class Mavenproject3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        final String CORRECT_PIN = "1234";
        final int MAX_ATTEMPTS = 3;
        int attempts = 0;
        boolean authenticated = false;

        // 1. PIN authentication loop (up to 3 attempts)
        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Enter your 4-digit PIN: ");
            String enteredPin = scanner.nextLine().trim();
            attempts++;

            if (enteredPin.equals(CORRECT_PIN)) {
                authenticated = true;
                System.out.println("PIN verified. Login successful!");
                break;
            } else {
                int remaining = MAX_ATTEMPTS - attempts;
                if (remaining > 0) {
                    System.out.println("Incorrect PIN. Attempts remaining: " + remaining);
                } else {
                    System.out.println("Incorrect PIN. Maximum attempts (3) reached. Card locked.");
                }
            }
        }

        if (!authenticated) {
            scanner.close();
            return;
        }

        double balance = 1000.00;
        String[] transactions = new String[100];
        int transactionCount = 0;
        int choice = 0;

        // 2. Main menu loop (1 to 5)
        while (choice != 5) {
            System.out.println("\n=== ATM MAIN MENU ===");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. View Transactions");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid option. Please enter a number from 1 to 5.");
                scanner.next();
                continue;
            }
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.printf("Current Balance: $%.2f%n", balance);
                    break;

                case 2:
                    System.out.print("Enter deposit amount: ");
                    if (!scanner.hasNextDouble()) {
                        System.out.println("Invalid amount. Please enter a valid number.");
                        scanner.next();
                        break;
                    }
                    double depositAmount = scanner.nextDouble();
                    if (Double.isNaN(depositAmount) || Double.isInfinite(depositAmount) || depositAmount <= 0) {
                        System.out.println("Invalid amount. Please enter a positive number.");
                    } else {
                        balance += depositAmount;
                        String record = String.format(Locale.US, "Deposited: $%.2f | Balance: $%.2f", depositAmount, balance);
                        if (transactionCount < transactions.length) {
                            transactions[transactionCount] = record;
                            transactionCount++;
                        } else {
                            System.out.println("Note: Transaction history limit (100) reached; balance was still updated.");
                        }
                        System.out.printf("Deposit successful. New balance: $%.2f%n", balance);
                    }
                    break;

                case 3:
                    System.out.print("Enter withdrawal amount: ");
                    if (!scanner.hasNextDouble()) {
                        System.out.println("Invalid amount. Please enter a valid number.");
                        scanner.next();
                        break;
                    }
                    double withdrawAmount = scanner.nextDouble();
                    if (Double.isNaN(withdrawAmount) || Double.isInfinite(withdrawAmount) || withdrawAmount <= 0) {
                        System.out.println("Invalid amount. Please enter a positive number.");
                    } else if (withdrawAmount > balance) {
                        System.out.println("Error: Insufficient balance.");
                    } else {
                        balance -= withdrawAmount;
                        String record = String.format(Locale.US, "Withdrew: $%.2f | Balance: $%.2f", withdrawAmount, balance);
                        if (transactionCount < transactions.length) {
                            transactions[transactionCount] = record;
                            transactionCount++;
                        } else {
                            System.out.println("Note: Transaction history limit (100) reached; balance was still updated.");
                        }
                        System.out.printf("Withdrawal successful. New balance: $%.2f%n", balance);
                    }
                    break;

                case 4:
                    System.out.println("\n--- Transaction History ---");
                    if (transactionCount == 0) {
                        System.out.println("No transactions recorded yet.");
                    } else {
                        for (int i = 0; i < transactionCount; i++) {
                            System.out.println((i + 1) + ". " + transactions[i]);
                        }
                    }
                    break;

                case 5:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please choose between 1 and 5.");
                    break;
            }
        }

        scanner.close();
    }
}