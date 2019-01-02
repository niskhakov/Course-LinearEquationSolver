import java.util.Locale;
import java.util.Scanner;

public class SimpleEquationSolver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        double a = scanner.nextDouble();
        double b = scanner.nextDouble();

        double x = b / a;
        System.out.println(x);
    }
}
