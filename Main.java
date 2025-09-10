package com.example.stockpredictor;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java -jar stockpredictor.jar <csv-path> [lagDays]");
            return;
        }
        String path = args[0];
        int lag = 5;
        if (args.length >= 2) lag = Integer.parseInt(args[1]);

        List<CSVReader.Row> rows = CSVReader.read(path);
        if (rows.size() <= lag) {
            System.err.println("Not enough rows for the given lag");
            return;
        }

        StockPredictor.DataSet ds = StockPredictor.buildLagFeatures(rows, lag);
        double[] beta = StockPredictor.fitOLS(ds.X, ds.y);

        System.out.println("Fitted coefficients (intercept first):");
        for (int i = 0; i < beta.length; i++) System.out.printf("b[%d]=%.6f%n", i, beta[i]);

        // Predict next day using most recent lags
        double[] lastX = new double[lag+1];
        lastX[0] = 1.0;
        int n = rows.size();
        for (int j = 0; j < lag; j++) lastX[1+j] = rows.get(n-1-j).close;
        double pred = 0.0;
        for (int i = 0; i < beta.length; i++) pred += beta[i] * lastX[i];

        System.out.printf("Predicted next close: %.4f%n", pred);

        // simple interactive mode
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Type 'exit' or press Enter to quit. Type 'sim' to simulate an update.");
            while (true) {
                String line = sc.nextLine();
                if (line == null || line.trim().isEmpty() || line.equalsIgnoreCase("exit")) break;
                if (line.equalsIgnoreCase("sim")) {
                    // ask for simulated new close
                    System.out.print("Enter simulated new close: ");
                    String v = sc.nextLine();
                    double newClose = Double.parseDouble(v);
                    // shift rows and recompute quickly using same model (for demo we won't refit)
                    for (int i = rows.size()-1; i > 0; i--) rows.set(i, rows.get(i-1));
                    rows.set(0, new CSVReader.Row("SIM", rows.get(0).open, rows.get(0).high, rows.get(0).low, newClose, rows.get(0).volume));
                    for (int j = 0; j < lag; j++) lastX[1+j] = rows.get(rows.size()-1-j).close;
                    pred = 0.0;
                    for (int i = 0; i < beta.length; i++) pred += beta[i] * lastX[i];
                    System.out.printf("New predicted next close: %.4f%n", pred);
                }
            }
        }

        System.out.println("Goodbye.");
    }
}