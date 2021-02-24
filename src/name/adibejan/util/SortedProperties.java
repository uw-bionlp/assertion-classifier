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

import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.Comparator;

/**
 * The Properties class with sorted keys. Useful when it is desired to save the
 * properties with the keys sorted
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6, 10/09/2010
 */
public class SortedProperties extends Properties {
    static final long serialVersionUID = -4843927731308514695L;

    private static Comparator<Object> strobjComparator = new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
            return ((String) o1).compareTo((String) o2);
        }
    };

    @Override
    public Enumeration<Object> keys() {
        Enumeration<Object> keysEnum = super.keys();
        Vector<Object> keyList = new Vector<Object>();
        while (keysEnum.hasMoreElements()) {
            keyList.add(keysEnum.nextElement());
        }
        Collections.sort(keyList, strobjComparator);
        return keyList.elements();
    }
}