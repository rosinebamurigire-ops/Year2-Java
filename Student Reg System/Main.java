/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.studentregsystem;

/**
 *
 * @author PC
 */
import java.util.Scanner;

public class Main {

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } else {
                System.out.println("Invalid input! Please enter a whole number.");
                scanner.nextLine();
            }
        }
    }

    private static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } else {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Student Academic Result System ===");
        System.out.print("Enter Registration Number: ");
        String regNumber = scanner.nextLine().trim();

        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine().trim();

        int age = readInt(scanner, "Enter Age: ");
        double mark1 = readDouble(scanner, "Enter Mark 1: ");
        double mark2 = readDouble(scanner, "Enter Mark 2: ");
        double mark3 = readDouble(scanner, "Enter Mark 3: ");

        Student student = new Student(regNumber, name, age, mark1, mark2, mark3);
        student.displayInformation();

        int choice;
        do {
            System.out.println("\n1. Display Information");
            System.out.println("2. Update Marks");
            System.out.println("3. Exit");
            choice = readInt(scanner, "Choose an option: ");

            switch (choice) {
                case 1:
                    student.displayInformation();
                    break;
                case 2:
                    student.setMark1(readDouble(scanner, "Enter new Mark 1: "));
                    student.setMark2(readDouble(scanner, "Enter new Mark 2: "));
                    student.setMark3(readDouble(scanner, "Enter new Mark 3: "));
                    System.out.println("Marks updated successfully!");
                    student.displayInformation();
                    break;
                case 3:
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1, 2, or 3.");
            }
        } while (choice != 3);

        scanner.close();
    }
}