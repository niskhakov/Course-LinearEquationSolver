import java.util.Locale;
import java.util.Scanner;

public class TwoVariablesEquationSolver {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        double a = scanner.nextDouble();
        double b = scanner.nextDouble();
        double c = scanner.nextDouble();

        double d = scanner.nextDouble();
        double e = scanner.nextDouble();
        double f = scanner.nextDouble();

        double x, y;

        y = (f - c * d/a)/(e - b * d /a);
        x = (c - b * y) /a;

        System.out.println(x + " " + y);
    }
}
