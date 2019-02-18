package solver;

import java.util.ArrayList;


enum SystemType {
    NO_SOLUTIONS,
    SINGLE_SOLUTION,
    INFINITE_NUMBER_OF_SOLUTIONS,
    UNKNOWN_ERROR
}

public class LinearEquationSolver {
    private AugmentedMatrix matrix;
    private StringBuilder logs;
    private String result;
    private SystemType resultType;
    private String decimalPattern = "#.#####";

    LinearEquationSolver(AugmentedMatrix matrix) {
        this.matrix = new AugmentedMatrix(matrix.getMatrixCopy());
        this.logs = new StringBuilder();
    }

    /**
     * It is step 1 of solving System of Linear Equations: transforming matrix to upper triangular form
     */
    private void transformToUpperTriangularForm() {
        Row currentRow; Complex currentCoefficient;
        Row nextRow; Complex nextCoefficient;
        Complex multiplicator;
        int n = matrix.size()[0]; // number of rows
        int m = matrix.size()[1]; // number of cols
        int rowNum, colNum;
        // If there are no special cases, then rowNum = colNum (transforming to diagonal form)
        for(rowNum = 0, colNum = 0; rowNum < n && colNum < m-1; rowNum++, colNum++) {
            currentRow = matrix.getRow(rowNum);
            currentCoefficient = currentRow.get(colNum);


            // Search for the row with non-zero coefficent
            int columnOffset = 0; boolean foundInColumn = false;
            Complex coeff; int innerColNum = colNum;
            while(currentCoefficient.equals(Complex.ZERO) && innerColNum < m-1) {

                // If a current coefficient is zero, then we should find row with non-zero coefficient below
                if (currentCoefficient.equals(Complex.ZERO)) {
                    foundInColumn = false;
                    int nextRowNum;
                    for (nextRowNum = rowNum; nextRowNum < n; nextRowNum++) {
                        coeff = matrix.getRow(nextRowNum).get(innerColNum);
                        if (!coeff.equals(Complex.ZERO)) {
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
            if(currentCoefficient.equals(Complex.ZERO)) {
                return;
            }

            // Make coefficient equal to one by dividing entire row by this coefficient
            if(!currentCoefficient.equals(new Complex(1, 0))) {
                currentRow.divide(currentCoefficient);
                logMessage(String.format("R%d / %s -> R%d", rowNum, currentCoefficient.toString(), rowNum));
            }

            // Perform actions to zero coefficients in other rows
            for(int j = rowNum + 1; j < n; j++) {
                nextRow = matrix.getRow(j);
                nextCoefficient = nextRow.get(colNum);
                currentCoefficient = currentRow.get(colNum); // it must always be equal to one
                // nextCoefficient should be zero
                if(!nextCoefficient.equals(Complex.ZERO)) {
                    multiplicator = nextCoefficient.divide(currentCoefficient).multiply(new Complex(-1, 0));
                    nextRow.add(currentRow.multiply(multiplicator, false));
                    logMessage(String.format("%s * R%d + R%d -> R%d", multiplicator.toString(), rowNum, j, j));
                }
            }
        }
    }

    /**
     * It is step 2 of solving System of Linear Equations: reduction of non-diagonal members of upper triangular form of matrix
     */
    private void reduceNonDiagonalCoefficients() {
        // Start from the end
        int rowNum, colNum, nextRowNum;
        int n = matrix.size()[0];
        int coefficientIndex = -1;
        Complex currentCoefficient, nextCoefficient, multiplicator;
        Row row; Row nextRow;
        for(rowNum = n-1; rowNum >= 0; rowNum--) {

            // Find first non-zero element
            row = matrix.getRow(rowNum);
            for(int i = 0; i < row.size()-1; i++) {
                if(!row.get(i).equals(Complex.ZERO)) {
                    coefficientIndex = i;
                    break;
                }
            }


            // If entire row filled with zeros - stop processing, go to the next iteration
            if(coefficientIndex == -1) {
                continue;
            }

            // Start subtracting this row from rows above
            for(nextRowNum = rowNum - 1; nextRowNum >= 0; nextRowNum--) {
                currentCoefficient = row.get(coefficientIndex);
                nextRow = matrix.getRow(nextRowNum);
                nextCoefficient = nextRow.get(coefficientIndex);
                if (!nextCoefficient.equals(Complex.ZERO)) {
                    multiplicator = nextCoefficient.divide(currentCoefficient).multiply(new Complex(-1, 0));
                    nextRow.add(row.multiply(multiplicator, false));
                    logMessage(String.format("%s * R%d + R%d -> R%d", multiplicator.toString(), rowNum, nextRowNum, nextRowNum));
                }
            }

        }
    }


    /**
     * It is step 3 of solving System of Linear Equations: checking for number of solutions
     *
     * In this method it is essential that matrix has a pseudo-diagonal form (after 1st and 2nd steps were completed)
     *
     * @return SystemType enum
     */
    private SystemType checkResult() {
        // Checking for no solution
        int rowNum, index;
        int n = matrix.size()[0]; // number of rows
        int m = matrix.size()[1]; // number of columns
        Row currentRow;
        for(rowNum = n-1; rowNum >=0; rowNum--) {
            currentRow = matrix.getRow(rowNum);
            if(currentRow.isZeroFilled()) {
                continue; // Zero rows are insignificant for us
            }

            // Getting first non-zero element
            index = currentRow.getIndexOfFirstNonZeroElement();

            // If first non-zero element located in a last column - the system is inconsistent
            if(index == m-1) {
                return SystemType.NO_SOLUTIONS;
            }

            // Check for infinite number of solutions

            // We look at last non-trivial row and get the matrix[i][j] first non-zero element, if j - is last column of
            // non-augmented matrix - then we have single solution, if j - is not the last column of the non-augmented
            // matrix - there is infinite number of solutions
            // Example:
            // 1 0 0 | 4                        1 0 0 | 4
            // 0 1 0 | 5                        0 1 0 | 5
            // 0 0 0 | 0                        0 0 1 | 0
            // 0 0 0 | 0                        0 0 0 | 0
            // INFINITE SOLUTIONS            SINGLE SOLUTION
            int cols = m -1; // number of columns of non-augmented matrix
            if(index+1 != cols) {
                return SystemType.INFINITE_NUMBER_OF_SOLUTIONS;
            }

            // All other ways were considered -> only one variant
            // We have single solution
            return SystemType.SINGLE_SOLUTION;
        }

        return SystemType.UNKNOWN_ERROR;
    }

    /**
     * It is step 4 of solving System of Linear Equations: get original columns' order in matrix by looking at swap history
     *
     * Look at swap history and do reverse actions in desc order to get original column positions.
     */
    public void revokeColumnsSwap() {
        int[] reversedSwapIndexes;
        ArrayList<SwapInfo> swapHistory = matrix.getSwapHistory();
        try {
            for (int i = swapHistory.size() - 1; i >= 0; i--) {
                reversedSwapIndexes = swapHistory.get(i).getReversedSwapInfo();
                matrix.swapColumns(reversedSwapIndexes[0], reversedSwapIndexes[1], false);
                logMessage(String.format("Reverse: C%d <-> C%d", reversedSwapIndexes[0], reversedSwapIndexes[1]));
            }
            matrix.clearSwapHistory();
        } catch(NullPointerException e) {
            // Ignore:
            // It means that we haven't swapped any columns and swapHistory is not initialized yet
            // (we do not initialize swapHistory to preserve memory, i know that in general this is possibly stupid =) )
            return;
        }
    }

    private void transformToDiagonalForm() {
        transformToUpperTriangularForm();
        reduceNonDiagonalCoefficients();
        resultType = checkResult();
        revokeColumnsSwap();
    }

    private void logMessage(String msg) {
        // System.out.println(msg);
        logs.append(msg);
        logs.append("\n");
    }

    /**
     * Get coefficients of single solution
     *
     * @return solution - array of coefficients
     */
    private Complex[] getSingleSolution() {
        int n = matrix.size()[0]; // Number of rows
        int m = matrix.size()[1]; // Number of columns of augmented matrix
        Complex[] solution = new Complex[m-1];
        int solutionIndex;
        for(int rowNum = 0; rowNum < n; rowNum++) {
            if(matrix.getRow(rowNum).isZeroFilled()) {
                continue;
            }

            solutionIndex = matrix.getRow(rowNum).getIndexOfFirstNonZeroElement();
            solution[solutionIndex] = matrix.getRow(rowNum).get(m-1);
        }
        return solution;
    }

    public String getLogs() {
        return this.logs.toString();
    }

    public String getResult() {
        return result;
    }


    public LinearEquationSolver solve() {
        transformToDiagonalForm();
        switch(resultType) {
            case NO_SOLUTIONS:
                result = "No solutions";
                return this;
            case INFINITE_NUMBER_OF_SOLUTIONS:
                result = "Infinite solutions";
                return this;
            case SINGLE_SOLUTION:
                Complex[] res = getSingleSolution();
                StringBuilder sb = new StringBuilder();
                for(Complex coefficient: res) {
                    sb.append(coefficient.toString());
                    sb.append("\n");
                }
                sb.deleteCharAt(sb.length()-1);
                result = sb.toString();
        }
        return this;
    }

    public AugmentedMatrix getMatrix() {
        return matrix;
    }
}
