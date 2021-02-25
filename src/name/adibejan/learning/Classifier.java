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

package name.adibejan.learning;

import name.adibejan.wrapper.Liblinear;
import name.adibejan.util.ConfigurationException;

/**
 * Main class for executing classification tasks
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | September 2011
 */
public class Classifier {
  private static ClassificationAlgorithm algorithm;

  static {
    algorithm = ClassificationAlgorithm.SVM_LIBLINEAR; /* default initialization */
  }
  
  public static void setAlgorithm(ClassificationAlgorithm algo) {
    algorithm = algo;
  }
  
  public static ClassificationAlgorithm getAlgorithm() {
    return algorithm;
  }

  public static String getWorkingPath() {
    switch(algorithm) {
    case SVM_LIBLINEAR: return Liblinear.getWorkingPath();
    }
    throw new ConfigurationException("Unsuported classification algorithm ["+algorithm+"]");
  }

  public static void trainpredict(String trainFile, String testFile, String predictFile) {
    trainpredict(trainFile, trainFile + ".model", testFile, predictFile);    
  }

  public static void trainpredict(int c, int s, String trainFile, String testFile, String predictFile) {
    trainpredict(trainFile, trainFile + ".model", testFile, predictFile);    
  }

  public static void trainpredict(String trainFile, String modelFile, String testFile, String predictFile) {
    switch(algorithm) {
    case SVM_LIBLINEAR: 
      Liblinear.train(trainFile, modelFile);
      Liblinear.predict(testFile, modelFile, predictFile);
      break;
    }
  }

  public static void trainpredict(int c, int s, String trainFile, String modelFile, String testFile, String predictFile) {
    switch(algorithm) {
    case SVM_LIBLINEAR: 
      Liblinear.train(c, s, trainFile, modelFile);
      Liblinear.predict(testFile, modelFile, predictFile);
      break;
    }
  }

  public static void train(String trainFile, String modelFile) {
    switch(algorithm) {
    case SVM_LIBLINEAR: 
      Liblinear.train(trainFile, modelFile);      
      break;
    }
  }

  public static void train(int c, int s, String trainFile, String modelFile) {
    switch(algorithm) {
    case SVM_LIBLINEAR: 
      Liblinear.train(c, s, trainFile, modelFile);      
      break;
    }
  }

  public static void predict(String testFile, String modelFile, String predictFile) {
    switch(algorithm) {
    case SVM_LIBLINEAR: 
      Liblinear.predict(testFile, modelFile, predictFile);
      break;
    }
  }
}