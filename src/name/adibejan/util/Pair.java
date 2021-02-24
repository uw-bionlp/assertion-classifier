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

import java.io.Serializable;

/**
 * Tool class for an (Object, Object) pair.
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 */
public class Pair<A, B> implements Serializable {
    private static final long serialVersionUID = 11L;

    private A first;
    private B second;

    /**
     * Builds a <code>Pair</code> object from two objects of types A and B
     */
    public Pair(A first, B second) {
        setFirst(first);
        setSecond(second);
    }

    /**
     * Sets the first object
     *
     * @param first the new first object of this pair
     */
    public void setFirst(A first) {
        this.first = first;
    }

    /**
     * Get the first object
     *
     * @return the first object of this pair
     */
    public A getFirst() {
        return first;
    }

    /**
     * Sets the second object
     *
     * @param second the new second object of this pair
     */
    public void setSecond(B second) {
        this.second = second;
    }

    /**
     * Get the second object
     *
     * @return the second object of this pair
     */
    public B getSecond() {
        return second;
    }

    /**
     * The equals method for Pair objects
     *
     */
    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof Pair))
            return false;
        Pair thatpair = (Pair) that;
        return (first == null ? thatpair.first == null : first.equals(thatpair.first))
                && (second == null ? thatpair.second == null : second.equals(thatpair.second));
    }

    /**
     * Tests whether the first object in this pair is equal with <code>o</code>
     *
     * @param o the object to be tested with the fist object in the pair.
     */
    public boolean equalsFirst(A o) {
        return first == null ? o == null : first.equals(o);
    }

    /**
     * Tests whether the second object in this pair is equal with <code>o</code>
     *
     * @param o the object to be tested with the second object in the pair.
     */
    public boolean equalsSecond(B o) {
        return second == null ? o == null : second.equals(o);
    }

    /**
     * The hasCode method for Pair objects
     *
     */
    @Override
    public int hashCode() {
        return (((first == null) ? 0 : first.hashCode()) << 16) ^ ((second == null) ? 0 : second.hashCode());

        // alternative: return ((first+second)*(first+second) + 3*first + second)/2 ;
    }

    /**
     * The string format of a Pair object
     *
     * @return the string representation of a Pair
     */
    @Override
    public String toString() {
        // return "<"+first+"|"+second+">";
        return toString(" ");
    }

    public String toString(String delim) {
        return first.toString() + delim + second.toString();
    }
}
