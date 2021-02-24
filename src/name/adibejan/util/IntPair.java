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

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import gnu.trove.list.array.TIntArrayList;

import static java.lang.System.out;

/**
 * Tool class for an (int, int) pair. This class can also be seen
 * as a class for interval objects.
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | June 2004
 * @ady.rep Int2Pair
 */
public class IntPair implements Comparable<IntPair>, java.io.Serializable {
  private static final long serialVersionUID = 97575822457L;
  
  protected int first;
  protected int second;  

  /**
   * Constructs an <tt>IntPair</tt> object from anoter <tt>IntPair</tt> object
   *
   */
  public IntPair(int f, int s) {
    setPair(f,s);
  }

  /**
   * Constructs an IntPair object from two int values
   *
   */
  public IntPair(IntPair pair) {
    setPair(pair.first, pair.second);
  }

  /**
   * Creates a int-int pair from a string representaion.
   * In the string representation the int values are separated by a specified delimiter. 
   * Examples of instances are: <code>IntPair("23:4",":");</code>, 
   * <code>IntPair("23:#:4",":#:");</code>, <code>IntPair("2:4",":");</code>.
   *
   * @param rep the formated <tt>String</tt> object
   * @param delim the delimiter that separates the int values in <tt>rep</tt>
   * @ady.rep loadFormat
   */  
  public static IntPair getInstance(String rep, String delim) {
    int pos = rep.indexOf(delim);
    if(pos == -1) throw new UnsupportedDataFormatException("["+delim+"] does not exist in ["+rep+"]");    
    int _f = Integer.parseInt(rep.substring(0,pos));
    int _s = Integer.parseInt(rep.substring(pos+delim.length(), rep.length()));
    return new IntPair(_f, _s);
  }
  
  /**
   * Creates a new copy of this object
   */
  public IntPair clone() {
    IntPair p = new IntPair(this);
    return p;
  }

  /**
   * Creates a new copy of this object with the elements swapped
   */
  public IntPair cloneSwap() {
    IntPair p = new IntPair(second, first);
    return p;
  }

  /**
   * Sets the values
   * 
   * @param f the new value for the first int
   * @param s the new value for the second int
   */
  public void setPair(int f, int s) {
    first = f;
    second = s;
  }

  /**
   * Sets the first value
   * 
   */
  public void setFirst(int f) {
    first = f;
  }
  
  /**
   * Accessor for the first value
   * 
   */
  public int getFirst() {
    return first;
  }
  
  /**
   * Sets the second value
   * 
   */
  public void setSecond(int s) {
    second = s;
  }
  
  /**
   * Accessor for the second value
   * 
   */
  public int getSecond() {
    return second;
  }

  /**
   * Increments the first value by 1
   * 
   */
  public void incFirst() {
    incFirst(1);
  }

  /**
   * Increments the first value
   * 
   * @param val the increment value
   */
  public void incFirst(int val) {
    first += val;
  }

  /**
   * Increments the second value by 1
   * 
   */
  public void incSecond() {
    incSecond(1);
  }
  
  /**
   * Increments the second value
   * 
   * @param val the increment value
   */
  public void incSecond(int val) {
    second += val;
  }

  /**
   * Increments the two pair values
   * 
   * @param pair increment for the pair values
   */
  public void inc(IntPair pair) {
    inc(pair.first, pair.second);
  }

  /**
   * Increments the two pair values. 
   * 
   * @param f increment for the first value
   * @param s increment for the second value
   */
  public void inc(int f, int s) {
    incFirst(f);
    incSecond(s);
  }

  /**
   * Inplementation of the <code>equals</code> method
   *
   */
  @Override
    public boolean equals(Object that) {
    if ( this == that ) return true;
    if ( !(that instanceof IntPair) ) return false;
    IntPair thatPair = (IntPair)that;
    return ((first == thatPair.first) && (second == thatPair.second));
  }

  /**
   * Tests whether the two values are equal
   */
  public boolean equals() {
    return (first == second);
  }

