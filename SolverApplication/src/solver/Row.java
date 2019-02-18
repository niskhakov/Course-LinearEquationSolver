package solver;

import java.util.Arrays;

public class Row {
    private Complex[] row;

    Row(Complex[] row) {
        setRow(row);
    }

    Row(int m) {
        row = new Complex[m];
    }


    Row divide(Complex v) {
        if(v.equals(Complex.ZERO)){
            throw new java.lang.ArithmeticException("Division by zero");
        }
        for(int i = 0; i < row.length; i++) {
            row[i] = row[i].divide(v);
        }
        return this;
    }

    Row multiply(Complex v, boolean inplace) {
        Row curRow;
        if(inplace) {
            curRow = this;
        } else {
            curRow = new Row(row.length);
        }
        for(int i = 0; i < row.length; i++) {
            curRow.set(i, row[i].multiply(v));
        }
        return curRow;
    }

    Row multiply(Complex v) {
        return multiply(v, true);
    }

    Row subtract(Row row) {
        for(int i = 0; i < this.row.length; i++) {
            this.row[i] = this.row[i].subtract(row.get(i));
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
            curRow.set(i, curRow.get(i).add(nextRow.get(i)));
        }
        return curRow;
    }

    public Complex[] getRow() {
        return Arrays.copyOf(row, row.length);
    }

    public void setRow(Complex[] row) {
        this.row = Arrays.copyOf(row, row.length);
    }

    public void set(int j, Complex value) {
        this.row[j] = new Complex(value);
    }

    public Complex get(int j) {
        return this.row[j];
    }

    public boolean isZeroFilled() {
        for(int i = 0; i < row.length; i++) {
            if( !row[i].equals(Complex.ZERO)) {
                return false;
            }
        }
        return true;
    }

    public int getIndexOfFirstNonZeroElement() {
        for(int i = 0; i < row.length; i++) {
            if(!row[i].equals(Complex.ZERO) ) {
                return i;
            }
        }

        return -1;
    }

    public int size() {
        return this.row.length;
    }
}
