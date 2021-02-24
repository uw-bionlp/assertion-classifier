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

package name.adibejan.nlp;

import name.adibejan.util.IntPair;
import name.adibejan.util.UnsupportedDataFormatException;

import java.util.List;
import java.util.ArrayList;

/**
 * Tool class for processing text
 *
 * @author Cosmin-Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 * @ady.rep SentenceSplitter
 */

public class TextProcessor {
    public enum Task {
        SPLIT_TOK, 
        REM_PCT, 
        REM_STOP, 
        REM_PCTSTOP
    };

    /**
     * Computes new indexes of a given token interval. This method isusually used
     * when performing annotation realignment due to tokenization.
     *
     */
    public static IntPair getTokenIndexes(List<String> otoks, List<String> ntoks, IntPair obnd) {
        if (obnd.getFirst() < 0 || obnd.getFirst() > obnd.getSecond() || obnd.getSecond() >= otoks.size())
            throw new IndexOutOfBoundsException(
                    "Original boundaries " + obnd + " are out of bounds [0," + otoks.size() + "]");

        List<IntPair> indexes = alignTokenIndexes(otoks, ntoks);
        IntPair left = indexes.get(obnd.getFirst());
        IntPair right = indexes.get(obnd.getSecond());

        return new IntPair(left.getFirst(), right.getSecond());
    }

    /**
     * Computes token indexes. Each otok[i] is associated with an IntPair that
     * represents the boundary indexes in the ntoks list.
     * 
     * @param otoks original tokens
     * @param ntoks new tokens
     * @assumption sizeOf(otoks) &gt;= sizeOf(ntoks)
     * @assumption flatten(otoks).replaceAll("//s+") equalsWith
     *             flatten(ntoks).replaceAll("//s+")
     */
    public static List<IntPair> alignTokenIndexes(List<String> otoks, List<String> ntoks) {
        List<IntPair> indexes = new ArrayList<IntPair>();
        String otok = null;
        String ntok = null;
        int otokIDX, ocharIDX, ntokIDX, ncharIDX;

        ntokIDX = 0;
        for (otokIDX = 0; otokIDX < otoks.size(); otokIDX++) {
            otok = otoks.get(otokIDX);
            ocharIDX = 0;

            boolean matchTokens = false;
            int startIDX = ntokIDX;
            while (!matchTokens) { /* solve one orig token in one step */
                ntok = ntoks.get(ntokIDX++);
                ncharIDX = 0;

                while (true) {
                    if (otok.charAt(ocharIDX) != ntok.charAt(ncharIDX)) {
                        throw new UnsupportedDataFormatException("Different tokens original[" + otok + "][" + ocharIDX
                                + "] != tokenized[" + ntok + "][" + ncharIDX + "]");
                    }

                    if (ocharIDX + 1 == otok.length()) { /* last char index in otok.tok */
                        if (ncharIDX + 1 == ntok.length()) { /* last char index in stok.rawtok */
                            matchTokens = true;
                            break;
                        } else { /* last char index in ftok, but not in stok - should not happen */
                            throw new UnsupportedDataFormatException("Longer new token: orig[" + otok + "][" + ocharIDX
                                    + "] != tokenized[" + ntok + "][" + ncharIDX + "]");
                        }
                    } else {
                        ocharIDX++;
                        if (ncharIDX + 1 == ntok.length())
                            break; /* get next stok */
                        ncharIDX++;
                    }
                }
            }
            indexes.add(new IntPair(startIDX, ntokIDX - 1));
        }
        return indexes;
    }

    /**
     * Flatten an array with string elements into a string
     */
    public static String flatten(String[] words) {
        return flatten(words, " ");
    }

    /**
     * Flatten a array with string elements into a string
     */
    public static String flatten(String[] words, String delim) {
        if (words.length == 0)
            return "";

        return flatten(words, 0, words.length - 1, delim);
    }

    /**
     * Flatten a array with string elements into a string
     */
    public static String flatten(String[] words, int start, int end, String delim) {
        if (start < 0 || end < 0 || start >= words.length || end >= words.length)
            throw new IndexOutOfBoundsException("start: " + start + " | end: " + end + " | length:" + words.length);
        if (start > end)
            throw new IllegalArgumentException("start: " + start + " > end: " + end);

        StringBuilder builder = new StringBuilder();
        builder.append(words[start]);
        for (int i = start + 1; i <= end; i++) {
            builder.append(delim);
            builder.append(words[i]);
        }
        return builder.toString();
    }
}
