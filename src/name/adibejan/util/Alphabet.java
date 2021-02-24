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

import name.adibejan.io.TextWriter;

import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.List;
import java.util.ArrayList;

import static java.lang.System.out;

/**
 * Data structure that keeps an efficient mapping between objects (items) and
 * ints (indexes) item -- index
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6, October 2007
 */
public class Alphabet<T> implements java.io.Serializable {
    private static final long serialVersionUID = 345657823972429457L;
    public static final String DEFAULT_VALUE = "__NULL__";

    protected TObjectIntHashMap<T> map;
    protected List<T> entries;
    protected int size;
    protected String name;

    /**
     * Builds an empty alphabet
     */
    public Alphabet(String name) {
        this.name = name;
        size = 0;
        map = new TObjectIntHashMap<T>();
        entries = new ArrayList<T>();
    }

    /**
     * Returns the size of the alphabet
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the name of the alphabet
     */
    public String getName() {
        return name;
    }

    /**
     * Tests whether the alphabet contains a specified <code>item</code>
     */
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    /**
     * Updates the alphabet for a specified <code>item</code>
     */
    public void update(T item) {
        if (!map.containsKey(item)) {
            map.put(item, size);
            entries.add(item);
            size++;
        }
    }

    /**
     * Returns the corresponding index for a specified <code>item</code>
     *
     * @return -1 if the label is not in the map. (note: default returns o if map
     *         does not contain a specific key)
     */
    public int getIndex(T item) {
        if (!map.containsKey(item))
            return -1;
        return map.get(item);
    }

    /**
     * Returns the corresponding item for a specified <code>index</code>. The index
     * must be in the boundary limits.
     */
    public T getItem(int index) {
        if (index < 0 || index >= size)
            throw new ArrayIndexOutOfBoundsException("The index [" + index + "] is outside the alphabet bounds!");
        return entries.get(index);
    }

    /**
     * Clears the alphabet resources
     */
    public void clear() {
        map.clear();
        entries.clear();
        size = 0;
    }

    /**
     * Prints the alphabet items.
     *
     * @param writer the TextWriter where the items will be stored. Note: the writer
     *               will not be closed by this method.
     */
    public void printItems(TextWriter writer) {
        for (T entry : entries)
            writer.println(entry.toString());
    }

    /**
     * Prints the alphabet map (the item - index pairs).
     *
     * @param writer the TextWriter where the items will be stored. Note: the writer
     *               will not be closed by this method.
     */
    public void printMap(TextWriter writer) {
        for (T entry : entries)
            writer.println(entry.toString() + " " + map.get(entry));
    }

    /**
     * Prints the alphabet map (the item - index pairs).
     */
    public void printMap(String path, String name) {
        TextWriter writer = new TextWriter(path + File.separator + name);
        printMap(writer);
        writer.close();
    }

    /**
     * Reads an alphabet from a specific location if the location is not valid or
     * there is no alphabet at that location, create a new alphabet
     */
    public static <T> Alphabet<T> load(String fpath, String fname) {
        return load(fpath + File.separator + fname);
    }

    /**
     * Reads an alphabet from a specific location if the location is not valid or
     * there is no alphabet at that location, create a new alphabet
     */
    @SuppressWarnings("unchecked")
    public static <T> Alphabet<T> load(String filePath) {
        Alphabet<T> alphabet = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
            alphabet = (Alphabet<T>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            out.println("There is no alphabet at [" + filePath + "]");
            out.println("Creating a new alphabet [" + filePath + "] ... ");
            alphabet = new Alphabet<T>("default");
        }
        return alphabet;
    }

    /**
     * Writes this alphabet into a specific location
     */
    public static <T> void save(Alphabet<T> alphabet, String fpath, String fname) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fpath + File.separator + fname));
            oos.writeObject(alphabet);
            oos.close();
        } catch (IOException e) {
            out.println("Error writing alphabet to " + fpath + File.separator + fname);
        }
    }
}
