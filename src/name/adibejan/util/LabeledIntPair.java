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

package name.adibejan.util;

/**
 * Tool class for an (int, int) pair with a label attached.
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | June 2004
 */
public class LabeledIntPair extends IntPair {
    protected String label;

    /**
     * Constructs an IntPair from two int values
     *
     */
    public LabeledIntPair(int f, int s, String label) {
        super(f, s);
        this.label = label;
    }

    public LabeledIntPair(IntPair pair, String label) {
        super(pair);
        this.label = label;
    }

    /**
     * Preorder format: label followed by the two integers (token_DELIM1_X_DELIM2_Y)
     */
    public static LabeledIntPair getInstanceFromPreorderFormat(String format, String delim1, String delim2) {
        assert !delim1.equals(delim2) : "Delimiters are the same [" + delim1 + "] for [" + format + "]";

        int pos1 = format.lastIndexOf(delim1); // split by last position of delim1
        assert pos1 != -1 : "The delimiter [" + delim1 + "] does not exist in [" + format + "]!";
        String _l = format.substring(0, pos1);
        String bnd = format.substring(pos1 + delim1.length(), format.length());

        int pos2 = bnd.indexOf(delim2);
        assert pos2 != -1 : "The delimiter [" + delim2 + "] does not exist in [" + bnd + "]!";
        int _f = Integer.parseInt(bnd.substring(0, pos2));
        int _s = Integer.parseInt(bnd.substring(pos2 + delim2.length(), bnd.length()));
        return new LabeledIntPair(_f, _s, _l);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        return label + "[" + first + "|" + second + "]";
    }

    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof LabeledIntPair))
            return false;
        LabeledIntPair thatPair = (LabeledIntPair) that;
        return (label.equals(thatPair.label) && (first == thatPair.first) && (second == thatPair.second));
    }
}
