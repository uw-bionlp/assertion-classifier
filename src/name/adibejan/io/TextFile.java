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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

import static java.lang.System.out;

/**
 * Tool for easy acees to a text file
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 * @ady.rep TextFileContent
 */
public class TextFile {
    /**
     * Restricts access to instances of this class
     */
    private TextFile() {
    }

    /**
     * Fetches the entire contents of a text file, and return it in a string. This
     * method assumes default encoding.
     *
     * @param fileName the file to read from
     * @return the entire content of the file
     * @ady.rep getContents
     */
    static public String read(String fileName) {
        File file = new File(fileName);
        return read(file);
    }

    /**
     * Fetches the entire contents of a text file, and return it in a string. This
     * method assumes default encoding.
     *
     * @param file the file to read from
     * @return the entire content of the file
     * @ady.rep getContents
     */
    static public String read(File file) {
        StringBuilder builder = new StringBuilder();
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = null; // not declared within while loop
            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            out.println("Error in reading the file: " + ioe.getMessage());
            ioe.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException ioe) {
                out.println("Error in closing the file: " + ioe.getMessage());
                ioe.printStackTrace();
            }
        }
        return builder.toString();
    }

    /**
     * Writes a string in a file and then closes the file. This method assumes
     * default encoding.
     *
     * @ady.rep setContents
     */
    static public void write(File file, String content) {
        TextWriter writer = new TextWriter(file, false, true);
        writer.print(content);
        writer.close();
    }

    /**
     * Writes a string into a given file and then closes the file (it also append a
     * new line at the end)
     */
    public static void writeln(String fileName, String content) {
        TextWriter writer = new TextWriter(fileName, false, true);
        writer.println(content);
        writer.close();
    }

    /**
     * Writes a string into a given file and then closes the file.
     */
    public static void write(String fileName, String content) {
        TextWriter writer = new TextWriter(fileName, false, true);
        writer.print(content);
        writer.close();
    }
}
