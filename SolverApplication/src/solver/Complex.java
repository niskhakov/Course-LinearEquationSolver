package solver;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Complex {
    private final double re;
    private final double im;
    private String decimalPattern = "#.####";

    public static final Complex ZERO = new Complex(0, 0);

    public Complex(double real, double imag) {
        this.re = real;
        this.im = imag;
    }

    public Complex(String str) {
        Complex cmp = parseComplex(str);
        this.re = cmp.re;
        this.im = cmp.im;
    }

    public Complex(Complex b) {
        this.re = b.re;
        this.im = b.im;
    }

    @Override
    public boolean equals(Object x) {
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        Complex that = (Complex) x;
        return (this.re == that.re) && (this.im == that.im);
    }



    public Complex add(Complex b) {
        Complex a = this;
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    public Complex subtract(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    public Complex multiply(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.im * b.re + a.re * b.im;
        return new Complex(real, imag);
    }

    public Complex reciprocal() {
        Complex a = this;
        double denominator = a.re * a.re + a.im * a.im;
        double real = a.re / denominator;
        double imag = - a.im / denominator;
        return new Complex(real, imag);
    }

    public Complex divide(Complex b) {
        Complex a = this;
        return a.multiply(b.reciprocal());
    }

    public static Complex parseComplex(String inp) {
        Pattern imagPattern = Pattern.compile("([+-]?\\d+(\\.?\\d+)?([eE][+-]?\\d)?[i|I])|([+-]?[i|I](\\d+(\\.?\\d+)?)?([eE][+-]?\\d)?)");

        // My numerous attempts, i leave it here)

        // Pattern realPattern = Pattern.compile("([+-]?(?!i|I)?\\d++(\\.?\\d++)?([eE][+-]?\\d)?(?!i|I))");
        // Pattern realPattern = Pattern.compile("(?!(([+-]?\\d+(\\.?\\d+)?([eE][+-]?\\d)?[i|I])))");
        // Matcher re = realPattern.matcher(inp);
        // re.find();
        // System.out.printf("Re: " + re.group() + " ");


        Matcher im = imagPattern.matcher(inp);
        String impart, repart;
        try {
            im.find();
            impart = im.group();
        } catch (IllegalStateException e) {
            impart = ""; // If a repart or impart is blank it will be zero
        }
        repart = inp.replace(impart, ""); // It is easier than finding/creating regexp pattern to find real part

        if(impart.equals("+i") || impart.equals("+I") || impart.equals("i") || impart.equals("I")) {
            impart = "1i";
        } else if(impart.equals("-i") || impart.equals("-I")) {
            impart = "-1i";
        }

        double rep = repart.isBlank() ? 0 : Double.parseDouble(repart);
        double imp = impart.isBlank() ? 0 : Double.parseDouble(impart.replaceAll("i|I", ""));

        return new Complex(rep, imp);
    }

    public void setDecimalPattern(String pattern) {
        this.decimalPattern = pattern;
    }

    @Override
    public String toString() {
        String ans;
        DecimalFormat f = new DecimalFormat(decimalPattern);
        if (im == 0) return f.format(re) + "";
        if (re == 0) return f.format(im) + "i";
        if (im <  0) return f.format(re) + "-" + f.format(-im) + "i";
        return f.format(re) + "+" + f.format(im) + "i";
    }


}
