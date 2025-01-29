
import java.util.Scanner;

public class VigenereCipher {
    private static final int ALPHABET_SIZE = 26;

    // Method to encrypt the text
    public String encrypt(String plaintext, String key) {
        StringBuilder result = new StringBuilder();
        plaintext = plaintext.toUpperCase();
        key = key.toUpperCase();
        
        for (int i = 0, j = 0; i < plaintext.length(); i++) {
            char currentChar = plaintext.charAt(i);
            
            if (Character.isLetter(currentChar)) {
                // Convert to 0-25 range
                int plainValue = currentChar - 'A';
                int keyValue = key.charAt(j % key.length()) - 'A';
                
                // Apply Vigenère encryption formula
                int encryptedValue = (plainValue + keyValue) % ALPHABET_SIZE;
                
                // Convert back to letter and append
                result.append((char) (encryptedValue + 'A'));
                j++;
            } else {
                // Keep non-alphabetic characters unchanged
                result.append(currentChar);
            }
        }
        
        return result.toString();
    }

    // Method to decrypt the text
    public String decrypt(String ciphertext, String key) {
        StringBuilder result = new StringBuilder();
        ciphertext = ciphertext.toUpperCase();
        key = key.toUpperCase();
        
        for (int i = 0, j = 0; i < ciphertext.length(); i++) {
            char currentChar = ciphertext.charAt(i);
            
            if (Character.isLetter(currentChar)) {
                // Convert to 0-25 range
                int cipherValue = currentChar - 'A';
                int keyValue = key.charAt(j % key.length()) - 'A';
                
                // Apply Vigenère decryption formula
                int decryptedValue = (cipherValue - keyValue + ALPHABET_SIZE) % ALPHABET_SIZE;
                
                // Convert back to letter and append
                result.append((char) (decryptedValue + 'A'));
                j++;
            } else {
                // Keep non-alphabetic characters unchanged
                result.append(currentChar);
            }
        }
        
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VigenereCipher cipher = new VigenereCipher();

        System.out.print("Enter the message to encrypt: ");
        String message = scanner.nextLine();

        System.out.print("Enter the key: ");
        String key = scanner.nextLine();

        // Remove spaces from key and validate
        key = key.replaceAll("\\s", "");
        if (key.isEmpty()) {
            System.out.println("Error: Key cannot be empty");
            scanner.close();
            return;
        }

        String encrypted = cipher.encrypt(message, key);
        System.out.println("Encrypted: " + encrypted);

        String decrypted = cipher.decrypt(encrypted, key);
        System.out.println("Decrypted: " + decrypted);

        scanner.close();
    }
} 
