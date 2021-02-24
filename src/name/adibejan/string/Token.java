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

package name.adibejan.string;

import gnu.trove.set.hash.TCharHashSet;

/**
 * A set of utilities for testing various types of tokens
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 */
public class Token {
    /** Default delimiter is __II__ (used when representing n-grams, features) */
    public static final String DEFAULT_DELIM = "__II__";

    /** Default delimiter is __II__ (used when representing features separator) */
    public static final String SHARP_DELIM = "#";

    private static TCharHashSet puncts = null;

    /**
     * Resticts access to the instances of this object
     */
    private Token() {
    }

    /**
     * Loads all punctuation characters (as in the POSIX character class for
     * punctuation)
     */
    private synchronized static void loadPunctuations() {
        if (puncts == null)
            puncts = new TCharHashSet("!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~".toCharArray());
    }

    /**
     * Checks if all the characters of a specified string are punctuations
     */
    public static boolean isPunctuations(String str) {
        loadPunctuations();
        return puncts.containsAll(str.toCharArray());
    }
}