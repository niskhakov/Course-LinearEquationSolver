package solver;

import java.util.Arrays;

public class Row {
    private double[] row;

    Row(double[] row) {
        setRow(row);
    }

    Row(int m) {
        row = new double[m];
    }


    Row divide(double v) {
        if(v == 0){
            throw new java.lang.ArithmeticException("Division by zero");
        }
        for(int i = 0; i < row.length; i++) {
            row[i] = row[i] / v;
        }
        return this;
    }

    Row multiply(double v, boolean inplace) {
        Row curRow;
        if(inplace) {
            curRow = this;
        } else {
            curRow = new Row(row.length);
        }
        for(int i = 0; i < row.length; i++) {
            curRow.set(i, row[i] * v);
        }
        return curRow;
    }

    Row multiply(double v) {
        return multiply(v, true);
    }

    Row subtract(Row row) {
        for(int i = 0; i < this.row.length; i++) {
            this.row[i] = this.row[i] - row.get(i);
        }
        return this;
    }

    Row add(Row row) {
        return add(row, true);
    }

    Row add(Row nextRow, boolean inplace) {
        Row curRow;
        if(inplace) {
            curRow = this;
        } else {
            curRow = new Row(row.length);
        }
        for(int i = 0; i < row.length; i++) {
            curRow.set(i, curRow.get(i) + nextRow.get(i));
        }
        return curRow;
    }

    public double[] getRow() {
        return Arrays.copyOf(row, row.length);
    }

    public void setRow(double[] row) {
        this.row = Arrays.copyOf(row, row.length);
    }

    public void set(int j, double value) {
        this.row[j] = value;
    }

    public double get(int j) {
        return this.row[j];
    }

    public boolean isZeroFilled() {
        for(int i = 0; i < row.length; i++) {
            if( row[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public int getIndexOfFirstNonZeroElement() {
        for(int i = 0; i < row.length; i++) {
            if(row[i] != 0 ) {
                return i;
            }
        }

        return -1;
    }

    public int size() {
        return this.row.length;
    }
}
