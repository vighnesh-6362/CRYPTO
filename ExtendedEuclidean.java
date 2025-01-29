
import java.util.Scanner;

public class ExtendedEuclidean {
    // Class to store the results
    static class Result {
        int gcd;    // Greatest Common Divisor
        int x;      // Coefficient x
        int y;      // Coefficient y

        Result(int gcd, int x, int y) {
            this.gcd = gcd;
            this.x = x;
            this.y = y;
        }
    }

    // Extended Euclidean Algorithm implementation
    public static Result extendedEuclidean(int a, int b) {
        // Base case
        if (b == 0) {
            return new Result(a, 1, 0);
        }

        // Recursive call
        Result result = extendedEuclidean(b, a % b);

        // Update x and y using results of recursive call
        int x = result.y;
        int y = result.x - (a / b) * result.y;

        return new Result(result.gcd, x, y);
    }

    // Calculate multiplicative inverse
    public static Integer findMultiplicativeInverse(int a, int m) {
        Result result = extendedEuclidean(a, m);
        
        // If GCD is not 1, multiplicative inverse doesn't exist
        if (result.gcd != 1) {
            return null;
        }
        
        // Make sure the inverse is positive
        int inverse = result.x % m;
        if (inverse < 0) {
            inverse += m;
        }
        
        return inverse;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first number (a): ");
        int a = scanner.nextInt();

        System.out.print("Enter second number (b): ");
        int b = scanner.nextInt();

        Result result = extendedEuclidean(a, b);

        System.out.println("\nResults:");
        System.out.println("GCD(" + a + ", " + b + ") = " + result.gcd);
        System.out.println("Coefficients (x, y) in Bézout's identity:");
        System.out.println("x = " + result.x);
        System.out.println("y = " + result.y);
        System.out.println("\nVerification:");
        System.out.println(a + "(" + result.x + ") + " + b + "(" + result.y + ") = " + result.gcd);
        System.out.println(a * result.x + b * result.y + " = " + result.gcd);

        // Calculate and display multiplicative inverse
        Integer multiplicativeInverse = findMultiplicativeInverse(a, b);
        System.out.println("\nMultiplicative Inverse:");
        if (multiplicativeInverse != null) {
            System.out.println("The multiplicative inverse of " + a + " modulo " + b + " is: " + multiplicativeInverse);
            // Verify the result
            System.out.println("Verification: " + a + " * " + multiplicativeInverse + " ≡ " + 
                             (a * multiplicativeInverse) + " ≡ 1 (mod " + b + ")");
        } else {
            System.out.println("Multiplicative inverse does not exist because GCD(" + a + ", " + b + ") ≠ 1");
        }

        scanner.close();
    }
} 
