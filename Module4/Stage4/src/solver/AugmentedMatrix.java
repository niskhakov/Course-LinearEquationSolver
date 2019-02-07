package solver;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AugmentedMatrix {
    private Row[] matrix;

    AugmentedMatrix(double[][] matrix) {
        setMatrix(matrix);
    }

    AugmentedMatrix() {}

    public void readMatrix(Scanner scanner) throws InputMismatchException {
        int n = scanner.nextInt();
        int m = n+1;
        matrix = new Row[n];
        for (int i = 0; i < n; i++) {
            matrix[i] = new Row(m);
            for (int j = 0; j < m; j++) {
                this.set(i, j, scanner.nextDouble());
            }
        }
    }

    public void set(int i, int j, double value) {
        matrix[i].set(j, value);
    }

    public double get(int i, int j) {
        return matrix[i].get(j);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < matrix.length; i++) {
            sb.append(Arrays.toString(matrix[i].getRow()));
            sb.append("\n");
        }
        return sb.toString();
    }


    public double[][] getMatrix() {
        double[][] copy = new double[matrix.length][];
        for(int i = 0; i < matrix.length; i++) {
            copy[i] = matrix[i].getRow();
        }
        return copy;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = new Row[matrix.length];
        for(int i = 0; i < matrix.length; i++) {
            this.matrix[i] = new Row(matrix[i]);
        }
    }

    public Row getRow(int index) {
        return this.matrix[index];
    }

    /**
     * Get dimensions of Augmented matrix
     * @return size()[0] - number of rows, size()[1] - number of columns
     */
    public int[] size() {
        int[] size = new int[2];
        size[0] = matrix.length;
        // Consider that all rows have the same size, according to the class initialization
        size[1] = matrix[0].size();
        return size;
    }


}
