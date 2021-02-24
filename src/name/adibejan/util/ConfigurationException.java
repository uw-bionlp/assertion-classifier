/*
* This file is part of the Assertion Classifier.
*
* The contents of this file are subject to the LGPL License, Version 3.0.
*
* Copyright (C) 2017, The University of Washington
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
 * This expception is thrown when a configuration problem occurs.
 * 
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 */
public class ConfigurationException extends RuntimeException {
  static final long serialVersionUID = -8391656320532977393L;
  
  /**
   * Constructs a <code>ConfigurationException</code> with no detail message.
   */
  public ConfigurationException() {
    super();
  }

  /**
   * Constructs a <code>ConfigurationException</code> with the specified 
   * detail message. 
   *
   * @param s the detail message.
   */
  public ConfigurationException(String s) {
    super(s);
  }

  /**
   * Throws this exception with a specific message
   */
  public static void throwPropertyNotSet(String propName) {
    throw new ConfigurationException(propName+" is not set. Please use the -D option in your java command.");
  }
}
