
import java.util.Scanner;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class ElGamal {
    private BigInteger p; // prime modulus
    private BigInteger g; // generator
    private BigInteger d; // private key
    private BigInteger e1; // public key part 1
    private BigInteger e2; // public key part 2
    private final SecureRandom random = new SecureRandom();
    private static final int BIT_LENGTH = 64; // Adjust this for larger/smaller primes

    // Constructor to automatically generate prime and primitive root
    public ElGamal() {
        generatePrimeAndRoot();
        generateKeys();
    }

    // Generate a prime number and find its primitive root
    private void generatePrimeAndRoot() {
        // Generate a prime number p where p-1 = 2q (q is prime)
        // This ensures we can easily find primitive roots
        while (true) {
            BigInteger q = BigInteger.probablePrime(BIT_LENGTH - 1, random);
            p = q.multiply(BigInteger.TWO).add(BigInteger.ONE);
            if (p.isProbablePrime(50)) {
                break;
            }
        }

        // Find primitive root
        g = findPrimitiveRoot(p);
    }

    // Find a primitive root modulo p
    private BigInteger findPrimitiveRoot(BigInteger p) {
        BigInteger pMinus1 = p.subtract(BigInteger.ONE);
        List<BigInteger> factors = new ArrayList<>();
        
        // We know p-1 = 2q where q is prime
        factors.add(BigInteger.TWO);
        factors.add(pMinus1.divide(BigInteger.TWO));

        // Test random values until we find a primitive root
        while (true) {
            BigInteger a = new BigInteger(p.bitLength() - 1, random);
            if (a.compareTo(BigInteger.ONE) <= 0 || a.compareTo(p) >= 0) {
                continue;
            }

            boolean isPrimitiveRoot = true;
            for (BigInteger factor : factors) {
                BigInteger exp = pMinus1.divide(factor);
                if (a.modPow(exp, p).equals(BigInteger.ONE)) {
                    isPrimitiveRoot = false;
                    break;
                }
            }

            if (isPrimitiveRoot) {
                return a;
            }
        }
    }

    // Generate public and private keys
    private void generateKeys() {
        // Generate private key d (1 < d < p-1)
        d = new BigInteger(p.bitLength() - 1, random);
        while (d.compareTo(BigInteger.ONE) <= 0 || d.compareTo(p.subtract(BigInteger.ONE)) >= 0) {
            d = new BigInteger(p.bitLength() - 1, random);
        }

        // Calculate e1 = g^d mod p
        e1 = g.modPow(d, p);
        e2 = null; // e2 will be calculated during encryption
    }

    // Encryption
    public BigInteger[] encrypt(BigInteger message) {
        // Generate random r (1 < r < p-1)
        BigInteger r = new BigInteger(p.bitLength() - 1, random);
        while (r.compareTo(BigInteger.ONE) <= 0 || r.compareTo(p.subtract(BigInteger.ONE)) >= 0) {
            r = new BigInteger(p.bitLength() - 1, random);
        }

        // Calculate c1 = g^r mod p
        BigInteger c1 = g.modPow(r, p);

        // Calculate e2 = e1^r mod p (store it for display)
        e2 = e1.modPow(r, p);
        
        // Calculate c2 = m * e2 mod p
        BigInteger c2 = e2.multiply(message).mod(p);

        return new BigInteger[]{c1, c2};
    }

    // Decryption
    public BigInteger decrypt(BigInteger[] ciphertext) {
        BigInteger c1 = ciphertext[0];
        BigInteger c2 = ciphertext[1];

        // Calculate s = c1^d mod p
        BigInteger s = c1.modPow(d, p);

        // Calculate s^(-1) mod p
        BigInteger sInverse = s.modInverse(p);

        // Calculate m = c2 * s^(-1) mod p
        return c2.multiply(sInverse).mod(p);
    }

    // Convert string to number
    private static BigInteger stringToBigInteger(String message) {
        StringBuilder binary = new StringBuilder();
        byte[] bytes = message.getBytes();
        for (byte b : bytes) {
            binary.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return new BigInteger(binary.toString(), 2);
    }

    // Convert number back to string
    private static String bigIntegerToString(BigInteger number) {
        String binary = number.toString(2);
        // Pad with zeros if necessary
        while (binary.length() % 8 != 0) {
            binary = "0" + binary;
        }
        
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            String chunk = binary.substring(i, Math.min(i + 8, binary.length()));
            message.append((char) Integer.parseInt(chunk, 2));
        }
        return message.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ElGamal elGamal = new ElGamal();

        System.out.println("Generated Prime (p): " + elGamal.p);
        System.out.println("Generator (g): " + elGamal.g);
        System.out.println("Public Key (e1): " + elGamal.e1);
        System.out.println("Private Key (d): " + elGamal.d);

        System.out.println("\nEnter message to encrypt:");
        String messageStr = scanner.nextLine();
        BigInteger message = stringToBigInteger(messageStr);

        // Encryption
        BigInteger[] ciphertext = elGamal.encrypt(message);
        System.out.println("\nEncrypted:");
        System.out.println("e2 = " + elGamal.e2);  // Display e2 after encryption
        System.out.println("c1 = " + ciphertext[0]);
        System.out.println("c2 = " + ciphertext[1]);

        // Decryption
        BigInteger decryptedMessage = elGamal.decrypt(ciphertext);
        String decryptedText = bigIntegerToString(decryptedMessage);
        System.out.println("\nDecrypted message: " + decryptedText);

        scanner.close();
    }
} 
