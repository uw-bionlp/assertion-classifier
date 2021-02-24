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

package context;

public class ConText {
  private static GenExperiencer e = new GenExperiencer();
  private static GenTemporality t = new GenTemporality();	  
  
  public static String analyzeExperiencer(String sentence) {
    return e.getExperiencer(MainConText.cleans(sentence));
  }

  public static String analyzeTemporality(String sentence) {
    return t.getTemporality(MainConText.cleans(sentence));
  }  
}