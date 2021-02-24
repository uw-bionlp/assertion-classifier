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

import name.adibejan.util.dynenum.DynamicEnum;

/**
 * Data structure for represeinting a dynamic enumeration
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | September 2011
 */
class AssertFeatures extends DynamicEnum {

    public AssertFeatures() {
        super();

        add("NEGPREFIX");
        add("NEGPREFIX_LEFTWINDOW");
        add("NEGPREFIX_RIGHTWINDOW");
        addNegPrefixes();

        add("POSSIBLE_SPECIAL");
        add("POSSIBLE_SPECIAL2");
        add("PRESENT_SPECIAL");
        add("ABSENT_SPECIAL");

        add("SIGNALCLOSESTLEFT_WINDOWSIZE");

        add("HAS_KINSHIP_INSENTENCE");

        add("CONCEPTSTEMEXPRESSION");
        add("NEGSIGNALCLOSESTLEFT_COMMARESTRICTED");
        add("QMARK_RIGHT");

        add("WORDLEFT1_UNCASE");
        add("WORDLEFT2_UNCASE");
        add("STEMLEFT1_UNCASE");
        add("TRIGRAMLEFT_UNCASE");
        add("TRIGRAMRIGHT_UNCASE");
        add("STEMTRIGRAMLEFT_UNCASE");

        add("NEGEX");
        add("NEGEX_W6");
        add("CONTEXT_EXPERIENCER");
        add("CONTEXT_TEMPORALITY_W6");

        add("WORD_POSITION");
        add("STEM");
    }

    public void addNegPrefixes() {
        for (String negPrefix : LexFeatureExtractor.negPrefixes) {
            add("NEGPREFIX__" + negPrefix);
            add("NEGPREFIX_LEFTWINDOW__" + negPrefix);
            add("NEGPREFIX_RIGHTWINDOW__" + negPrefix);
        }
    }
}