  /**
   * Inplementation of the <code>hasCode</code> method
   *
   */
  @Override
    public int hashCode() {
    return ((first+second)*(first+second) + 3*first + second)/2 ;
  }  

  /**
   * Inplementation of the <code>toString</code> method
   *
   */
  @Override
    public String toString() {
    return "i2["+first+"|"+second+"]";
  }

  /**
   * Converts this object to s string format representation
   * 
   * @return a <tt>String</tt> in the first:second format
   */
  public String getDotsFormat() {
    return getStringFormat(":");
  }
  
  /**
   * Converts this object to s string format representation
   * 
   * @return a <tt>String</tt> in the <code>first+delim+second</code> format
   */
  public String getStringFormat(String delim) {
    StringBuilder builder = new StringBuilder();
    builder.append(first);
    builder.append(delim);
    builder.append(second);
    return builder.toString();
  }

  /**
   * Shifts the pair values with the same value
   *
   * @param l shifting value for both int pairs
   */
  public void shift(int l) {
    first += l;
    second += l;
  }
  
  /**
   * Prints to System.out a string format of this object
   *
   */
  public void println() {
    out.println(first+" - "+second);
  } 
  
  /**
   * Prints to System.out a string format of this object
   *
   */
  public void print() {
    out.print(first+" - "+second);
  }

  /**
   * Test whether this <tt>IntPair</tt> object is a point (i.e., its values are equal).
   *
   */
  public boolean isPoint() {
    return first == second;
  }

  /**
   * Test whether this <tt>IntPair</tt> object has its values in the increasing order
   *
   */
  public boolean isIncreasing() {
    return first <= second;
  }

  /**
   * Test whether this <tt>IntPair</tt> object has its values in the strictly increasing order
   *
   */
  public boolean isStrictlyIncreasing() {
    return first < second;
  }

  /**
   * Swaps the value of this <tt>IntPair</tt> object
   * 
   * @ady.rep IntPair.inverse
   */
  public void swap() {
    int aux;
    aux = first;
    first = second;
    second = aux;
  }

  /**
   * Returns a comparator based on the first int 
   * @ady.rep getComparatorByFirstInt
   */
  public static Comparator<IntPair> getAscComparatorByFirstInt() {
    return new Comparator<IntPair>() {
      public int compare(IntPair i1, IntPair i2) {
        if( i1.getFirst() < i2.getFirst()) return -1;
        if( i1.getFirst() > i2.getFirst()) return 1;
        return 0;
      }
    };
  }

  public static Comparator<IntPair> getDescComparatorByFirstInt() {
    return new Comparator<IntPair>() {
      public int compare(IntPair i1, IntPair i2) {
        if( i2.getFirst() < i1.getFirst()) return -1;
        if( i2.getFirst() > i1.getFirst()) return 1;
        return 0;
      }
    };
  }

  public static Comparator<IntPair> getDescComparatorByDistanceAbs() {
    return new Comparator<IntPair>() {
      public int compare(IntPair i1, IntPair i2) {
        if( i2.distanceAbs() < i1.distanceAbs()) return -1;
        if( i2.distanceAbs() > i1.distanceAbs()) return 1;
        return 0;
      }
    };
  }


  /**
   * Returns a comparator based on the second int 
   *
   */
  public static Comparator<IntPair> getComparatorBySecondInt() {
    return new Comparator<IntPair>() {
      public int compare(IntPair i1, IntPair i2) {
        if( i1.getSecond() < i2.getSecond()) return -1;
        if( i1.getSecond() > i2.getSecond()) return 1;
        return 0;
      }
    };
  }

