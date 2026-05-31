import java.util.*;
import java.io.*;
import java.nio.file.*;

public class TextSteganography {
    private static final String ENCODING = "UTF-8";

    // a) Accept cover file and secret message
    public static String[] inputData() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter cover file path: ");
        String coverFile = sc.nextLine();
        System.out.print("Enter secret message: ");
        String secret = sc.nextLine();
        return new String[]{coverFile, secret};
    }

    // b) Encode & hide message using space character steganography
    public static String encode(String coverText, String secret) {
        if (secret.isEmpty()) return coverText;

        StringBuilder binarySecret = new StringBuilder();
        for (char c : secret.toCharArray()) {
            binarySecret.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }

        String binaryWithLength = String.format("%16s", Integer.toBinaryString(secret.length()))
                                   .replace(' ', '0') + binarySecret;

        StringBuilder encoded = new StringBuilder(coverText);
        int bitIndex = 0;

        for (int i = 0; i < encoded.length() && bitIndex < binaryWithLength.length(); i++) {
            if (encoded.charAt(i) == ' ') {
                encoded.setCharAt(i, binaryWithLength.charAt(bitIndex++) == '1' ? '\u00A0' : ' ');
            }
        }

        return encoded.toString();
    }

    // c) Save encoded output
    public static void saveFile(String content, String outputPath) {
        try (FileWriter fw = new FileWriter(outputPath)) {
            fw.write(content);
            System.out.println("✓ Encoded file saved: " + outputPath);
        } catch (IOException e) {
            System.out.println("✗ Save error: " + e.getMessage());
        }
    }

    // d) Retrieve hidden message
    public static String decode(String encodedText) {
        StringBuilder binaryData = new StringBuilder();

        for (char c : encodedText.toCharArray()) {
            if (c == ' ' || c == '\u00A0') {
                binaryData.append(c == '\u00A0' ? '1' : '0');
            }
        }

        if (binaryData.length() < 16) return "";

        String lengthBinary = binaryData.substring(0, 16);
        int msgLength = Integer.parseInt(lengthBinary, 2);

        if (msgLength == 0 || binaryData.length() < 16 + (msgLength * 8)) return "";

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < msgLength; i++) {
            int start = 16 + (i * 8);
            String byteStr = binaryData.substring(start, start + 8);
            message.append((char) Integer.parseInt(byteStr, 2));
        }

        return message.toString();
    }

    // e) Display cover text (unchanged visually)
    public static void displayCover(String text) {
        System.out.println("\n=== Cover Text (Visually Unchanged) ===");
        System.out.println(text.substring(0, Math.min(200, text.length())) + 
                          (text.length() > 200 ? "..." : ""));
    }

    // f) Main - Test with multiple inputs
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Number of test cases: ");
        int tests = sc.nextInt();
        sc.nextLine();

        for (int t = 1; t <= tests; t++) {
            System.out.println("\n" + "=".repeat(40) + 
                             " TEST " + t + " " + "=".repeat(40));

            String[] input = inputData();
            String coverFile = input[0], secret = input[1];

            String coverText = "";
            try {
                coverText = new String(Files.readAllBytes(Paths.get(coverFile)), ENCODING);
            } catch (IOException e) {
                System.out.println("✗ Cannot read file: " + e.getMessage());
                continue;
            }

            String encoded = encode(coverText, secret);
            String outputFile = "encoded_" + t + ".txt";
            saveFile(encoded, outputFile);

            displayCover(coverText);

            String decoded = decode(encoded);
            System.out.println("\n=== Hidden Message Retrieved ===");
            System.out.println("Original: \"" + secret + "\"");
            System.out.println("Retrieved: \"" + decoded + "\"");
            System.out.println("Status: " + (secret.equals(decoded) ? "✓ SUCCESS" : "✗ FAILED"));
        }

        System.out.println("\n✓ Steganography System Complete!");
    }
}
