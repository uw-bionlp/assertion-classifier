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

/**
 * Data structure for sentence level resources
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | July 2011
 */
public class SentenceLevelResources {
    public String[] toks;

    /**
     * Returns the sentence as a concatenation of stems (from the general splat stem sequence)
     */
    public String getTokenSequence(IntPair bnd, String delim) {
        return getTokenSequence(bnd.getFirst(), bnd.getSecond(), delim);
    }

    /**
     * Returns the sentence as a concatenation of tokens
     */
    public String getTokenSequence(int first, int second, String delim) {
        if (first < 0 || first > second || second >= toks.length)
            throw new IndexOutOfBoundsException("Illegal splat sequence boundaries [" + first + ":" + second + "]");

        StringBuilder builder = new StringBuilder();
        int i;
        for (i = first; i < second; i++) {
            builder.append(toks[i]);
            builder.append(delim);
        }
        builder.append(toks[i]);
        return builder.toString();
    }
}