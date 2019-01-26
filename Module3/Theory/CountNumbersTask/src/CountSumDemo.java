import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class CountSumDemo {
    /* public */ static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a file path to dataset: ");
        String path = scanner.nextLine();
        File file = new File(path);
        int sum = 0;
        try(Scanner fileScan = new Scanner(file)) {
            while(fileScan.hasNextInt()) {
                sum += fileScan.nextInt();
            }
            System.out.println("Sum: " + sum);
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist! ");
        }
    }
}
