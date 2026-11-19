import java.util.Scanner;

public class SmartRestaurantOrder{

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // 1. Declare variables and constant
        final double DRINK_PRICE = 15.50;

        String customerName;
        int age;
        double mealPrice;
        int numberOfDrinks;
        boolean isStudent;
        int rating;

        double drinksCost;
        double subtotal;
        double discountRate = 0;
        double discountAmount;
        double amountAfterDiscount;
        double tipRate = 0;
        double tipAmount;
        double finalBill;

        // 2. Collect customer information
        System.out.println("===== SMART RESTAURANT =====");

        System.out.print("Enter customer name: ");
        customerName = input.nextLine();

        System.out.print("Enter customer age: ");
        age = input.nextInt();

        System.out.print("Enter meal price: ");
        mealPrice = input.nextDouble();

        System.out.print("Enter number of drinks: ");
        numberOfDrinks = input.nextInt();

        System.out.print("Is the customer a student? (true/false): ");
        isStudent = input.nextBoolean();

        System.out.print("Enter service rating (1-5): ");
        rating = input.nextInt();

        // 3. Calculate drinks cost and subtotal
        drinksCost = numberOfDrinks * DRINK_PRICE;
        subtotal = mealPrice + drinksCost;

        // 4. Check discount eligibility
        if (isStudent && age >= 60) {
            discountRate = 0.10;
        } else if (isStudent) {
            discountRate = 0.10;
        } else if (age >= 60) {
            discountRate = 0.05;
        } else {
            discountRate = 0;
        }

        discountAmount = subtotal * discountRate;

        amountAfterDiscount = subtotal - discountAmount;

        // 5. Check service rating and calculate tip
        if (rating < 1 || rating > 5) {

            System.out.println("ERROR: Invalid service rating!");

        } else {

            if (rating >= 4) {
                tipRate = 0.10;
            } else {
                tipRate = 0;
            }

            tipAmount = amountAfterDiscount * tipRate;

            // 6. Calculate final bill
            finalBill = amountAfterDiscount + tipRate; // Note: tipAmount is calculated above

            // 7. Type casting
            int wholeAmount = (int) finalBill;

            // 8. Print receipt
            System.out.println("\n==============================");
            System.out.println("       SMART RESTAURANT");
            System.out.println("==============================");

            System.out.println("Customer: " + customerName);

            System.out.printf("Meal: $%.2f%n", mealPrice);

            System.out.printf("Drinks: $%.2f%n", drinksCost);

            System.out.printf("Discount: $%.2f%n", discountAmount);

            System.out.printf("Tip: $%.2f%n", tipAmount);

            System.out.printf("Final bill: $%.2f%n", finalBill);

            System.out.println("Whole amount: $" + wholeAmount);

            System.out.println("==============================");
            System.out.println("Thank you for visiting!");

        }

        input.close();
    }
}