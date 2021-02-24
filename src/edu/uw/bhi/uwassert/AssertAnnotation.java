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

import name.adibejan.util.IntPair;

/**
 * Data structure for assert annotations
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | July 2011
 */
public class AssertAnnotation {
    private Concept concept;
    private AssertTargetClass assertionValue;

    /**
     * Creates a dummy annotation for real instances
     */
    public static AssertAnnotation getTestInstance(IntPair conceptBoundaries, String conceptName) {
        AssertAnnotation aa = new AssertAnnotation();
        Concept concept = new Concept();
        concept.setBoundaries(conceptBoundaries);
        concept.setName(conceptName);
        concept.setSentenceId(1);
        aa.concept = concept;
        aa.assertionValue = AssertTargetClass.ABSENT; // default value
        return aa;
    }

    public Concept getConcept() {
        return concept;
    }

    public AssertTargetClass getAssertionValue() {
        return assertionValue;
    }
}