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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Enumeration;

import static java.lang.System.out;

/**
 * Simple configuration manager
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6, October 2010
 */
public class Config {
    public static String FILE_CONFIG;
    private static SortedProperties props;

    /* initialize properties */
    static {
        FILE_CONFIG = System.getProperty("CONFIGFILE");
        if (FILE_CONFIG == null)
            ConfigurationException.throwPropertyNotSet("CONFIGFILE");
        loadProps();
    }

    /**
     * Loads the config file
     */
    private static void loadProps() {
        props = new SortedProperties();
        try {
            props.load(new FileInputStream(FILE_CONFIG));
        } catch (FileNotFoundException fnfe) {
            out.println("Properties files not found: " + fnfe.getMessage());
        } catch (IOException ioe) {
            out.println(ioe.getMessage());
        }
    }

    /**
     * Saves the config file
     */
    public static void save() {
        try {
            props.store(new FileOutputStream(FILE_CONFIG), "");
        } catch (FileNotFoundException fnfe) {
            out.println("Properties files not found: " + fnfe.getMessage());
        } catch (IOException ioe) {
            out.println(ioe.getMessage());
        }
    }

    /**
     * Adds a new property
     * 
     * @param property the name of the new property
     * @param value    the value for <code>property</code>
     */
    public static void setP(String property, String value) {
        props.setProperty(property, value);
    }

    /**
     * Returns the value for a specified property
     */
    public static String getP(String property) {
        return props.getProperty(property);
    }

    public static Enumeration propertyNames() {
        return props.propertyNames();
    }
}