  /**
   * Compare <code>this</code> object with a given <tt>IntPair</tt> object using
   * Allen's temporal relations
   *
   * @return an <tt>TemporalRelation</tt> enum
   * @ady.rep getAllenRelations
   */
  public TemporalRelation getTemporalRelation(IntPair interval) {
    IntPair ithis = isIncreasing() ? this.clone() : this.cloneSwap();
    IntPair ithat = interval.isIncreasing() ? interval.clone() : interval.cloneSwap();
    
    if(ithis.second < ithat.first) return TemporalRelation.BEFORE;    
    if(ithat.second < ithis.first) return TemporalRelation.AFTER;    
    
    if ((!ithis.isPoint() || !ithat.isPoint())) {
      if(ithis.second == ithat.first) return TemporalRelation.MEETS;        
      if(ithat.second == ithis.first) return TemporalRelation.MET_BY;    
    }
    if(ithis.first < ithat.first && ithat.first < ithis.second && ithis.second < ithat.second)
      return TemporalRelation.OVERLAPS;    
    
    if(ithat.first < ithis.first && ithis.first < ithat.second && ithat.second < ithis.second)
      return TemporalRelation.OVERLAPPED_BY;
    
    if((!ithis.isPoint() && !ithat.isPoint())) {
      if(ithis.first == ithat.first && ithis.second < ithat.second) return TemporalRelation.STARTS;
      if(ithat.first == ithis.first && ithat.second < ithis.second) return TemporalRelation.STARTED_BY;
    }
    
    if(ithis.first > ithat.first && ithis.second < ithat.second) return TemporalRelation.DURING;    
    if(ithat.first > ithis.first && ithat.second < ithis.second) return TemporalRelation.CONTAINS;
    
    if ((!isPoint() && !ithat.isPoint())) {
      if(ithis.first > ithat.first && ithis.second == ithat.second) return TemporalRelation.FINISHES;
      if(ithat.first > ithis.first && ithat.second == ithis.second) return TemporalRelation.FINISHED_BY;
    }
    
    if(ithis.first == ithat.first && ithis.second == ithat.second) return TemporalRelation.EQUALS;
    
    throw new IllegalStateException("None of temporal relations meet!");
  }


  /**
   * Compares two intervals and returns an int value
   * 
   * @return 0 for EQUAL, <br/>
   * 1 for AFTER, MET_BY, OVERLAPPED_BY, STARTS, CONTAINS, and FINISHED_BY;<br/>
   * -1 for BEFORE, MEETS, OVERLAPS, STARTED_BY, DURING, and FINISHES.
   */
  public int compareTo(IntPair interval) {
    switch(getTemporalRelation(interval)) {
    case BEFORE: return -1;
    case AFTER: return 1;
    case MEETS: return -1;
    case MET_BY: return 1;
    case OVERLAPS: return -1;
    case OVERLAPPED_BY: return 1;
    case STARTS: return 1;
    case STARTED_BY: return -1;
    case DURING: return -1;
    case CONTAINS: return 1;
    case FINISHES: return -1;
    case FINISHED_BY: return 1;
    case EQUALS: return 0;
    }    
    throw new IllegalStateException("Cannot compare 2 intervals!");
  }
  
  public int compareFirstTo(IntPair other) {
    if( first < other.first) return -1;
    if( first > other.first) return 1;
    return 0;
  }
  
  public int compareSecondTo(IntPair other) {
    if(second < other.second) return -1;
    if(second > other.second) return 1;
    return 0;
  }

  /**
   * Returns the numeric distance between the int values of this object
   *
   */
  public int distance() {
    return first - second;
  }

  /**
   * Returns the absolute value of the distance between the int values of this object
   *
   */
  public int distanceAbs() {
    return Math.abs(distance());
  }

  /**
   * Tests whether this interval is included in a given interval
   * @assumption intervals are in increasing order
   */
  public boolean isIncluded(IntPair interval) {
    if(interval.first<= first && second <= interval.second) return true;
    return false;
  }

  /**
   * Tests whether this interval is after in a given point interval
   *
   */
  public boolean isAfter(int interval) {
    if(first >= interval && second >= interval) return true;
    return false;
  }

  /**
   * Tests whether this interval is strictly after in a given point interval
   *
   */
  public boolean isStrictlyAfter(int interval) {
    if(first > interval && second > interval) return true;
    return false;
  }

