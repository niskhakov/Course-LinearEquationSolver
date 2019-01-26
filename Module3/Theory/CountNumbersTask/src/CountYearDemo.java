import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CountYearDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a file path to dataset: ");
        String path = scanner.nextLine();
        File file = new File(path);
        String[] lineArgs;
        long population;
        long previousPopulation;
        long largestDifferenceOfPopulation = 0;
        int year;
        int yearOfMaxDifference;
        try(Scanner fileScan = new Scanner(file)) {
            fileScan.nextLine();
            lineArgs = fileScan.nextLine().split("\\t");
            previousPopulation = Long.parseLong(lineArgs[1].replaceAll(",", ""));
            yearOfMaxDifference = Integer.parseInt(lineArgs[0]);
            while(fileScan.hasNextLine()) {
                lineArgs = fileScan.nextLine().split("\\t");
                population = Long.parseLong(lineArgs[1].replaceAll(",", ""));
                year = Integer.parseInt(lineArgs[0]);
                System.out.println(year + " : " + (population - previousPopulation));
                if((population - previousPopulation) > largestDifferenceOfPopulation) {
                    largestDifferenceOfPopulation = (population - previousPopulation);
                    yearOfMaxDifference = year;
                }
                previousPopulation = population;

            }
            System.out.println("Year : " + yearOfMaxDifference + "; maxDifference: " + largestDifferenceOfPopulation);
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist! ");
        }
    }
}
