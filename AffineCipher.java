
import java.util.Scanner;

public class AffineCipher {
    private final int a;
    private final int b;
    private static final int M = 26; // size of alphabet

    public AffineCipher(int a, int b) {
        if (gcd(a, M) != 1) {
            throw new IllegalArgumentException("'a' and " + M + " must be coprime. Choose another value for 'a'.");
        }
        this.a = a;
        this.b = b;
    }

    // Calculate Greatest Common Divisor
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Find modular multiplicative inverse
    private int modInverse(int a) {
        for (int i = 1; i < M; i++) {
            if ((a * i) % M == 1) {
                return i;
            }
        }
        throw new ArithmeticException("Modular multiplicative inverse does not exist");
    }

    public String encrypt(String text) {
        StringBuilder result = new StringBuilder();
        
        for (char c : text.toUpperCase().toCharArray()) {
            if (Character.isLetter(c)) {
                // Convert letter to number (A=0, B=1, etc)
                int x = c - 'A';
                // Apply affine function (ax + b) mod M
                int encrypted = (a * x + b) % M;
                // Convert number back to letter
                result.append((char) (encrypted + 'A'));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public String decrypt(String text) {
        StringBuilder result = new StringBuilder();
        int aInverse = modInverse(a);
        
        for (char c : text.toUpperCase().toCharArray()) {
            if (Character.isLetter(c)) {
                // Convert letter to number (A=0, B=1, etc)
                int y = c - 'A';
                // Apply inverse affine function a^-1(y - b) mod M
                int decrypted = (aInverse * (y - b + M)) % M;
                // Handle negative modulo
                if (decrypted < 0) {
                    decrypted += M;
                }
                // Convert number back to letter
                result.append((char) (decrypted + 'A'));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    // Example usage
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AffineCipher cipher = new AffineCipher(5, 8);
        
        System.out.print("Enter the message to encrypt: ");
        String message = scanner.nextLine();
        
        String encrypted = cipher.encrypt(message);
        System.out.println("Encrypted: " + encrypted);
        
        String decrypted = cipher.decrypt(encrypted);
        System.out.println("Decrypted: " + decrypted);
        
        scanner.close();
    }
} 
