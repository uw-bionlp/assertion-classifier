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

import name.adibejan.util.Alphabet;
import name.adibejan.util.Config;
import name.adibejan.util.EnumUtil;
import name.adibejan.util.IntPair;
import name.adibejan.util.ConfigurationException;
import name.adibejan.learning.features.DynamicFeatureManager;

import java.io.File;
import java.util.List;

/**
 * Assert System
 *
 * @author Nic Dobbins, adapted from AssertionClassification.java by Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | July 2011
 */
public class AssertionClassifier {
    private String ASSERTRESOURCES;
    private LibLinearDecoder decoder = null;
    private Alphabet<String> alph = null;
    private LexFeatureExtractor featureExtractor = new LexFeatureExtractor();
    private DynamicFeatureManager<AssertFeatures> featureManager = new DynamicFeatureManager<AssertFeatures>();

    public AssertionClassifier() {

        // Assert resources
        ASSERTRESOURCES = System.getProperty("ASSERTRESOURCES") + File.separator;
        if (ASSERTRESOURCES == null)
            ConfigurationException.throwPropertyNotSet("ASSERTRESOURCES");
        System.setProperty("OPENNLP_EN_TOKENMODEL_PATH", ASSERTRESOURCES + Config.getP("path.opennlp.en.token"));

        // Config
        AssertConfig.set();
        AssertConfig.setFeaturesMask(AssertConfig.GFB_FEATURE_SET_RESTRICT);
        AssertConfig.setFeaturesFilter(AssertConfig.GFB_FEATURE_SET_RESTRICT);
        featureManager.setFeatureTypeFilter(AssertConfig.getFeaturesFilter());
        featureExtractor.loadSignals(ASSERTRESOURCES);
        decoder = new LibLinearDecoder(ASSERTRESOURCES + Config.getP("run.learn.model_restrict"));
        alph = Alphabet.load(ASSERTRESOURCES + Config.getP("run.learn.alphabet_restrict"));
    }

    public String predict(String sentence, int first, int last) {
        List<String> features = featureExtractor.extractFeatures(sentence, new IntPair(first, last));
        return predictWithFeatures(features);
    }

    public String predict(String[] tokens, int first, int last) {
        List<String> features = featureExtractor.extractFeatures(tokens, new IntPair(first, last));
        return predictWithFeatures(features);
    }

    private String predictWithFeatures(List<String> features) {
        int[] featureIdxs = featureManager.getFeatureIndices(alph, features);
        int maxClassIdx = decoder.decode(featureIdxs);
        return EnumUtil.getField(maxClassIdx-1, AssertTargetClass.class).name().toLowerCase();
    }
}
