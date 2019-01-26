import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class CountTaskDemo {
    /* public */ static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a file path to dataset: ");
        String path = scanner.nextLine();
        System.out.print("Set lower limit: ");
        File file = new File(path);
        int count = 0;
        int limit = scanner.nextInt();
        try(Scanner fileScan = new Scanner(file)) {
            while(fileScan.hasNextInt()) {
                if(fileScan.nextInt() >= limit) {
                    count++;
                }
            }
            System.out.println("Count: " + count);
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist! ");
        }

    }
}
