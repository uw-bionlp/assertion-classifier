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

package name.adibejan.io;

import name.adibejan.util.UnsupportedDuplicateException;

import java.util.Hashtable;

import static java.lang.System.out;

/**
 * Manager for administering <code>TextWriter</code> objects
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | July 2011
 */
public class TextWriterManager {
  private Hashtable<String, TextWriter> writers;

  /**
   * Builds the manager
   */
  public TextWriterManager() {
    writers = new Hashtable<String, TextWriter>();
  }

  /**
   * Adds a new <code>TextWriter</code> in the container. In the event the writer exists in the container,
   * an exception is thrown.
   *
   * @param key the keyword associated to the writer
   * @param filePath the file path of the new writer
   * @throws UnsupportedDuplicateException if <tt>key</tt> is already in the container
   */
  public void add(String key, String filePath) {
    add(key, new TextWriter(filePath, "UTF-8"));
  }
  
  /**
   * Adds a new <code>TextWriter</code> in the container. In the event the writer exists in the container,
   * an exception is thrown.
   *
   * @param key the keyword associated to the writer
   * @param writer the new writer to be added in the container
   * @throws UnsupportedDuplicateException if <tt>key</tt> is already in the container
   */
  public void add(String key, TextWriter writer) {
    if(writers.containsKey(key))
      throw new UnsupportedDuplicateException("["+key+"] already is in the container!");
    writers.put(key, writer);    
  }
  
  /**
   * Returns the wtiter for a specifird key of <code>null</code> if the container does not store such a key
   *
   */
  public TextWriter get(String key) {
    return writers.get(key);
  }

  /**
   * Tests if a key is already in the container
   */
  public boolean containsKey(String key) {
    return writers.containsKey(key);
  }
  
  public void close() {
    for(TextWriter writer : writers.values())
      writer.close();
    writers.clear();
  }
}