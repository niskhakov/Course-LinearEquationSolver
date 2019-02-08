package solver;

import java.text.DecimalFormat;

public class LinearEquationSolver {
    private AugmentedMatrix matrix;
    private StringBuilder logs;
    private double[] result;
    private String decimalPattern = "#.#####";

    LinearEquationSolver(AugmentedMatrix matrix) {
        this.matrix = new AugmentedMatrix(matrix.getMatrix());
        this.logs = new StringBuilder();
    }

    /**
     * It is step 1 of solving Linear Equation: transforming matrix to upper triangular form
     */
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
            int columnOffset = 0; boolean foundInColumn = false;
            double coeff; int innerColNum = colNum;
            while(currentCoefficient == 0 && innerColNum < n) {

                // If a current coefficient is zero, then we should find row with non-zero coefficient below
                if (currentCoefficient == 0) {
                    foundInColumn = false;
                    int nextRowNum;
                    for (nextRowNum = rowNum; nextRowNum < n; nextRowNum++) {
                        coeff = matrix.getRow(nextRowNum).get(innerColNum);
                        if (coeff != 0) {
                            // Found row with non-zero i-th coefficient
                            foundInColumn = true;
                            break;
                        }
                    }

                    if(foundInColumn) {
                        // We should swap rows
                        // These variables can be equal if previous column was filled with zeros,
                        // but on this column this row have non-zero coefficient
                        if(rowNum != nextRowNum) {
                            this.matrix.swapRows(rowNum, nextRowNum);
                            logMessage(String.format("R%d <-> R%d", rowNum, nextRowNum));
                        }
                        // If innerColNum != colNum -> we found non-zero coefficient in other column, we should swap columns
                        if(innerColNum != colNum) {
                            matrix.swapColumns(colNum, innerColNum);
                            logMessage(String.format("C%d <-> C%d", colNum, innerColNum));
                        }
                        // Update currentRow and currentCoefficient variables after all swaps
                        currentRow = matrix.getRow(rowNum); // rowNum after swap references to nextRowNum
                        currentCoefficient = currentRow.get(colNum);
                        break;
                    } else {
                        innerColNum++;
                    }

                }

            }

            // If after all (full search of non-zero coefficient in whole matrix) we have zero-coefficient
            // We should end up with matrix processing - matrix already transformed
            if(currentCoefficient == 0) {
                return;
            }

            // Make coefficient equal to one by dividing entire row by this coefficient
            if(currentCoefficient != 1.0) {
                currentRow.divide(currentCoefficient);
                logMessage(String.format("R%d / %s -> R%d", rowNum, new DecimalFormat(decimalPattern).format(currentCoefficient), rowNum));
            }

            // Perform actions to zero coefficients in other rows
            for(int j = rowNum + 1; j < n; j++) {
                nextRow = matrix.getRow(j);
                nextCoefficient = nextRow.get(colNum);
                // nextCoefficient should be zero
                currentCoefficient = currentRow.get(colNum); // it must always be equal to one
                multiplicator = nextCoefficient / currentCoefficient * (-1);
                nextRow.add(currentRow.multiply(multiplicator, false));
                logMessage(String.format("%s * R%d + R%d -> R%d", new DecimalFormat(decimalPattern).format(multiplicator), rowNum, j, j));
            }
        }
    }

    /**
     * It is step 2 of solving Linear Equation: reduction of non-diagonal members of upper triangular form of matrix
     */
    private void reduceNonDiagonalCoefficients() {

    }

    private void tranformToDiagonalForm() {
        transformToUpperTriangularForm();
//        transformToLowerTriangularForm();
    }

    private void logMessage(String msg) {
        // System.out.println(msg);
        logs.append(msg);
        logs.append("\n");
    }


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
