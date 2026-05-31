import java.util.Scanner;

/**
 * Smart Password Generator - Innovative Project
 * Generates secure passwords using user-specific information and evaluates password strength
 */
public class SmartPasswordGenerator {

    // Instance variables to store user details
    private String name;
    private String pan;
    private String dateOfBirth;
    private String generatedPassword;

    // Scanner for user input
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Method to accept user details (Requirement a)
     */
    public void acceptUserDetails() {
        System.out.println("\n=== Enter User Details ===");

        System.out.print("Enter Name: ");
        this.name = scanner.nextLine().trim();

        System.out.print("Enter PAN Number: ");
        this.pan = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter Date of Birth (DD/MM/YYYY): ");
        this.dateOfBirth = scanner.nextLine().trim();
    }

    /**
     * Method to generate a secure password using user data (Requirement b)
     */
    public void generatePassword() {
        String namePart = "";
        if (name.length() >= 2) {
            int length = Math.min(3, name.length());
            namePart = name.substring(0, length);
            namePart = Character.toUpperCase(namePart.charAt(0)) + 
                      namePart.substring(1).toLowerCase();
        }

        String panPart = pan.length() >= 3 ? pan.substring(pan.length() - 3) : pan;

        String yearPart = "";
        if (dateOfBirth.contains("/")) {
            String[] parts = dateOfBirth.split("/");
            if (parts.length == 3 && parts[2].length() == 4) {
                yearPart = parts[2];
            }
        }

        int randomNum1 = (int)(Math.random() * 90) + 10;
        int randomNum2 = (int)(Math.random() * 4) + 1;

        char specialChar = getSpecialChar(pan.charAt(0));

        StringBuilder passwordBuilder = new StringBuilder();
        passwordBuilder.append(namePart);
        passwordBuilder.append(specialChar);
        passwordBuilder.append(panPart);
        if (!yearPart.isEmpty()) {
            passwordBuilder.append(yearPart);
        }
        passwordBuilder.append(randomNum1);
        passwordBuilder.append(specialChar);
        passwordBuilder.append(randomNum2);

        this.generatedPassword = passwordBuilder.toString();
    }

    private char getSpecialChar(char input) {
        char[] specialChars = {'@', '#', '$', '%', '&', '*'};
        int index = Character.toLowerCase(input) % specialChars.length;
        return specialChars[index];
    }

    public void displayPassword() {
        System.out.println("\n=== Generated Password ===");
        System.out.println("Name: " + name);
        System.out.println("PAN: " + pan);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Password: " + generatedPassword);
        System.out.println("Password Length: " + generatedPassword.length() + " characters");
    }

    public String classifyPasswordStrength() {
        if (generatedPassword == null || generatedPassword.isEmpty()) {
            return "Weak";
        }

        int length = generatedPassword.length();
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        for (char c : generatedPassword.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpperCase = true;
            else if (Character.isLowerCase(c)) hasLowerCase = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecialChar = true;
        }

        if (length >= 12 && hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar) {
            return "Strong";
        } else if (length >= 8 && length <= 11 && hasSpecialChar && hasNumber && 
                 (hasUpperCase || hasLowerCase)) {
            return "Medium";
        } else {
            return "Weak";
        }
    }

    public void displayPasswordStrength() {
        String strength = classifyPasswordStrength();
        System.out.println("\n=== Password Strength ===");
        System.out.println("Strength: " + strength);

        switch (strength) {
            case "Strong":
                System.out.println("✓ Excellent! Your password is very secure.");
                break;
            case "Medium":
                System.out.println("⚠ Your password is moderately secure.");
                break;
            case "Weak":
                System.out.println("✗ Your password is weak. Make it longer with more variety.");
                break;
        }
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("    SMART PASSWORD GENERATOR - INNOVATIVE PROJECT");
        System.out.println("=================================================");

        int numberOfUsers;

        System.out.print("\nHow many users do you want to test? ");
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        numberOfUsers = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= numberOfUsers; i++) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("         USER #" + i);
            System.out.println("=".repeat(50));

            SmartPasswordGenerator generator = new SmartPasswordGenerator();

            generator.acceptUserDetails();
            generator.generatePassword();
            generator.displayPassword();
            generator.displayPasswordStrength();

            System.out.println("=".repeat(50));
        }

        System.out.println("\n=================================================");
        System.out.println("    ALL USERS PROCESSED SUCCESSFULLY!");
        System.out.println("=================================================");

        scanner.close();
    }
}
