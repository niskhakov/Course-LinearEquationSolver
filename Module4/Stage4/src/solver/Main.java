package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    static String getHelp() {
        String helpMsg = "This program is intended to solve Linear Equations with any amount of variables using Gauss-Jordan Elimination. \n\n" +
                "Options: " +
                "\n\t * -in [pathToFile] reads file with the first number - number of equations and variables, and augmented matrix of linear equation" +
                "\n\t * -out [pathToFile] writes results to the output file " +
                "\n\t * -h shows help information " +
                "\n\nExample: \njava Solver -in in.txt -out out.txt" +
                "\n\nExample of in.txt:\n" +
                "3\n" +
                "1 1 2 9\n" +
                "2 4 -3 1\n" +
                "3 6 -5 0";
        return helpMsg;
    }

    /**
     * Program should start with necessary options -in [pathToFileWithMatrix] and -out [pathToOutputFile]
     * Example: > java Solver -in in.txt -out out.txt
     * @param args
     */
    public static void main(String[] args) {
        // Getting and handling console parameters
        String inputFile="";
        String outputFile="";
        for(int i = 0; i < args.length; i++) {
            try {
                switch (args[i]) {
                    case "-in":
                        inputFile = args[i + 1];
                        break;
                    case "-out":
                        outputFile = args[i + 1];
                        break;
                    case "-h" :
                        System.out.println(getHelp());
                        return;

                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Invalid command line options, use '-h' option to see help");
                return;
            }
        }

        if(inputFile.isEmpty() || outputFile.isEmpty()) {
            System.err.println("Invalid command line options, use '-h' option to see help");
            return;
        }

        System.out.println("Input File: " + inputFile + "\nOutput File: " + outputFile);

        // Working with input file
        File file = new File(inputFile);
        AugmentedMatrix matrix = new AugmentedMatrix();
        try(Scanner fileScanner = new Scanner(file);) {
            matrix.readMatrix(fileScanner);
        } catch(FileNotFoundException e) {
            System.out.println("File: " + file.getAbsolutePath() + " doesn't exist");
            return;
        } catch (InputMismatchException e) {
            System.err.println("Invalid structure of input file: " + file.getAbsolutePath());
            return;
        }

        if(matrix.size()[0] == 0 || matrix.size()[1] == 0) {
            System.err.println("Invalid structure of input file: " + file.getAbsolutePath());
            return;
        }

        // Input matrix output
        System.out.println("Input matrix:");
        System.out.println(matrix);


        // Solving equation
        System.out.println("Start solving linear equation.");
        LinearEquationSolver solver = new LinearEquationSolver(matrix);
        String res = solver.solve().getResult();
        System.out.println("Rows manipulation:");
        System.out.println(solver.getLogs());

        System.out.println("Resulting matrix:");
        System.out.println(solver.getMatrix());

        System.out.println("The solution is: (" + res.replaceAll("\n", ", ") + ")");

        // Output writing
        try(FileWriter fileWriter = new FileWriter(outputFile)) {
            fileWriter.write(res);
            System.out.println("Saved to " + outputFile);
        } catch(IOException e) {
            System.err.println("Result cannot be written to the output file, please try other output file");
        }
    }
}