package solver;

import java.text.DecimalFormat;

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
    private String decimalPattern = "#.#####";

    LinearEquationSolver(AugmentedMatrix matrix) {
        this.matrix = new AugmentedMatrix(matrix.getMatrix());
        this.logs = new StringBuilder();
    }

    /**
     * It is step 1 of solving System of Linear Equations: transforming matrix to upper triangular form
     */
    private void transformToUpperTriangularForm() {
        Row currentRow; double currentCoefficient;
        Row nextRow; double nextCoefficient;
        double multiplicator;
        int n = matrix.size()[0]; // number of rows
        int m = matrix.size()[1]; // number of cols
        int rowNum, colNum;
        // If there are no special cases, then rowNum = colNum (transforming to diagonal form)
        for(rowNum = 0, colNum = 0; rowNum < n && colNum < m-1; rowNum++, colNum++) {
            currentRow = matrix.getRow(rowNum);
            currentCoefficient = currentRow.get(colNum);


            // Search for the row with non-zero coefficent
            int columnOffset = 0; boolean foundInColumn = false;
            double coeff; int innerColNum = colNum;
            while(currentCoefficient == 0 && innerColNum < m-1) {

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
                currentCoefficient = currentRow.get(colNum); // it must always be equal to one
                // nextCoefficient should be zero
                if(nextCoefficient != 0) {
                    multiplicator = nextCoefficient / currentCoefficient * (-1);
                    nextRow.add(currentRow.multiply(multiplicator, false));
                    logMessage(String.format("%s * R%d + R%d -> R%d", new DecimalFormat(decimalPattern).format(multiplicator), rowNum, j, j));
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
        double currentCoefficient, nextCoefficient, multiplicator;
        Row row; Row nextRow;
        for(rowNum = n-1; rowNum >= 0; rowNum--) {

            // Find first non-zero element
            row = matrix.getRow(rowNum);
            for(int i = 0; i < row.size()-1; i++) {
                if(row.get(i) != 0) {
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
                if (nextCoefficient != 0) {
                    multiplicator = nextCoefficient / currentCoefficient * (-1);
                    nextRow.add(row.multiply(multiplicator, false));
                    logMessage(String.format("%s * R%d + R%d -> R%d", new DecimalFormat(decimalPattern).format(multiplicator), rowNum, nextRowNum, nextRowNum));
                }
            }

        }
    }

    /**
     * It is step 3 of solving System of Linear Equations: checking for number of solutions
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
            // Count number of non-zero elements (except last element, which corresponds to coefficient B) in last row,
            // which contains at least one non-zero element
            int counter = 0;
            for(int i = 0; i < m-1; i++) {
                if(currentRow.get(i) != 0) {
                    counter++;
                }
            }
            if(counter > 1) {
                // We have two or more non zero coefficients in a "diagonal" form of not-augmented matrix
                // thus, we have a linear equations system which has infinite number of solutions
                return SystemType.INFINITE_NUMBER_OF_SOLUTIONS;
            }

            // All other ways were considered -> only one variant
            // We have single solution
            return SystemType.SINGLE_SOLUTION;
        }

        return SystemType.UNKNOWN_ERROR;
    }

    private void transformToDiagonalForm() {
        transformToUpperTriangularForm();
        reduceNonDiagonalCoefficients();
    }

    private void logMessage(String msg) {
        // System.out.println(msg);
        logs.append(msg);
        logs.append("\n");
    }

    private double[] getSolution() {
        // TODO: implement method
        return new double[]{-1, -1};
    }

    public String getLogs() {
        return this.logs.toString();
    }

    public String getResult() {
        return result;
    }


    public LinearEquationSolver solve() {
        transformToDiagonalForm();
        SystemType type = checkResult();

        switch(type) {
            case NO_SOLUTIONS:
                result = "No solutions";
                return this;
            case INFINITE_NUMBER_OF_SOLUTIONS:
                result = "Infinite solutions";
                return this;
            case SINGLE_SOLUTION:
                double[] res = getSolution();
                StringBuilder sb = new StringBuilder();
                for(double coefficient: res) {
                    sb.append(new DecimalFormat(decimalPattern).format(coefficient));
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
