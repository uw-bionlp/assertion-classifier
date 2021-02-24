package edu.uw.bhi.decoder.liblinear;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class LibLinearDecoder {
    private double[][] weightsByClass = null;
    private int classCount = 6;
    private int featureCount = 89923;
    private int[] classIdxMap = null;

    public LibLinearDecoder(String modelFilePath) {
        loadWeightsFromModelFile(modelFilePath);
    }

    public int decode(int[] featureIdxs) {
        Arrays.sort(featureIdxs);

        // Add up weights
        double[] decValues = new double[classCount];
        for (int featIdx : featureIdxs) {

            // The dimension of testing data may exceed that of training
            if (featIdx <= featureCount) {
                for (int i = 0; i < classCount; i++) {
                    decValues[i] += weightsByClass[i][featIdx-1];
                }
            }
        }

        // ArgMax
        int decMaxIdx = 0;
        for (int i = 1; i < classCount; i++) {
            if (decValues[i] > decValues[decMaxIdx]) {
                decMaxIdx = i;
            }
        }
        return classIdxMap[decMaxIdx];
    }

    private void loadWeightsFromModelFile(String path) {
        BufferedReader input = null;
        double[][] weights = null;
        boolean inWeights = false;
        String line = null; 
        int weightIdx = 0;

        try {
            input = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            while (true) {
                line = input.readLine();
                if (line == null) break;
                else if (inWeights) {
                    String[] weightsStr = line.split(" ");
                    int classIdx = 0;
                    for (String w : weightsStr) {
                        Double weight = Double.parseDouble(w);
                        weights[classIdx][weightIdx] = weight;
                        classIdx++;
                    }
                    weightIdx++;
                } else if (line.startsWith("nr_class")) {
                    classCount = Integer.parseInt(line.split(" ")[1]);
                } else if (line.startsWith("nr_feature")) {
                    featureCount = Integer.parseInt(line.split(" ")[1]);
                } else if (line.startsWith("label")) {
                    String[] labels = line.split(" ");
                    classIdxMap = new int[labels.length-1];
                    for (int i = 1; i < labels.length; i++) {
                        classIdxMap[i-1] = Integer.parseInt(labels[i]);
                    }
                } else if (line.equals("w")) {
                    inWeights = true;
                    weights = new double[classCount][featureCount];
                }
            }
            weightsByClass = weights;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error in closing the file: "+ioe.getMessage());
                ioe.printStackTrace();
            }
        }
    }
}
