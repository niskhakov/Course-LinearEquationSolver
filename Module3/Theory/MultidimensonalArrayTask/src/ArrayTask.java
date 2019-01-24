import java.util.ArrayList;
import java.util.Scanner;

public class ArrayTask {

    /**
     * Input:
     *  9 5 3
     *  0 7 -1
     *  -5 2 9
     *  end
     *
     *  Output (Sum of (i-1,j) (i+1,j) (i, j+1), (i, j-1) elements):
     *  3 21 22
     *  10 6 19
     *  20 16 -1
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> lines = new ArrayList<>();
        String input;
        while(!(input = scanner.nextLine()).equals("end")) {
            lines.add(input);
        }

        int n = lines.size();

        int[][] matrix = new int[n][n];
        String[] values;
        for(int i = 0; i < n; i++) {
            values = lines.get(i).split(" ");
            for(int j = 0; j < n; j++) {
                matrix[i][j] = Integer.parseInt(values[j]);
//                System.out.print(matrix[i][j] + " ");
            }
//            System.out.println();
        }

        int[][] result = new int[n][n];
        int iLeft, iRight, jUp, jDown;
        int sum;
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                iLeft =      i == 0 ? n - 1: i - 1;
                iRight = i == (n-1) ? 0 : i + 1;
                jUp =        j == 0 ? (n-1) : j - 1;
                jDown =  j == (n-1) ? 0 : j + 1;

                sum = matrix[iLeft][j] + matrix[iRight][j] + matrix[i][jUp] + matrix[i][jDown];
                result[i][j] = sum;
            }
        }

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
    }
}
