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

package negex;

//Author: Junebae Kye, Supervisor: Imre Solti
//Date: 06/27/2010
//
//Purpose: this program calls GenNegEx and defines negation scopes of sentences.  It determines whether a keyword is in the negation scope or not.
//If a keyword is in the negation scope, for example, 3 air hunger  "Denies shortness of breath, stridor, or AIR HUNGER."  Negated   Negated 
//Output looks as the following: Number TAB Phrase TAB Sentence TAB Dummystring TAB Decision TAB Decision by computer 

import java.io.*;

public class CallKit {

  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
	    System.out.println("Usage: please, specify a text file and an option of yes or no.");
	    return;
    }
    String filename = "CallKit.result";
    File existed = new File(filename);
    if (existed.exists())
	    existed.delete();
    FileWriter fw = new FileWriter(filename, true);
    boolean value;
    if (args[1].toLowerCase().equals("yes"))
	    value = true;
    else
	    value = false;      
    GenNegEx g = new GenNegEx(value);
    process(fw, g, args[0]);
    fw.close();
  }
    
  // post: process data and prints a result to a text file
  public static void process(FileWriter fw, GenNegEx g, String textfile) throws IOException {    
    BufferedReader file = new BufferedReader(new FileReader(textfile));
    String line;
    while ((line = file.readLine()) != null) {
	    String[] parts = line.split("\\t");
	    String sentence = cleans(parts[2]);
	    String scope = g.negScope(sentence);	    
	    if (scope.equals("-1")) 
        fw.write(line + "\t" + "Affirmed" + "\n");
	    else if (scope.equals("-2"))
        fw.write(line + "\t" + "Negated" + "\n");
	    else {
        String keyWords = cleans(parts[1]); 
        if (contains(scope, sentence, keyWords))
          fw.write(line + "\t" + "Negated" + "\n");
        else
          fw.write(line + "\t" + "Affirmed" + "\n");  
      }

	    // Prints out the scope on the screen for demonstration purposes.
	    // CHANGE as you like.
	    //System.out.println(scope);
      System.out.println(NegEx.analyzeNegation(parts[2], parts[1]));

    }
    file.close();
  }  

    
  // post: returns true if a keyword is in the negation scope. otherwise, returns false 
  public static boolean contains(String scope, String line, String keyWords) {
    //System.out.println("scope:"+scope);
    //System.out.println("line:"+line);
    //System.out.println("keys:"+keyWords);

    String[] token = line.split("\\s+");  
    String[] s = keyWords.trim().split("\\s+");  
    String[] number = scope.split("\\s+");
    int counts = 0;  
    
    int end = Integer.valueOf(number[2]);
    if(end >= token.length) end--;

    for (int i = Integer.valueOf(number[0]); i <= end; i++)
	    if (s.length == 1) {
        if (token[i].equals(s[0]))
          return true;
	    } else 
        if ((token.length - i) >= s.length) {
          String firstWord = token[i];
          if (firstWord.equals(s[0])) {
            counts++;
            for (int j = 1; j < s.length; j++) { 
              if (token[i + j].equals(s[j]))
                counts++;
              else {
                counts = 0;
                break;
              }
              if (counts == s.length)
                return true;
            }
          }
        }
    return false;
  }

  // post: removes punctuations
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
