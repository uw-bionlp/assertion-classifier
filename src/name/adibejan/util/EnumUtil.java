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

import name.adibejan.math.IntSet;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.EnumSet;
import java.util.List;
import java.util.Arrays;

import static java.lang.System.out;

/**
 * Utils for enumerations
 *
 * @author Cosmin-Adrian Bejan
 * @version 1.0
 * @since JDK1.5 | Aug 02, 2005
 */
public class EnumUtil {
    /**
     * Converts a string representation into an enumset
     *
     */
    public static <T extends Enum<T>> EnumSet<T> getSet(Class<T> enumClass, String representation) {
        List<String> tokens = Arrays.asList(representation.split("\\s+"));
        EnumSet<T> flags = EnumSet.noneOf(enumClass);
        for (String token : tokens) {
            T flag = Enum.valueOf(enumClass, token);
            flags.add(flag);
        }
        return flags;
    }

    /**
     * Returns the field of a specified enum type (by field name)
     */
    public static <T extends Enum<T>> T getField(String fieldName, Class<T> enumClass) {
        return Enum.valueOf(enumClass, fieldName);
    }

    /**
     * Returns the field of a specified enum type (by field ordinal)
     * 
     * @param fieldOrdinal assume the enum has the default ordinals
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T getField(int fieldOrdinal, Class<T> enumClass) {
        Method method = null;
        T[] values = null;
        try {
            method = enumClass.getDeclaredMethod("values", (Class[]) null);
            values = (T[]) method.invoke(null, (Object[]) null);
        } catch (NoSuchMethodException nsme) {
            out.println(nsme.getMessage());
            nsme.printStackTrace();
        } catch (IllegalAccessException iae) {
            out.println(iae.getMessage());
            iae.printStackTrace();
        } catch (InvocationTargetException ita) {
            out.println(ita.getMessage());
            ita.printStackTrace();
        }

        if (fieldOrdinal < 0 || fieldOrdinal >= values.length)
            throw new IndexOutOfBoundsException(
                    "Invalid ordinal [" + fieldOrdinal + "] for class" + enumClass.getName());

        return values[fieldOrdinal];
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> IntSet getOrdinals(Class<T> enumClass, int shiftValue) {
        Method method = null;
        T[] values = null;
        try {
            method = enumClass.getDeclaredMethod("values", (Class[]) null);
            values = (T[]) method.invoke(null, (Object[]) null);
        } catch (NoSuchMethodException nsme) {
            out.println(nsme.getMessage());
            nsme.printStackTrace();
        } catch (IllegalAccessException iae) {
            out.println(iae.getMessage());
            iae.printStackTrace();
        } catch (InvocationTargetException ita) {
            out.println(ita.getMessage());
            ita.printStackTrace();
        }

        IntSet set = new IntSet();
        for (int i = 0; i < values.length; i++)
            set.add(values[i].ordinal() + 1);

        return set;
    }
}
