/*
* This file is part of the Assertion Classifier.
*
* The contents of this file are subject to the LGPL License, Version 3.0.
*
* Copyright (C) 2017, The University of Washington
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

package name.adibejan.wrapper;

import name.adibejan.util.Shell;
import name.adibejan.util.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;

import java.util.Properties;
import java.util.MissingResourceException;

import static java.lang.System.out;

/**
 * Wrapper for performing operations with LIBLINEAR (http://www.csie.ntu.edu.tw/~cjlin/liblinear/)
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 * @ady.todo make all classifiers implement Classifier with at least train and predict
 */
public class Liblinear {   
  private static String LIBLINEAR_PATH;
  private static String LIBLINEAR_WORKING_PATH;

  static {
    LIBLINEAR_PATH = System.getProperty("LIBLINEAR_PATH");
    if(LIBLINEAR_PATH == null) {  /* use default path */
      Properties config = new Properties();
      InputStream in = Liblinear.class.getResourceAsStream("/resources/wrapper.properties");
      if(in == null)
        throw new MissingResourceException("Could not find the resource [/resources/wrapper.properties]", null, null);
      try {
        config.load(in);
      } catch (IOException ioe) {
        out.println("Could not load the properties file [/resources/wrapper.properties]");
        ioe.printStackTrace();
      }
      LIBLINEAR_PATH = config.getProperty("path.liblinear");
     
      if(LIBLINEAR_PATH == null)
        throw new ConfigurationException("Could not set the LIBLINEAR_PATH property!");
    }
    System.setProperty("SHELLLOGFILE", LIBLINEAR_PATH+File.separator+"liblinear_shell.log");
    LIBLINEAR_WORKING_PATH = LIBLINEAR_PATH + File.separator + "working";    
  }

  public static String getWorkingPath() {
    return LIBLINEAR_WORKING_PATH;
  }
  
  /**
   * Executes the train command with default parameters: <code>train file.train file.model</code>
   *
   */
  public static void train(String pathTrainFile, String pathModelFile) {
    StringBuilder command = new StringBuilder();
    command.append("train ");
    command.append(pathTrainFile);
    command.append(" ");
    command.append(pathModelFile);
    
    Shell.run(LIBLINEAR_PATH, command.toString(), " ");
  }

  /**
   * Executes the train command with default parameters: <code>train file.train file.model</code>
   *
   * @param c (cost) - liblinear param (default 1)
   * @param s (type of solver) - liblinear param (default 1)
   */
  public static void train(int c, int s, String pathTrainFile, String pathModelFile) {
    if(s < 0 || s > 7)
      throw new IllegalArgumentException("Illegal solver type number ["+s+"]");

    StringBuilder command = new StringBuilder();
    command.append("train ");
    command.append("-c ");
    command.append(c);
    command.append(" -s ");
    command.append(s);
    command.append(" ");
    command.append(pathTrainFile);
    command.append(" ");
    command.append(pathModelFile);
    
    Shell.run(LIBLINEAR_PATH, command.toString(), " ");
  }
  
  /**
   * Executes the train command with default parameters: <code>train file.train file.model</code>
   *
   */
  public static void predict(String pathTestFile, String pathModelFile, String pathPredictFile) {
    StringBuilder command = new StringBuilder();
    command.append("predict ");
    command.append(pathTestFile);
    command.append(" ");
    command.append(pathModelFile);
    command.append(" ");
    command.append(pathPredictFile);
    
    Shell.run(LIBLINEAR_PATH, command.toString(), " ");
  }
};
