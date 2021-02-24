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

package edu.uw.bhi.uwassert;

import name.adibejan.learning.Classifier;
import name.adibejan.learning.ClassificationAlgorithm;

import name.adibejan.util.dynenum.DynamicEnumSet;

import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;

/**
 * System config
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | October 2011
 */
public class AssertConfig {
  /* Train: 94.81  Test-micro: 93.45 Test-macro: 75.71 */
  public static final String GFB_FEATURE_SET_RESTRICT = "ABSENT_SPECIAL CONCEPTSTEMEXPRESSION CONTEXT_EXPERIENCER CONTEXT_TEMPORALITY_W6 HAS_KINSHIP_INSENTENCE NEGEX NEGEX_W6 NEGPREFIX_LEFTWINDOW__de NEGPREFIX_LEFTWINDOW__mis NEGPREFIX_RIGHTWINDOW__ab NEGPREFIX__im NEGPREFIX__mis NEGSIGNALCLOSESTLEFT_COMMARESTRICTED POSSIBLE_SPECIAL POSSIBLE_SPECIAL2 PRESENT_SPECIAL QMARK_RIGHT SIGNALCLOSESTLEFT_WINDOWSIZE STEM STEMLEFT1_UNCASE STEMTRIGRAMLEFT_UNCASE WORDLEFT1_UNCASE WORDLEFT2_UNCASE WORD_POSITION";
  
  public static AssertFeatures featuresEnum;
  public static DynamicEnumSet<AssertFeatures> featuresMask;   
  public static DynamicEnumSet<AssertFeatures> featuresFilter;

  /**
   *
   */
  @SuppressWarnings("unchecked")
  public static void set() { 
    Classifier.setAlgorithm(ClassificationAlgorithm.SVM_LIBLINEAR);    
    featuresEnum = new AssertFeatures();
    setFeaturesMask(GFB_FEATURE_SET_RESTRICT);
    initFeaturesFilter();
  }
  
  /**
   * Checks if a specific feature type is selected in this configuration
   */
  public static boolean isSelected(String featureType) {
    return featuresMask.contains(featureType);
  }

  /**
   *
   */
  @SuppressWarnings("unchecked")
    public static void initFeaturesFilter() {
    featuresFilter = featuresEnum.allOf();    //for extraction        
  }

  public static void setFeaturesFilter(DynamicEnumSet<AssertFeatures> filter) {
    featuresFilter = filter;
  }
  
  @SuppressWarnings("unchecked")
  public static void setFeaturesFilter(String featuresString) {
    featuresFilter = featuresEnum.noneOf();
    List<String> featuresList = Arrays.asList(featuresString.split("\\s+"));
    for(String featureItem : featuresList) 
      featuresFilter.add(featureItem);
  }  
  
  @SuppressWarnings("unchecked")
    public static void setFeaturesMask(String featuresString) {
    featuresMask = featuresEnum.noneOf();
    List<String> featuresList = Arrays.asList(featuresString.split("\\s+"));
    for(String featureItem : featuresList) 
      featuresMask.add(featureItem);
  }
    
  public static DynamicEnumSet<AssertFeatures> getFeaturesFilter() {
    return featuresFilter;
  }
}