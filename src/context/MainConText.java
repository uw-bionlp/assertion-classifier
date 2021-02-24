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

package context;

/***************************************************************************************
 * Author: Junebae Kye, Supervisor: Imre Solti
 * Date: 07/04/2010 
 *
 *
 *This program is to mainly print a line along with its negation scope, temporality, and experiencer. For example, 550     hiatal herniaA HIATAL HERNIA was found.     Affirmed     -1     Historical     Patient(Number TAB Phrase TAB Sentence TAB Dummystring TAB Negation Scope TAB Temporality TAB Experiencer)
 *Usage: java MainConText Annotations-1-120-random.txt yes(or no)
 *
 ****************************************************************************************/

import java.io.*;
import java.util.*;

public class MainConText {

  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
	    System.out.println("Usage: please, specify a text file and an option of yes or no for negation scope.");
	    return;
    }
    boolean value;
    if (args[1].toLowerCase().equals("yes"))
	    value = true;
    else
	    value = false;
    GenNegEx n = new GenNegEx(value);
    GenExperiencer e = new GenExperiencer();
    GenTemporality t = new GenTemporality();	  
    BufferedReader file = new BufferedReader(new FileReader(args[0]));
    String line;
    while ((line = file.readLine()) != null) {
	    String[] s = line.split("\\t");
	    String sentence = cleans(s[2]);
	    String scope = n.negScope(sentence);
	    String experi = e.getExperiencer(sentence);
	    String tempo = t.getTemporality(sentence);
	    System.out.println(line + "\t" + scope + "\t" + tempo + "\t" + experi);
      
      //System.out.println(ConText.analyzeExperiencer(s[2]));
      //System.out.println(ConText.analyzeTemporality(s[2]));
    }
    file.close();
  }

  // post: removes punctuations from a sentence
  public static String cleans(String line) {
    line = line.toLowerCase();
    if (line.contains("\""))
	    line = line.replaceAll("\"", "");
    if (line.contains(","))
	    line = line.replaceAll(",", "");  
    if (line.contains("."))
	    line = line.replaceAll("\\.", "");
    if (line.contains(";"))
	    line = line.replaceAll(";", "");
    if (line.contains(":"))
	    line = line.replaceAll(":", "");
    return line;
  }
}
