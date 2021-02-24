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

package name.adibejan.util.dynenum;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;

import static java.lang.System.out;

/**
 * Collection of <code>EnumValue</code> names
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | September 2011
 */
public class DynamicEnumSet<DE extends DynamicEnum> {
    private TreeSet<String> names; /* I trade the constant names order for log(n) access */
    private DE dynenum;

    /**
     * Creates an empty set. Note: The class cannot be instantiated from outside the
     * package
     */
    DynamicEnumSet(DE dynenum) {
        this.dynenum = dynenum;
        names = new TreeSet<String>();
    }

    /**
     * Returns the current set of <code>EnumValue</code> names
     */
    public Set<String> set() {
        return names;
    }

    /**
     * Returns a copy of the current set of <code>EnumValue</code> names
     */
    public Set<String> copySet() {
        Set<String> copyNames = new HashSet<String>(names);
        return copyNames;
    }

    /**
     * Adds a new <code>EnumValue</code> name to this set
     */
    public void add(String name) {
        if (!dynenum.contains(name))
            throw new IllegalArgumentException("[" + name + "] not in the dynenum!");
        names.add(name);
    }

    public void add(List<String> namesList) {
        for (String name : namesList) {
            add(name);
        }
    }

    /**
     * Removes a specific <code>EnumValue</code> name from this set
     */
    public void remove(String name) {
        if (!dynenum.contains(name))
            throw new IllegalArgumentException("[" + name + "] not in the dynenum!");
        names.remove(name);
    }

    public void remove(List<String> namesList) {
        for (String name : namesList) {
            remove(name);
        }
    }

    public int size() {
        return names.size();
    }

    public boolean isEmpty() {
        return names.isEmpty();
    }

    /**
     * Checks if a give <code>EnumValue</code> name is already in this set
     */
    public boolean contains(String name) {
        return names.contains(name);
    }

    public void removeAll(DynamicEnumSet<DE> other) {
        Set<String> copyNames = copySet(); // to avoid ConcurrentModificationException
        for (String name : copyNames)
            if (other.contains(name))
                remove(name);
    }

    /**
     * Copies the elements of this set except a specified element
     */
    public DynamicEnumSet<DE> copyMinus(String minusName) {
        if (!names.contains(minusName))
            throw new IllegalArgumentException("[" + minusName + "] not in the dynenum!");
        DynamicEnumSet<DE> newDES = new DynamicEnumSet<DE>(dynenum);
        for (String name : names) {
            if (!name.equals(minusName))
                newDES.names.add(name);
        }
        return newDES;
    }

    /**
     * Copies the elements of this set plus a specified element
     */
    public DynamicEnumSet<DE> copyPlus(String plusName) {
        if (names.contains(plusName))
            throw new IllegalArgumentException("[" + plusName + "] already in the set!");

        DynamicEnumSet<DE> newDES = new DynamicEnumSet<DE>(dynenum);
        for (String name : names) {
            newDES.names.add(name);
        }
        newDES.add(plusName);
        return newDES;
    }

    /**
     * Copies the elements of this set
     */
    public DynamicEnumSet<DE> copy() {
        DynamicEnumSet<DE> newDES = new DynamicEnumSet<DE>(dynenum);
        for (String name : names) {
            newDES.names.add(name);
        }
        return newDES;
    }

    public void print(String message) {
        out.print(message);
        for (String name : names)
            out.print(name + " ");
        out.println();
    }
}