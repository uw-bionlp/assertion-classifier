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

import static java.lang.System.out;

/**
 * Tester of the UW medical assertion classifier
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 */
public class Test {

    public static void main(String[] args) {
        System.setProperty("CONFIGFILE", "test/assertcls.properties");
        System.setProperty("ASSERTRESOURCES", "test/assert-resources");

        AssertionClassifier assertionClassifier = new AssertionClassifier();

        String[] s0 = new String[] { "Brother", "has", "dyspnea" };
        out.println("s0 = " + assertionClassifier.predict(s0, 2, 2));

        String s1 = "The patient, will-be discharged now with a final diagnosis of no acute asthmatic bronchitis with chronic obstructive pulmonary disease .";
        out.println("s1 = " + assertionClassifier.predict(s1, 13, 15));

        String s2 = "The patient, will-be discharged now with a final diagnosis of acute asthmatic bronchitis with chronic obstructive pulmonary disease.";
        out.println("s2 = " + assertionClassifier.predict(s2, 12, 14));

        String s3 = "He reports severe dyspnea on exertion.";
        out.println("s3 = " + assertionClassifier.predict(s3, 3, 3));

        String s4 = "Blunting of left CPA likely effusion.";
        out.println("s4 = " + assertionClassifier.predict(s4, 5, 5));

        String s5 = "Father has dyspnea.";
        out.println("s5 = " + assertionClassifier.predict(s5, 2, 2));

        String[] s6 = new String[] { "presents", "with", "mild", "dyslexia" };
        out.println("s6 = " + assertionClassifier.predict(s6, 3, 3));
    }
}