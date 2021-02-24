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

import name.adibejan.util.UnsupportedDataFormatException;
import name.adibejan.util.IntCounterHashtable;

import java.util.Arrays;

/**
 * Data structure for a data instance.
 *
 * @author Cosmin Adrian Bejan, Nic Dobbins
 * @version 1.0
 * @since JDK1.6 | July 2011
 */

public class DataInstance {
    public int id;
    public String targetValue;
    private IntCounterHashtable features;

    public DataInstance(FeaturePair featurePair, int featureID) {
        features = new IntCounterHashtable();
        id = featurePair.getId();
        targetValue = featurePair.getTargetValue();
        features.update(featureID);
    }

    public void update(FeaturePair featurePair, int featureID) {
        if (id != featurePair.getId())
            throw new UnsupportedDataFormatException(
                    "Ids are not consistent [" + id + "] != [" + featurePair.instanceID + "]");
        if (!targetValue.equals(featurePair.getTargetValue()))
            throw new UnsupportedDataFormatException(
                    "Targets are not consistent [" + targetValue + "] != [" + featurePair.targetValue + "]");
        features.update(featureID);
    }

    public int[] getKeys() {
        return features.keys();
    }

    /**
     * Returns the following format: targetClass fid1:1 fid2:1 ...
     */
    public String getNoWeightingFormat() {
        int[] keys = features.keys();
        Arrays.sort(keys);

        StringBuilder builder = new StringBuilder();
        // builder.append("("+id+") ");
        builder.append(targetValue);
        builder.append(" ");
        for (int i = 0; i < keys.length - 1; i++) {
            builder.append(keys[i]);
            builder.append(":1 ");
        }
        builder.append(keys[keys.length - 1]);
        builder.append(":1");

        return builder.toString();
    }

    /**
     * Returns the following format: targetClass fid1:freq(fid1) fid2:freq(fid2) ...
     */
    public String getFrequencyWeightingFormat() {
        int[] keys = features.keys();
        Arrays.sort(keys);

        StringBuilder builder = new StringBuilder();
        // builder.append("("+id+") ");
        builder.append(targetValue);
        builder.append(" ");
        for (int i = 0; i < keys.length - 1; i++) {
            builder.append(keys[i]);
            builder.append(":");
            builder.append(features.getCount(keys[i]));
            builder.append(" ");
        }
        builder.append(keys[keys.length - 1]);
        builder.append(":");
        builder.append(features.getCount(keys[keys.length - 1]));

        return builder.toString();
    }

}