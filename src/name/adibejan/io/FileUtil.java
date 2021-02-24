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

import name.adibejan.string.StringUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;


import java.nio.channels.FileChannel;

import java.util.Comparator;
import java.util.Arrays;

import static java.lang.System.out;

/**
 * File utilities
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 */
public class FileUtil {
  public static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

  /**
   * Restricts access to instances of this class
   */
  private FileUtil() {}

  /**
   * Filters all the files from a given directory.
   *
   * @param dirPath the directory with the files to filter
   * @return all the files from <code>dirPath</code>
   */
  public static File[] filterFiles(String dirPath) {
    File dir = new  File(dirPath);
    File[] fdirs = dir.listFiles(new FileFilter() {
        public boolean accept(File file) {
          if(file.isFile()) return true;
          return false;
        }
      });
    return fdirs;
  }
  
  /**
   * Filters all the files from a given directory that have their extension mached on 
   * a specified string. 
   *
   * @param dirPath the directory with the files to filter
   * @param extension the string representing a specific extension. If a <tt>.</tt> 
   * delimiter is present, it has to be inside this string.
   * @return all the files from <code>dirPath</code> whose extension match <code>expression</code>
   */
  public static File[] filterFilesByExtension(String dirPath, final String extension) {
    File dir = new  File(dirPath);
    File[] fdirs = dir.listFiles(new FileFilter() {
        public boolean accept(File file) {
          if(!file.isFile()) return false;
          return file.getName().endsWith(extension);
        }
      });
    return fdirs;
  }

  /**
   * Deletes all the files from a direcory
   *
   * @param dirPath the directory with the files to remove
   * @ady.rep rmFiles
   */
  public static void deleteFiles(String dirPath) {
    File[] files = filterFiles(dirPath);    
    for(File file : files)
      file.delete();
  }

  /**
   * Copies the content of one file into another (the classic approach)
   * 
   * @ady.rep copyFile1
   */
  public static long copyFileUsingBuffer(String from, String to) {
    return copyFileUsingBuffer(new File(from), new File(to));
  }

  /**
   * Copies the content of one file into another (the classic approach)
   * 
   * @ady.rep copyFile1
   */
  public static long copyFileUsingBuffer(File from, File to) {
    InputStream _from = null;
    OutputStream _to = null;
    
    try {
      _from = new FileInputStream(from);
      _to = new FileOutputStream(to);
    } catch(FileNotFoundException fnfe) {
      out.println(fnfe.getMessage());
      fnfe.printStackTrace();
    }
    
    long total = copyStreamUsingBuffer(_from, _to);

    try {
      _from.close();
      _to.close();
    } catch(IOException ioe) {
      out.println("Exception when closing the streams: "+ioe.getMessage());
      ioe.printStackTrace();
    }

    return total;
  }
  
  /**
   * Copies the content of one stream into another (the classic approach with buffer).
   * The method does not close the streams.
   * 
   * @ady.rep copyFile1
   */
  public static long copyStreamUsingBuffer(InputStream from, OutputStream to) {
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    long count = 0;
    int length = -1;
    try {
      while (-1 != (length = from.read(buffer))) {
        to.write(buffer, 0, length);
        to.flush();
        count += length;
      }
      //from.close();
      //to.close();
    } catch(IOException ioe) {
      out.println(ioe.getMessage());
      ioe.printStackTrace();
    }
    return count;
  }  
}
