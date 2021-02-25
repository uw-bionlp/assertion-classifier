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

package name.adibejan.wrapper;
import name.adibejan.util.ConfigurationException;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import java.util.Properties;
import java.util.MissingResourceException;

import static java.lang.System.out;

/**
 * Wrapper for performing operations with OpenNLP (http://incubator.apache.org/opennlp/)
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | September 2010
 */
public class OpenNLP {   
  private static TokenizerME tokenizerME;
  
  /**
   * Implements the singleton pattern to ensure one instance for tokenizer model
   */
  private static synchronized void loadENTokenizerModel() {
    if(tokenizerME == null) {
      String modelPath = System.getProperty("OPENNLP_EN_TOKENMODEL_PATH");
      
      if(modelPath == null) {  /* use default path */      
        Properties config = new Properties();
        InputStream in = OpenNLP.class.getResourceAsStream("/resources/wrapper.properties");
        if(in == null)
          throw new MissingResourceException("Could not find the resource [/resources/wrapper.properties]", null, null);
        try {
          config.load(in);
        } catch (IOException ioe) {
          out.println("Could not load the properties file [/resources/wrapper.properties]");
          ioe.printStackTrace();
        }
        modelPath = config.getProperty("path.opennlp.enmodel.token");
      
        if(modelPath == null)
          throw new ConfigurationException("Could not set the OPENNLP_EN_TOKENMODEL_PATH property!");      
      }
      
      TokenizerModel model = null;
      InputStream modelStream = null;
      try {
        modelStream = new FileInputStream(modelPath);
        model = new TokenizerModel(modelStream);
      } catch (IOException ioe) {
        out.println("Exception in loadind the token model:"+ioe.getMessage());
        ioe.printStackTrace();
      } finally {
        if (modelStream != null) {
          try {
            modelStream.close();
          } catch (IOException ioe) {            
            out.println("Exception in closing the token model:"+ioe.getMessage());
          }
        }
      }
      tokenizerME = new TokenizerME(model);
    }
  }

  /**
   * Tokenizes a given text 
   *
   * @param text the string to be tokenized
   * @return an array of tokens
   */
  public static String[] tokenize(String text) {
    loadENTokenizerModel();
    return tokenizerME.tokenize(text);
  }   
}