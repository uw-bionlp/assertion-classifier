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
 * Runtime expception thrown when no duplicates are allowed in a particular
 * object collection.
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 */
public class UnsupportedDuplicateException extends RuntimeException {
    static final long serialVersionUID = 7547108684904668038L;

    /**
     * Constructs a <code>UnsupportedDuplicateException</code> with no detail
     * message.
     */
    public UnsupportedDuplicateException() {
        super();
    }

    /**
     * Constructs a <code>UnsupportedDuplicateException</code> with the specified
     * detail message.
     *
     * @param s the detail message.
     */
    public UnsupportedDuplicateException(String s) {
        super(s);
    }
}
