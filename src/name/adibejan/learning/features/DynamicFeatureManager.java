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

import name.adibejan.util.Alphabet;
import name.adibejan.util.dynenum.DynamicEnum;
import name.adibejan.util.dynenum.DynamicEnumSet;

import java.util.List;

import edu.uw.bhi.uwassert.AssertConfig;
import edu.uw.bhi.uwassert.AssertTargetClass;

/**
 * Tool for transforming readable format feature files into SVM format files.
 * The management of features uses the <code>DynamicEnum</code> data structure.
 *
 * @author Cosmin Adrian Bejan, Nic Dobbins
 * @version 1.0
 * @since JDK1.6 | July 2011
 */

public class DynamicFeatureManager<DE extends DynamicEnum> extends FeatureManager {
    private DynamicEnumSet<DE> typeFilter;

    /**
     * Establishes the feature type filter
     */
    public void setFeatureTypeFilter(DynamicEnumSet<DE> typeFilter) {
        this.typeFilter = typeFilter;
    }

    /**
     * Verifies if all the configuration attributes are set
     */
    public void checkSetting() {
        super.checkSetting();
        assert typeFilter != null : "The feature filter is not set!";
    }

    public <TC extends Enum<TC>> int[] getFeatureIndices(Alphabet<String> falph, List<String> features) {
        checkSetting();
        DynamicFeaturePair featurePair = null;
        DataInstance currentInstance = null;
        int currentID = -1;
        instanceCounter = 1;

        try {
            for (String line : features) {
                if (line == null)
                    break;

                featurePair = DynamicFeaturePair.getMultiClassTargetInstance(line, AssertTargetClass.class, AssertConfig.featuresEnum);
                if (featurePair == null)
                    continue;
                
                if (typeFilter.contains(featurePair.featureType.name())) {
                    falph.update(featurePair.pairValue);
                    if (currentID != featurePair.instanceID) {
                        currentInstance = new DataInstance(featurePair, falph.getIndex(featurePair.pairValue));
                    } else {
                        currentInstance.update(featurePair, falph.getIndex(featurePair.pairValue));
                    }
                    currentID = featurePair.instanceID;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        return currentInstance.getKeys();
    }
}