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

import java.io.Serializable;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Tool class for an (Object, Object, Object) triple.
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 * @ady.rep Triplet
 */
public class Triple<A,B,C> implements Serializable {
  private static final long serialVersionUID = 13234523451L;
  
  private A first;
  private B second;
  private C third;

  /**
   * Builds a <code>Triple</code> object from three objects of types A, B and C
   *
   */
  public Triple(A first, B second, C third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }
  
  /**
   * Sets the first value
   * 
   */
  public void setFirst(A first) {
    this.first = first;
  }

  /**
   * Accessor for the first value
   * 
   */
  public A getFirst() {
    return first;
  }

  /**
   * Sets the second value
   * 
   */
  public void setSecond(B second) {
    this.second = second;
  }
  
  /**
   * Accessor for the second value
   * 
   */
  public B getSecond() {
    return second;
  }

  /**
   * Sets the third value
   * 
   */
  public void setThird(C third) {
    this.third = third;
  }
  
  /**
   * Accessor for the third value
   * 
   */
  public C getThird() {
    return third;
  }
  
  /**
   * Inplementation of the <code>toString</code> method
   *
   */
  @Override
  public String toString() {
    //return "<"+first+","+second+","+third+">";
    return toString(" ");
  }
  
  public String toString(String delim) {
    return first.toString() + delim + second.toString() + delim + third.toString();
  }
  
  /**
   * The equals method for <code>Triple</code> objects
   *
   */
  @Override
    public boolean equals(Object that) {
    if (this == that ) return true;
    if (!(that instanceof Triple)) return false;
    Triple thattriplet = (Triple)that;
    return (first == null ? thattriplet.first == null : first.equals(thattriplet.first)) &&
      (second == null ? thattriplet.second == null : second.equals(thattriplet.second)) &&
      (third == null ? thattriplet.third == null : third.equals(thattriplet.third));
  }
  
  /**
   * Tests whether the first object in this <tt>Triple</tt> object is equal with <code>o</code>
   *
   * @param o the object to be tested with the fist object in the <tt>Triple</tt> object. 
   */
  public boolean equalsFirst(A o) {
    return first == null ? o == null : first.equals(o);
  }

  /**
   * Tests whether the second object in this <tt>Triple</tt> object is equal with <code>o</code>
   *
   * @param o the object to be tested with the second object in the <tt>Triple</tt> object. 
   */
  public boolean equalsSecond(B o) {
    return second == null ? o == null : second.equals(o);
  }
  
  /**
   * Tests whether the third object in this <tt>Triple</tt> object is equal with <code>o</code>
   *
   * @param o the object to be tested with the third object in the <tt>Triple</tt> object. 
   */
  public boolean equalsThird(C o) {
    return third == null ? o == null : third.equals(o);
  }
  
  /**
   * The hasCode method for <tt>Triple</tt> objects
   *
   */
  @Override
  public int hashCode() {
    return (((first == null) ? 0 : first.hashCode()) << 32) ^ 
      ((second == null) ? 0: second.hashCode() << 16) ^
      ((third == null) ? 0: third.hashCode());
  }
}
