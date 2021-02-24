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

package name.adibejan.math;

import name.adibejan.util.UnsupportedDuplicateException;

import gnu.trove.list.array.TIntArrayList;

/**
 * Set of int primitives
 * 
 *
 * @author Cosmin-Adrian Bejan - ady@hlt.utdallas.edu
 * @version 1.0
 * @since JDK1.6
 */

public class IntSet {
    /*
     * the resaon for not using the TIntHashSet class is because of missing some
     * basic functionalities such as size(), isEmpty(), and get() which would
     * require a little bit of work to implement them
     */
    private TIntArrayList set;

    public IntSet() {
        set = new TIntArrayList();
    }

    public IntSet(int MAX, boolean fromZero) {
        assert MAX > 0 : "MAX [" + MAX + "] is not > 0 !";
        set = new TIntArrayList();
        int start = fromZero ? 0 : 1;
        for (int elem = start; elem <= MAX; elem++)
            set.add(elem);
    }

    public IntSet clone() {
        IntSet newiset = new IntSet();
        for (int i = 0; i < set.size(); i++)
            newiset.set.add(set.get(i));

        return newiset;
    }

    /**
     * Creates a <tt>IntSet</tt> instance of a specified size. The elements the new
     * set are consecutive with the smallest element starting from a specified
     * initial value
     *
     * @param init the value of the smallest element
     * @param size the size of the new set
     */
    public static IntSet getInstance(int init, int size) {
        IntSet s = new IntSet();
        for (int i = 0; i < size; i++)
            s.add(init + i);
        return s;
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public boolean contains(int elem) {
        return set.contains(elem);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntSet))
            return false;
        IntSet otherSet = (IntSet) obj;
        set.sort();
        otherSet.set.sort();
        return set.equals(otherSet.set);
    }

    /**
     * Adds a new element in the set.
     *
     * @param elem the new element to be added
     */
    public void add(int elem) {
        if (!set.contains(elem))
            set.add(elem);
    }

    /**
     * Adds a <code>IntSet</code> in this set.
     *
     * @param otherSet the new set to be added
     */
    public void add(IntSet otherSet) {
        for (int i = 0; i < otherSet.size(); i++)
            add(otherSet.set.get(i));
    }

    /**
     * Adds a new element in the set and verifies is the element already exists.
     *
     * @param elem the new element to be added
     * @throws UnsupportedDuplicateException if <tt>elem</tt> is already in the set
     */
    public void addCheck(int elem) {
        if (set.contains(elem))
            throw new UnsupportedDuplicateException("[" + elem + "] already in the int set");
        set.add(elem);
    }

    public boolean remove(int offset) {
        assert 0 <= offset && offset < set.size() : "Offset [" + offset + "] out of bounds!";
        return set.remove(offset);
    }

    public void clear() {
        set.clear();
    }

    public void sort() {
        set.sort();
    }

    /**
     * Returns an element at a specified offset
     *
     * @param offset specifies the index in this set of the elemet to be returned
     * @return the element at the <tt>offset</tt>
     */
    public int get(int offset) {
        if (offset < 0 || offset >= set.size())
            throw new IndexOutOfBoundsException("offset out of set bounds");

        return set.get(offset);
    }

    public boolean isStrictlyIncluded(IntSet otherSet) {
        if (size() >= otherSet.size())
            return false;
        for (int i = 0; i < set.size(); i++)
            if (!otherSet.contains(set.get(i)))
                return false;
        return true;
    }

    public boolean isEqualOrIncluded(IntSet otherSet) {
        if (size() > otherSet.size())
            return false;
        for (int i = 0; i < set.size(); i++)
            if (!otherSet.contains(set.get(i)))
                return false;
        return true;
    }

    public IntSet intersect(IntSet otherSet) {
        IntSet intersectSet = new IntSet();
        for (int i = 0; i < set.size(); i++)
            if (otherSet.contains(set.get(i)))
                intersectSet.add(set.get(i));
        return intersectSet;
    }

    public static IntSet intersect(IntSet set1, IntSet set2) {
        IntSet intersect = new IntSet();
        for (int i = 0; i < set1.size(); i++) {
            if (set2.contains(set1.get(i)))
                intersect.add(set1.get(i));
        }
        return intersect;
    }

    /**
     * this \ other
     */
    public IntSet difference(IntSet otherSet) {
        IntSet differenceSet = new IntSet();
        for (int i = 0; i < set.size(); i++)
            if (!otherSet.contains(set.get(i)))
                differenceSet.add(set.get(i));
        return differenceSet;
    }

    /**
     * (this \ other) U (other \ this)
     *
     */
    public IntSet symmetricDifference(IntSet otherSet) {
        if (otherSet.isEmpty())
            return this.clone();
        if (isEmpty())
            return otherSet.clone();

        IntSet sd = new IntSet();
        for (int i = 0; i < this.size(); i++)
            if (!otherSet.contains(this.get(i)))
                sd.add(this.get(i));
        for (int i = 0; i < otherSet.size(); i++)
            if (!this.contains(otherSet.get(i)))
                sd.add(otherSet.get(i));

        return sd;
    }

    /**
     * this U other
     */
    public IntSet reunion(IntSet otherSet) {
        IntSet reunion = new IntSet();
        // reunion.set = (TIntArrayList)set.clone();
        for (int i = 0; i < set.size(); i++)
            reunion.add(set.get(i));
        for (int i = 0; i < otherSet.size(); i++)
            reunion.add(otherSet.get(i));
        return reunion;
    }

    public void reunionInPlace(IntSet otherSet) {
        add(otherSet);
    }

    /**
     * Finds the next index array for n choose k method
     */
    private void updateChooseKIndices(int[] currentIdx, int[] finalIdx) {
        int n = set.size();
        int k = currentIdx.length;

        for (int i = k - 1; i >= 0; i--) {
            if (currentIdx[i] != finalIdx[i]) {
                currentIdx[i]++;
                for (int j = i + 1; j < k; j++)
                    currentIdx[j] = currentIdx[j - 1] + 1;
                return;
            }
        }
    }

}