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

import name.adibejan.util.IntPair;
import name.adibejan.nlp.TextProcessor;

/**
 * Data structure for a medical concept
 * Assumptions: no multiline concepts (each concept spans a single sentence)
 * 
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | July 2011
 */
public class Concept {
  private String name;
  private int sentid;
  private IntPair bnd;
  private String type;

  public void setName(String name) {
    this.name = name;
  } 

  public String getName() {
    return name;
  }
  
  public void setSentenceId(int sentid) {
    this.sentid = sentid;
  }
  
  public int getSentenceId() {
    return sentid;
  }
  
  public void setBoundaries(IntPair bnd) {
    this.bnd = bnd;
  }
  
  public IntPair getBoundaries() {
    return bnd;
  }
  
  public int getStart() {
    return bnd.getFirst();
  }
  
  public int getEnd() {
    return bnd.getSecond();
  }

  public void setType(String type) {
    this.type = type;
  }
  
  public String getType() {
    return type;
  }  
  
  /**
   * Checks whether the concept name is the same as indicated by the concept boundary
   */
  public void testSameName(SentenceLevelResources sentRes) {
    String stripName = name.replaceAll(" ", "");
    String stripTokenSequence = TextProcessor.flatten(sentRes.toks, getStart(), getEnd(), "");
    
    assert stripName.equalsIgnoreCase(stripTokenSequence) : "Different names concept_name["+stripName+"] token_sequence["+stripTokenSequence+"]";
  }
}