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
 * Wrapper for int primitives that supports performing aritmetic features in
 * place
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 * @ady.rep IntCounter
 */
public class MutableInt implements Comparable<MutableInt> {
    private int value;

    /**
     * Creates a new instance with default value on 0
     */
    public MutableInt() {
        value = 0;
    }

    /**
     * Creates a new instance with a specified int value
     */
    public MutableInt(int value) {
        this.value = value;
    }

    /**
     * Increment the int value by 1
     *
     */
    public void inc() {
        value++;
    }

    public void inc(int step) {
        value += step;
    }

    @Override
    public int compareTo(MutableInt other) {
        if (value < other.value)
            return -1;
        if (value > other.value)
            return 1;
        return 0;
    }

    public void set(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