  /**
   * Tests whether this interval is strictly after in a given interval
   *
   */
  public boolean isStrictlyAfter(IntPair interval) {
    return getTemporalRelation(interval) == TemporalRelation.AFTER;
  }

  /**
   * Tests whether this interval is before in a given point interval
   *
   */
  public boolean isBefore(int interval) {
    if(first <= interval && second <= interval) return true;
    return false;
  }

  /**
   * Tests whether this interval is strictly before in a given point interval
   *
   */
  public boolean isStrictlyBefore(int interval) {
    if(first < interval && second < interval) return true;
    return false;
  }

  /**
   * Tests whether this interval is strictly after in a given interval
   *
   */
  public boolean isStrictlyBefore(IntPair interval) {
    return getTemporalRelation(interval) == TemporalRelation.BEFORE;
  }
  
  /**
   * Tests whether this interval includes in a given interval
   * @assumption the intervals are in increasing order
   */
  public boolean includes(IntPair interval) {
    if(first<= interval.first && interval.second <= second) return true;
    return false;
  }

  /**
   * Tests whether this interval includes in a point interval
   *
   */
  public boolean includes(int interval) {
    if(first <= interval && interval <= second) return true;
    if(second <= interval && interval <= first) return true;
    return false;
  } 

  /**
   * Tests whether this interval intersect another interval
   *
   */
  public boolean intersects(IntPair interval) {
    switch(getTemporalRelation(interval)) {
    case BEFORE: return false;
    case AFTER: return false;
      //case MEETS: return false;
      //case MET_BY: return false;
    case MEETS: return true;
    case MET_BY: return true;
    case OVERLAPS: return true;
    case OVERLAPPED_BY: return true;
    case STARTS: return true;
    case STARTED_BY: return true;
    case DURING: return true;
    case CONTAINS: return true;
    case FINISHES: return true;
    case FINISHED_BY: return true;
    case EQUALS: return true;
    }
    throw new IllegalStateException("Cannot compute the intersection between 2 intervals!");
  }  

  /**
   * Tests if a specified interval intersects any of the intervals from a list.
   * 
   * @return the position in the list of the first interval that intersects a specified interval.
   * If the specified interval does not intersects any of the intervals in the list, it returns -1.
   */
  public static int getFirstIntersection(IntPair interval, List<IntPair> intervals) {
    for(int i=0; i < intervals.size(); i++) 
      if(interval.intersects(intervals.get(i))) return i;
    return -1;
  }

  public static List<IntPair> getIntersections(IntPair interval, List<IntPair> intervals) {
    List<IntPair> intersections = new ArrayList<IntPair>();
    for(IntPair candidate : intervals)
      if(interval.intersects(candidate))
        intersections.add(candidate);
    return intersections;
  }  

  /**
   * Insert a specific interval into a list of intervals. The operation is performed such that
   * the resulting intervals will have the largest span and none of them intersect with each
   * other.
   *
   * @assumption all the intervals in the <code>intervals</code> list do not intersect each other
   */
  public static void insertSpecial(IntPair interval, List<IntPair> intervals) {
    List<IntPair> intersections = getIntersections(interval, intervals);
    
    if(intersections.isEmpty()) {
      intervals.add(interval);
    } else {
      Collections.sort(intersections, getDescComparatorByDistanceAbs());

      IntPair biggest = intersections.get(0);
      if(biggest.distanceAbs() < interval.distanceAbs()) {
        intervals.removeAll(intersections);
        intervals.add(interval);
      }      
    }
  }
  
  /**
   * Returns the closest interval from a list that is before a specified interval
   *
   * @assumption all intervals are increasing
   */
  public static IntPair getClosestBefore(IntPair reference, List<IntPair> intervals) {
    IntPair closestbefore = null;
    int min = Integer.MAX_VALUE;
    int diff = -1; 
    for(IntPair interval : intervals) {
      if(interval.getSecond() < reference.getFirst()) { //BEFORE
        diff = reference.getFirst() - interval.getSecond();
        if(min > diff) {
          min = diff;
          closestbefore = interval;
        }
      }       
    }
    return closestbefore;
  }

}
