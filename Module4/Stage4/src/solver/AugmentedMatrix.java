package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AugmentedMatrix {
    private Row[] matrix;

    // We don't always need swapHistory, so method which uses this field must check whether it is initialized or not.
    private ArrayList<SwapInfo> swapHistory;


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
        for (int i = 0; i < matrix.length; i++) {
            sb.append(Arrays.toString(matrix[i].getRow()));
            sb.append("\n");
        }
        return sb.toString();
    }

    public void swapRows(int firstRowIndex, int secondRowIndex) {
        Row temp = matrix[firstRowIndex];
        matrix[firstRowIndex] = matrix[secondRowIndex];
        matrix[secondRowIndex] = temp;
    }

    private void addSwapHistoryEntry(int prevIndex, int nextIndex) {
        // Initialization check
        if(swapHistory==null) {
            swapHistory = new ArrayList<>();
        }
        swapHistory.add(new SwapInfo(prevIndex, nextIndex));
    }

    public void swapColumns(int firstColumnIndex, int secondColumnIndex) {
        double coeff1, coeff2;
        for(Row row : matrix) {
            coeff1 = row.get(firstColumnIndex);
            coeff2 = row.get(secondColumnIndex);
            row.set(firstColumnIndex, coeff2);
            row.set(secondColumnIndex, coeff1);
        }

        addSwapHistoryEntry(firstColumnIndex, secondColumnIndex);
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
