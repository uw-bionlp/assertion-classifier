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

package name.adibejan.learning.features;

import static java.lang.System.out;

/**
 * Data structure for a feature pair. 
 * <br> The string representation for a feature pair is:
 * <br> id target feature_type#feature_value
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | July 2011
 */

public abstract class FeaturePair {
  public static final String DELIMITER = "#";
  
  protected int instanceID;
  protected String targetValue;  
  protected String featureValue;
  protected String pairValue;

  /**
   * Returns the id of this object
   */
  public int getId() {
    return instanceID;
  }

  /**
   * Returns the target value
   */
  public String getTargetValue() {
    return targetValue;
  }
  
  /**
   * Returns the feature value
   */
  public String getFeatureValue() {
    return featureValue;
  }
  
  /**
   * Prints the attributes of this <code>FeaturePair</code>
   */
  public abstract void print();
}