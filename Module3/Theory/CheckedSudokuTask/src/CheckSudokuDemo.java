import java.util.Arrays;
import java.util.Scanner;

class CheckSudokuDemo {

    static boolean checkSortedArray(int[] row, int n) {
        for(int i = 0; i < n*n; i++) {
            if(row[i] != (i+1)) {
                return false;
            }
        }
        return true;
    }

   static boolean checkRows(int[][] matrix, int n) {
        int[] row;
        for(int i = 0; i < n*n; i++) {
            row = Arrays.copyOf(matrix[i], n*n);
            Arrays.sort(row);
            if(!checkSortedArray(row, n))
                return false;

        }
        return true;
    }

    static boolean checkColumns(int[][] matrix, int n) {
        int[] column = new int[n*n];
        for(int j = 0; j < n*n; j++) {
            // Copy column
            for(int i = 0; i < n*n; i++) {
                column[i] = matrix[i][j];
            }

            Arrays.sort(column);
            if(!checkSortedArray(column, n))
                return false;
        }

        return true;
    }

    static int[][][] splitInBatches(int[][] matrix, int n) {
        int[][][] res = new int[n*n][n][n];
        for(int k = 0; k < n*n; k++) {
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    res[k][i][j] = matrix[i + (k/n)*n][j + (k % n)*n];
                }
            }
        }
        return res;
    }

    static boolean checkSubmatrix(int[][] submatrix, int n) {
        int[] subArray = new int[n*n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                subArray[i*n + j] = submatrix[i][j];
            }
        }
        Arrays.sort(subArray);
        if(!checkSortedArray(subArray, n))
            return false;
        return true;
    }

    static boolean checkSubmatrixes(int[][] matrix, int n) {
        int[][][] submatrixes = splitInBatches(matrix, n);
        for(int k = 0; k < n*n; k ++) {
            if(!checkSubmatrix(submatrixes[k], n)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] matrix = new int[n*n][n*n];

        for(int i = 0; i < n*n; i++) {
            for(int j = 0; j < n*n; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }

        // Checking rows
        //System.out.println("Rows: " + checkRows(matrix, n));

        // Checking columns
        //System.out.println("Columns: " + checkColumns(matrix, n));

        // Checking submatrixes
        //System.out.println("Submatrixes: " + checkSubmatrixes(matrix, n));

        if(checkRows(matrix, n) && checkColumns(matrix, n) && checkSubmatrixes(matrix, n)) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }
}
