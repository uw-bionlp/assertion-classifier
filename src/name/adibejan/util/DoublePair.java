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
import java.util.Comparator;

import name.adibejan.math.ArOp2;

import static java.lang.System.out;

/**
 * Tool class for an (double, double) pair.
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 */
public class DoublePair implements Serializable {
    private static final long serialVersionUID = 123L;
    protected double first;
    protected double second;

    /**
     * Constructs an <tt>DoublePair</tt> object from anoter <tt>DoublePair</tt>
     * object
     */
    public DoublePair(double f, double s) {
        setPair(f, s);
    }

    /**
     * Constructs an DoublePair object from two double values
     */
    public DoublePair(DoublePair pair) {
        setPair(pair.first, pair.second);
    }

    /**
     * Creates a new copy of this object
     */
    public DoublePair clone() {
        DoublePair p = new DoublePair(this);
        return p;
    }

    /**
     * Sets the values
     * 
     * @param f the new value for the first double
     * @param s the new value for the second double
     */
    public void setPair(double f, double s) {
        first = f;
        second = s;
    }

    /**
     * Sets the first value
     */
    public void setFirst(double f) {
        first = f;
    }

    /**
     * Accessor for the first value
     */
    public double getFirst() {
        return first;
    }

    /**
     * Sets the second value
     */
    public void setSecond(double s) {
        second = s;
    }

    /**
     * Accessor for the second value
     */
    public double getSecond() {
        return second;
    }

    /**
     * Inplementation of the <code>equals</code> method
     */
    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof DoublePair))
            return false;
        DoublePair thatPair = (DoublePair) that;
        return ((first == thatPair.first) && (second == thatPair.second));
    }

    /**
     * Inplementation of the <code>hasCode</code> method. It does not always ensures
     * uniqueness due to the loss of precission in conversion
     */
    @Override
    public int hashCode() {
        int f = (int) Double.doubleToLongBits(first);
        int s = (int) Double.doubleToLongBits(second);
        return ((f + s) * (f + s) + 3 * f + s) / 2;
    }

    /**
     * Inplementation of the <code>toString</code> method
     */
    @Override
    public String toString() {
        return "i2[" + first + "|" + second + "]";
    }

    /**
     * Converts this object to s string format representation
     * 
     * @return a <tt>String</tt> in the first:second format
     */
    public String getDotsFormat() {
        return "" + first + ":" + second;
    }

    /**
     * Prints to System.out a string format of this object
     *
     */
    public void println() {
        out.println(first + " - " + second);
    }

    /**
     * Prints to System.out a string format of this object
     *
     */
    public void print() {
        out.print(first + " - " + second);
    }

    /**
     * Test whether this <tt>DoublePair</tt> object is a point (i.e., its values are
     * equal).
     *
     */
    public boolean isPoint() {
        return first == second;
    }

    /**
     * Test whether this <tt>DoublePair</tt> object has its values in the increasing
     * order
     *
     */
    public boolean isIncreasing() {
        return first <= second;
    }

    /**
     * Test whether this <tt>DoublePair</tt> object has its values in the strictly
     * increasing order
     *
     */
    public boolean isStrictlyIncreasing() {
        return first < second;
    }

    /**
     * Computes a specific aritmetic operation between the two values in this pair
     */
    public double compute(ArOp2 op) {
        switch (op) {
            case ADD:
                return first + second;
            case SUB:
                return first - second;
            case MUL:
                return first * second;
            case DIV:
                if (second == 0)
                    throw new ArithmeticException("The second value in the pair is zero!");
                return first / second;
            case MOD:
                return first % second;
        }

        throw new UnsupportedOperationException("Operation not supported: " + op.name());
    }

    /**
     * Swaps the value of this <tt>DoublePair</tt> object
     */
    public void swap() {
        double aux;
        aux = first;
        first = second;
        second = aux;
    }

    /**
     * Returns a descending comparator based on the first double value
     *
     */
    public static Comparator<DoublePair> getComparatorByFirstValue() {
        return new Comparator<DoublePair>() {
            public int compare(DoublePair dp1, DoublePair dp2) {
                if (dp1.getFirst() < dp2.getFirst())
                    return -1;
                if (dp1.getFirst() > dp2.getFirst())
                    return 1;
                return 0;
            }
        };
    }

    /**
     * Compares the values of two <code>DoublePair</code> objects by their first
     * values
     */
    public int compareByFirstValue(DoublePair other) {
        if (getFirst() < other.getFirst())
            return -1;
        if (getFirst() > other.getFirst())
            return 1;
        return 0;
    }

    /**
     * Returns a descending comparator based on the second double value
     *
     */
    public static Comparator<DoublePair> getComparatorBySecondValue() {
        return new Comparator<DoublePair>() {
            public int compare(DoublePair dp1, DoublePair dp2) {
                if (dp1.getSecond() < dp2.getSecond())
                    return -1;
                if (dp1.getSecond() > dp2.getSecond())
                    return 1;
                return 0;
            }
        };
    }

    /**
     * Compares the values of two <code>DoublePair</code> objects by their second
     * values
     */
    public int compareBySecondValue(DoublePair other) {
        if (getSecond() < other.getSecond())
            return -1;
        if (getSecond() > other.getSecond())
            return 1;
        return 0;
    }
}
