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

package name.adibejan.io;

import java.io.File;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.BufferedWriter;

import static java.lang.System.out;

/**
 * Writer for text content
 * 
 * @ady.rep OutWriter and OutWriter2
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 */
public class TextWriter {
    private PrintWriter pw;

    /**
     * Creates a TextWriter to the System.out with the <code>"UTF-8"</code> charset
     * encoding.
     *
     */
    public TextWriter() {
        try {
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out, "UTF-8")));
        } catch (UnsupportedEncodingException uee) {
            out.println("Error in getting the System.out:" + uee.getMessage());
            uee.printStackTrace();
        }
    }

    /**
     * Creates a TextWriter with a specified file name, default append mode (false)
     * and charset encoding.
     *
     * @param fileName the name of the file
     */
    public TextWriter(String fileName) {
        setWriter(new File(fileName), false);
    }

    /**
     * Creates a TextWriter with a specified file name, append mode, and default
     * charset encoding.
     *
     * @param fileName the name of the file
     * @param append   if true it will perform the writing opperations in append
     *                 mode
     */
    public TextWriter(String fileName, boolean append) {
        setWriter(new File(fileName), append);
    }

    /**
     * Creates a TextWriter with a specified file name, charset encoding and default
     * append mode (false).
     *
     * @param fileName    the name of the file
     * @param charsetName the charset encoding. See
     *                    <code>java.nio.charset.Charset</code> for options of
     *                    encodings.
     */
    public TextWriter(String fileName, String charsetName) {
        setWriter(new File(fileName), false, charsetName);
    }

    /**
     * Creates a TextWriter with a specified file name, append mode, and charset
     * encoding.
     *
     * @param fileName    the name of the file
     * @param append      if true it will perform the writing opperations in append
     *                    mode
     * @param charsetName the charset encoding. See
     *                    <code>java.nio.charset.Charset</code> for options of
     *                    encodings.
     */
    public TextWriter(String fileName, boolean append, String charsetName) {
        setWriter(new File(fileName), append, charsetName);
    }

    /**
     * Creates a TextWriter with a specified file, append mode, and charset
     * encoding.
     *
     * @param file        the file to write into
     * @param append      if true it will perform the writing opperations in append
     *                    mode
     * @param charsetName the charset encoding. See
     *                    <code>java.nio.charset.Charset</code> for options of
     *                    encodings.
     */
    public TextWriter(File file, boolean append, String charsetName) {
        setWriter(file, append, charsetName);
    }

    /**
     * Creates a TextWriter with a specified file name, append mode, and option for
     * <code>"UTF-8"</code> charset encoding.
     *
     * @param fileName    the name of the file
     * @param append      if true it will perform the writing opperations in append
     *                    mode
     * @param utf8Charset if true it will use the <code>"UTF-8"</code> charset
     *                    encoding.
     */
    public TextWriter(String fileName, boolean append, boolean utf8Charset) {
        if (utf8Charset)
            setWriter(new File(fileName), append, "UTF-8");
        else
            setWriter(new File(fileName), append);
    }

    /**
     * Creates a TextWriter with a specified file, append mode, and option for
     * <code>"UTF-8"</code> charset encoding.
     *
     * @param file        the file to write into
     * @param append      if true it will perform the writing opperations in append
     *                    mode
     * @param utf8Charset if true it will use the <code>"UTF-8"</code> charset
     *                    encoding.
     */
    public TextWriter(File file, boolean append, boolean utf8Charset) {
        if (utf8Charset)
            setWriter(file, append, "UTF-8");
        else
            setWriter(file, append);
    }

    /**
     * Sets the <tt>PrintWriter</tt> object that uses the default charset encoding.
     * 
     * @param file   the handler for this writer
     * @param append specifies whehter the writing operations should be perfomed in
     *               append mode or not.
     */
    private void setWriter(File file, boolean append) {
        try {
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append))));
        } catch (FileNotFoundException fne) {
            out.println("Error in opening in the writer:" + fne.getMessage());
            fne.printStackTrace();
        }
    }

    /**
     * Sets the <tt>PrintWriter</tt> object with a given charset encoding.
     * 
     * @param file        the handler for this writer
     * @param append      specifies whehter the writing operations should be
     *                    perfomed in append mode or not.
     * @param charsetName specifies the charset encoding (e.g.,
     *                    <code>"UTF-8"</code>)
     */
    private void setWriter(File file, boolean append, String charsetName) {
        try {
            pw = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charsetName)));
        } catch (UnsupportedEncodingException uee) {
            out.println("Error in opening in the writer:" + uee.getMessage());
            uee.printStackTrace();
        } catch (FileNotFoundException fne) {
            out.println("Error in opening in the writer:" + fne.getMessage());
            fne.printStackTrace();
        }
    }

    /**
     * Writes a string in a specified format
     *
     */
    public void printf(String format, Object... args) {
        pw.printf(format, args);
        pw.flush();
    }

    /**
     * Writes a string
     *
     * @param s the string to write
     */
    public void print(String s) {
        pw.print(s);
        pw.flush();
    }

    /**
     * Writes a string and appends a new line at the end
     *
     * @param s the string to write
     */
    public void println(String s) {
        pw.println(s);
        pw.flush();
    }

    /**
     * Writes a new line
     *
     */
    public void println() {
        pw.println();
    }

    /**
     * Closes the streams
     *
     */
    public void close() {
        pw.flush();
        pw.close(); // will automatically close all the chained streams
    }
}
