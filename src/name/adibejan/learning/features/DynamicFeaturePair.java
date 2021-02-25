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

import name.adibejan.string.StringUtil;
import name.adibejan.util.EnumUtil;
import name.adibejan.util.Pair;

import name.adibejan.util.dynenum.EnumValue;
import name.adibejan.util.dynenum.DynamicEnum;

import static java.lang.System.out;

/**
 * Data structure for a feature pair with the feature type from a DynamicEnum.
 *
 * @author Cosmin Adrian Bejan, Nic Dobbins
 * @version 1.0
 * @since JDK1.6 | September 2011
 */
public class DynamicFeaturePair extends FeaturePair {
    public EnumValue featureType;

    /**
     * Builds a <code>FeaturePair</code> instance from a given string representation
     * (for multi class taget features)
     *
     * Format: id target feature_type#feature_value Example: 0000001 positive
     * WORDLEFT2#edges
     */
    public static <TC extends Enum<TC>> DynamicFeaturePair getMultiClassTargetInstance(String rep,
            Class<TC> targetClass, DynamicEnum dynenum) {
        String[] toks = rep.split("\\s+");
        DynamicFeaturePair instance = getPartialInstance(toks, dynenum);
        if (instance == null)
            return null;

        TC target = EnumUtil.getField(toks[1].toUpperCase(), targetClass);
        instance.targetValue = "" + (target.ordinal() + 1);

        return instance;
    }

    /**
     * Builds a partial <code>FeaturePair</code> instance from a given string
     * representation (the common code for multi and binary class target features)
     */
    private static DynamicFeaturePair getPartialInstance(String[] toks, DynamicEnum dynenum) {
        if (toks.length != 3)
            return null;

        DynamicFeaturePair instance = new DynamicFeaturePair();
        instance.instanceID = Integer.parseInt(toks[0]);
        instance.pairValue = toks[2];
        Pair<String, String> featurePair = StringUtil.split2First(toks[2], DELIMITER);
        instance.featureType = dynenum.get(featurePair.getFirst());
        if (featurePair.getSecond().equals(""))
            return null;
        instance.featureValue = featurePair.getSecond();

        return instance;
    }

    public void print() {
        out.println("[" + instanceID + "] [" + targetValue + "] [" + featureType.name() + "] [" + featureValue + "]");
    }

}