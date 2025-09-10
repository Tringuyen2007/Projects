package com.example.stockpredictor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static class Row {
        public final String date;
        public final double open, high, low, close;
        public final long volume;

        public Row(String date, double open, double high, double low, double close, long volume) {
            this.date = date;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
        }
    }

    public static List<Row> read(String path) throws IOException {
        List<Row> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String header = br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                String date = parts[0];
                double open = Double.parseDouble(parts[1]);
                double high = Double.parseDouble(parts[2]);
                double low = Double.parseDouble(parts[3]);
                double close = Double.parseDouble(parts[4]);
                long volume = Long.parseLong(parts[5]);
                rows.add(new Row(date, open, high, low, close, volume));
            }
        }
        return rows;
    }
}