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

import gnu.trove.map.hash.TObjectIntHashMap;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import static java.lang.System.out;

/**
 * Data structure for represeinting a dynamic enumeration.</br>
 *
 * Advantages:</br>
 * - add and remove fileds at runtime</br>
 * - the field name can be any string (not only ascii, _ and digits)</br>
 *
 * Limitations:</br>
 * - cannot be used in switch</br>
 * - cannot be used as consts</br>
 * - cannot append more attributes to the fileds (yet)</br>
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | September 2011
 */
public class DynamicEnum {
    protected TObjectIntHashMap<String> map;
    protected List<EnumValue> values;
    protected int size;

    /**
     * Creates a dynamic enumeration structure
     */
    protected DynamicEnum() {
        map = new TObjectIntHashMap<String>();
        values = new ArrayList<EnumValue>();
        size = 0;
    }

    /**
     * Appends a new <code>EnumValue</code> object specified name
     */
    public void add(String name) {
        if (!map.containsKey(name)) {
            map.put(name, size);
            values.add(new EnumValue(name, size));
            size++;
        }
    }

    /**
     * Checks if a give <code>EnumValue</code> name is already in this structure
     */
    public boolean contains(String name) {
        return map.containsKey(name);
    }

    /**
     * Checks if a give <code>EnumValue</code> ordinal is already in this structure
     */
    public boolean contains(int ordinal) {
        if (map.isEmpty())
            return false;
        if (ordinal >= 0 && ordinal < size)
            return true;
        return false;
    }

    /**
     * Returns the size of this structure
     */
    public int size() {
        return size;
    }

    /**
     * Given a name, returns its corresponding <code>EnumValue</code> object
     */
    public EnumValue get(String name) {
        if (!contains(name))
            throw new IllegalArgumentException("[" + name + "] not in the map!");
        return values.get(map.get(name));
    }

    /**
     * Given an ordinal, returns its corresponding <code>EnumValue</code> object
     */
    public EnumValue get(int ordinal) {
        if (!contains(ordinal))
            throw new IllegalArgumentException("[" + ordinal + "] not in the map!");
        return values.get(ordinal);
    }

    /**
     * Returns the names of this enumeration
     */
    public Set<String> getNames() {
        return map.keySet();
    }

    /**
     * Returns an empty <code>DynamicEnumSet</code> for this structure
     */
    public DynamicEnumSet noneOf() {
        DynamicEnumSet<DynamicEnum> set = new DynamicEnumSet<DynamicEnum>(this);
        return set;
    }

    /**
     * Returns a <code>DynamicEnumSet</code> that contains all the names of this
     * structure
     */
    public DynamicEnumSet allOf() {
        DynamicEnumSet<DynamicEnum> set = new DynamicEnumSet<DynamicEnum>(this);
        for (EnumValue value : values) {
            set.add(value.name());
        }
        return set;
    }

    public void println() {
        for (EnumValue value : values)
            out.println(value.name());
    }
}