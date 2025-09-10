package com.example.stockpredictor;

import java.util.ArrayList;
import java.util.List;

public class StockPredictor {

    // Fit linear regression using OLS: y = X * beta, returns beta (including intercept at index 0)
    public static double[] fitOLS(double[][] X, double[] y) {
        int n = X.length;
        int p = X[0].length; // includes intercept
        // compute X^T X and X^T y
        double[][] XtX = new double[p][p];
        double[] Xty = new double[p];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                Xty[j] += X[i][j] * y[i];
                for (int k = 0; k < p; k++) {
                    XtX[j][k] += X[i][j] * X[i][k];
                }
            }
        }
        // Solve XtX * beta = Xty using Gaussian elimination
        return solveLinearSystem(XtX, Xty);
    }

    private static double[] solveLinearSystem(double[][] A, double[] b) {
        int n = b.length;
        double[][] M = new double[n][n+1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, M[i], 0, n);
            M[i][n] = b[i];
        }
        // Gaussian elimination
        for (int i = 0; i < n; i++) {
            // pivot
            int pivot = i;
            for (int r = i+1; r < n; r++) {
                if (Math.abs(M[r][i]) > Math.abs(M[pivot][i])) pivot = r;
            }
            double[] tmp = M[i]; M[i] = M[pivot]; M[pivot] = tmp;
            double diag = M[i][i];
            if (Math.abs(diag) < 1e-12) throw new RuntimeException("Singular matrix");
            for (int j = i; j <= n; j++) M[i][j] /= diag;
            for (int r = 0; r < n; r++) if (r != i) {
                double factor = M[r][i];
                for (int c = i; c <= n; c++) M[r][c] -= factor * M[i][c];
            }
        }
        double[] x = new double[n];
        for (int i = 0; i < n; i++) x[i] = M[i][n];
        return x;
    }

    // Build dataset using lagged close prices as features
    public static DataSet buildLagFeatures(List<CSVReader.Row> rows, int lagDays) {
        List<double[]> Xlist = new ArrayList<>();
        List<Double> ylist = new ArrayList<>();
        for (int i = lagDays; i < rows.size(); i++) {
            double[] features = new double[lagDays + 1]; // intercept + lag features
            features[0] = 1.0; // intercept
            for (int j = 0; j < lagDays; j++) {
                features[1 + j] = rows.get(i - 1 - j).close; // last day, 2 days ago, ...
            }
            Xlist.add(features);
            ylist.add(rows.get(i).close);
        }
        double[][] X = Xlist.toArray(new double[0][]);
        double[] y = ylist.stream().mapToDouble(Double::doubleValue).toArray();
        return new DataSet(X, y);
    }

    public static class DataSet {
        public final double[][] X;
        public final double[] y;
        public DataSet(double[][] X, double[] y) { this.X = X; this.y = y; }
    }
}