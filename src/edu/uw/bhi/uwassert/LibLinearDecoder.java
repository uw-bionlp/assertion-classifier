/*
* This file is part of the Assertion Classifier.
*
* The contents of this file are subject to the LGPL License, Version 3.0.
*
* Copyright (C) 2021, The University of Washington
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package edu.uw.bhi.uwassert;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Java implementation of LibLinear prediction method
 *
 * @author Nic Dobbins
 * @version 1.0
 * @since JDK1.6 | February 2021
 */
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
                        weights[classIdx][weightIdx] = Double.parseDouble(w);
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
