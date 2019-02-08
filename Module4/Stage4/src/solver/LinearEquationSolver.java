package solver;

import java.text.DecimalFormat;

public class LinearEquationSolver {
    private AugmentedMatrix matrix;
    private StringBuilder logs;
    private double[] result;

    LinearEquationSolver(AugmentedMatrix matrix) {
        this.matrix = new AugmentedMatrix(matrix.getMatrix());
        this.logs = new StringBuilder();
    }

    private void transformToUpperTriangularForm() {
        Row currentRow; double currentCoefficient;
        Row nextRow; double nextCoefficient;
        double multiplicator;
        int n = matrix.size()[0];
        int rowNum, colNum;
        // If there are no special cases, then rowNum = colNum (transforming to diagonal form)
        for(rowNum = 0, colNum = 0; rowNum < n && colNum < n; rowNum++, colNum++) {
            currentRow = matrix.getRow(rowNum);
            currentCoefficient = currentRow.get(colNum);


            // Search for the row with non-zero coefficent
            while(currentCoefficient == 0) {

                // If a current coefficient is zero, then we should find row with non-zero coefficient below
                double coeff;
                if (currentCoefficient == 0) {
                    for (int nextRowNum = rowNum + 1; nextRowNum < n; nextRowNum++) {
                        coeff = matrix.getRow(nextRowNum).get(colNum);
                        if (coeff != 0) {
                            // Found row with non-zero i-th coefficient
                            // We should swap rows
                            this.matrix.swapRows(rowNum, nextRowNum);
                            logMessage(String.format("R%d <-> R%d",rowNum, nextRowNum));
                            // Update currentRow and currentCoefficient variables
                            currentRow = matrix.getRow(rowNum);
                            currentCoefficient = coeff;
                            break;
                        }
                    }
                }

                // If search of row with non-zero coefficient failed, we should go search for the right non-zero coefficient
                if (currentCoefficient == 0) {
                    colNum++;
                    logMessage(String.format("Go from processing %d column to processing %d one", colNum-1, colNum));
                    // Update currentRow and currentCoefficient variables
                    currentRow = matrix.getRow(rowNum);
                    currentCoefficient = currentRow.get(colNum);
                }
            }

            for(int j = rowNum + 1; j < n; j++) {
                nextRow = matrix.getRow(j);
                nextCoefficient = nextRow.get(colNum);
                // nextCoefficient should be zero
                multiplicator = nextCoefficient / currentCoefficient * (-1);
                nextRow.add(currentRow.multiply(multiplicator, false));
                logMessage(String.format("%s * R%d + R%d -> R%d", new DecimalFormat("#.###").format(multiplicator), rowNum, j, j));

//                if(coefficient != 1.0) {
//                    matrix.getRow(j).normalizeRow(i);
//                    logMessage(String.format("R%d / %s - R%d -> R%d", j, new DecimalFormat("#.###").format(coefficient), i, j));
//                } else {
//                    logMessage(String.format("R%d - R%d -> R%d", j, i, j));
//                }
//                matrix.getRow(j).subtract(currentRow);
            }
        }
    }

//    private void transformToLowerTriangularForm() {
//        Row currentRow;
//        int n = matrix.size()[0];
//        for(int i = n-1; i >= 0; i--) {
//            currentRow = matrix.getRow(i);
//            normalizeRowsForTransforming(i, currentRow);
//            for(int j = i-1; j >= 0; j--) {
//                subtractRowsForTransforming(i,j,currentRow);
//            }
//        }
//    }

    private void tranformToDiagonalForm() {
        transformToUpperTriangularForm();
//        transformToLowerTriangularForm();
    }

    private void logMessage(String msg) {
        logs.append(msg);
        logs.append("\n");
    }
//
//    /**
//     * Devide every element of matrix[j] row by the element at position i, subtracts currentRow from matrix[j]
//     * and logs results
//     * (was originally created to move duplicating code from methods)
//     * @param i
//     * @param j
//     * @param currentRow
//     */
//    private void subtractRowsForTransforming(int i, int j, Row currentRow) {
//        double coefficient = matrix.getRow(j).get(i);
//        if(coefficient != 1.0) {
//            matrix.getRow(j).normalizeRow(i);
//            logMessage(String.format("R%d / %s - R%d -> R%d", j, new DecimalFormat("#.###").format(coefficient), i, j));
//        } else {
//            logMessage(String.format("R%d - R%d -> R%d", j, i, j));
//        }
//        matrix.getRow(j).subtract(currentRow);
//    }

//    /**
//     * Divide every element of the row by the element at position `i` and logs results
//     * (was originally created to move duplicating code from methods)
//     * @param i index of the element at the row
//     * @param currentRow
//     */
//    private void normalizeRowsForTransforming(int i, Row currentRow) {
//        double coefficient = currentRow.get(i);
//        if(coefficient != 1.0) {
//            currentRow.normalizeRow(i);
//            logMessage(String.format("R%d / %s -> R%d", i, new DecimalFormat("#.###").format(coefficient), i));
//        }
//    }

    public String getLogs() {
        return this.logs.toString();
    }

    public double[] getResult() {
        return result;
    }

    public String getResultString() {
        StringBuilder sb = new StringBuilder();
        for(double coefficient: result) {
            sb.append(new DecimalFormat("#.####").format(coefficient));
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public LinearEquationSolver solve() {
        tranformToDiagonalForm();
        // Get coefficients of the last column
        int n = this.matrix.size()[0];
        int m = this.matrix.size()[1];
        result = new double[n];
        for(int i = 0; i < n; i++) {
            result[i] = matrix.get(i, m-1);
        }
        return this;
    }

    public AugmentedMatrix getMatrix() {
        return matrix;
    }
}
