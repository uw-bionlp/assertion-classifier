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

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
//import java.util.Set;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.decorator.TIntObjectMapDecorator;

import static java.lang.System.out;

/**
 * Data structure that keeps an efficient mapping between objects (items) and ints (indexes)
 * item -- (index, count)
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6, February 2011
 */
public class IntCounterHashtable implements java.io.Serializable {
  private static final long serialVersionUID = 77434675247L;
  private TIntObjectHashMap<MutableInt> map;

  /**
   * Builds the map of keys
   */
  public IntCounterHashtable() {
    map = new TIntObjectHashMap<MutableInt>();
  }
  
  /**
   * Tests whether a specified object is in the map
   */
  public boolean contains(int key) {
    return map.containsKey(key);
  }
  
  /**
   * Updates the counts for a specified object. 
   * 
   * The default start count is set to 1.
   */
  public void update(int key) {
    update(key, 1);
  }
  
  /**
   * Updates the counts for a specified object. 
   * 
   */
  public void update(int key, int startCounter) {
    if(map.containsKey(key)) {
      MutableInt counter = map.get(key);
      counter.inc();
    } else 
      map.put(key, new MutableInt(startCounter));
  }
  
  /**
   * Returns the frequency of a given object
   *
   * @return the count of a specified object. If the event is not in the hashtable, then it
   * will return 0
   */
  public int getCount(int key) {
    if(!map.containsKey(key)) return 0;
    return map.get(key).get();
  }

  /**
   * Returns the keys of this hashtable
   *
   */
  public int[] keys() {
    return map.keys();
  }

  public void print() {
    int[] keys = map.keys();
    for(int i = 0; i < keys.length; i++) 
      out.println(keys[i]+" "+map.get(keys[i]).get());    
  }

  public void printKeysSorted() {
    int[] keys = map.keys();
    Arrays.sort(keys);
    for(int i = 0; i < keys.length; i++) 
      out.println(keys[i]+" "+map.get(keys[i]).get());    
  }

  public void printKeysReverseSorted() {
    int[] keys = map.keys();
    Arrays.sort(keys);
    for(int i = keys.length - 1; i >= 0; i--) 
      out.println(keys[i]+" "+map.get(keys[i]).get());    
  }
  
  public void printCountersSorted() {
    TIntObjectMapDecorator<MutableInt> mapdec = new TIntObjectMapDecorator<MutableInt>(map);
    List<Map.Entry<Integer, MutableInt>> entries = new ArrayList<Map.Entry<Integer, MutableInt>>(mapdec.entrySet());
    Collections.sort(entries, new Comparator<Map.Entry<Integer, MutableInt>>() {
        public int compare(Map.Entry<Integer, MutableInt> c1, Map.Entry<Integer, MutableInt> c2) {
          return c2.getValue().compareTo(c1.getValue()); //descending
        }
      });
    for(Map.Entry<Integer, MutableInt> entry : entries) {
      out.println(entry.getKey().toString() + " " + entry.getValue().get());
    }
  }

  public void printCountersReverseSorted() {
    TIntObjectMapDecorator<MutableInt> mapdec = new TIntObjectMapDecorator<MutableInt>(map);
    List<Map.Entry<Integer, MutableInt>> entries = new ArrayList<Map.Entry<Integer, MutableInt>>(mapdec.entrySet());
    Collections.sort(entries, new Comparator<Map.Entry<Integer, MutableInt>>() {
        public int compare(Map.Entry<Integer, MutableInt> c1, Map.Entry<Integer, MutableInt> c2) {
          return c1.getValue().compareTo(c2.getValue());
        }
      });
    for(Map.Entry<Integer, MutableInt> entry : entries) {
      out.println(entry.getKey().toString() + " " + entry.getValue().get());
    }
  }

}
