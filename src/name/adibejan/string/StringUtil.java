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

package name.adibejan.string;

import name.adibejan.util.Pair;
import name.adibejan.util.IntPair;
import name.adibejan.util.Triple;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import static java.lang.System.out;

/**
 * String utilities.
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
*/
public class StringUtil {    
  /** Default delimiter is __II__ */
  //public static final String DD = "__II__";
  
  /**
   * Resticts access to the instances of this object
   */
  private StringUtil() {}

  /**
   * Splits a formated <tt>String</tt> object according to a given delimiter <tt>String</tt> object.
   * If multiple occurences of the delimiter are present, the split is perfomed 
   * at the first occurence. 
   *
   * @param rep the formated string to be split
   * @param delim the delimiter the establishes the split position
   * @throws UnsupportedStringFormatException if <code>delim</code> is not 
   * found in <code>rep</code>.
   * @ady.rep StrUtil.getFirstStringPair
   */  
  public static Pair<String,String> split2First(String rep, String delim) {
    int pos = rep.indexOf(delim);
    if(pos == -1) throw new UnsupportedStringFormatException("["+delim+"] does not exist in ["+rep+"]");
    String first = rep.substring(0,pos);
    String second = rep.substring(pos+delim.length(), rep.length());
    return new Pair<String,String>(first,second);
  }
  
  /**
   * Splits a file path into a string with root path and a string with file name. 
   * This method does not test whether the input string is a valid file path. Therefore,
   * can also split a directory path into the same path and empty string.
   *
   * @param filePath the path to be split
   * @return a pair of strings with the first string representing the root path, 
   * the second - the file name.
   * When the <code>File.separator</code> does not exist in <code>filePath</code>
   * the first string will be empty. 
   * @ady.rep StrUtil.basename StrUtil.splitFilePath StrUtil.filenamenoext
   */
  public static Pair<String, String> split2FilePath(String filePath) {
    //Note: is different from split2Last(fileName, File.separator); because it also keeps
    //      the delimiter (/) in the first chunk
    int pos = filePath.lastIndexOf(File.separator);
    String dirPath = null;
    String fileName = null;
    if(pos == -1) {
      dirPath = "";
      fileName = filePath;
    } else {
      dirPath = filePath.substring(0, pos+1);
      fileName = filePath.substring(pos+1, filePath.length());
    }
    return new Pair<String, String>(dirPath, fileName);
  }
  
  /**
   * Splits a file path into a its root path, base name, and extension. 
   * It also handles the cases when the <code>File.separator</code> or extension 
   * delimiter <tt>.</tt> does not exist in the file path.
   *
   * @param filePath the path to be split
   * @return a triple of strings with the first string representing the root path, 
   * the second - basename,  and the third - the its extension. 
   * When the <code>File.separator</code> does not exist in <code>filePath</code>
   * the first string will be empty. In cases when the extension delimiter <tt>.</tt> 
   * does not exist the third string will be empty.
   * @ady.rep StrUtil.basename StrUtil.splitFilePath StrUtil.filenamenoext
   */
  public static Triple<String, String, String> split3FilePath(String filePath) {
    Pair<String, String> split1 = split2FilePath(filePath);
    Pair<String, String> split2 = splitFileName(split1.getSecond());
    return new Triple<String, String, String>(split1.getFirst(), split2.getFirst(), split2.getSecond());
  }
  
  /**
   * Splits a file name string into its basename and extension. It also handles the cases
   * when the extension delimiter <tt>.</tt> does not exist in the file name.
   *
   * @param fileName the file name string to be split
   * @return a pair of strings with the first string representing the basename and the second
   * string the extension. When the extension delimiter <tt>.</tt> does not exist the second
   * string will be empty.
   * @ady.rep StrUtil.basename StrUtil.splitFilePath StrUtil.filenamenoext
   */
  public static Pair<String, String> splitFileName(String fileName) {
    String delimiter = ".";
    int pos = fileName.lastIndexOf(delimiter);
    String baseName = null;
    String extension = null;
    if(pos == -1) {
      baseName = fileName;
      extension = "";
    } else {
      baseName = fileName.substring(0,pos);
      extension = fileName.substring(pos+delimiter.length(), fileName.length());
    }
    return new Pair<String,String>(baseName,extension);
  }
  
  /**
   * Converts an int value into string and left pads the string with a given character.
   * 
   * @param value the int value to convert
   * @param size specifies how many characters to fill out
   * @param ch the character to fill out the space
   * @return the left padded string (e.g., for <code>(73,7,0)</code>, it the return 
   * string will be <code>0000073</code>).
   * @ady.rep StrUtil.i2s getLeftPadInt2String leftPadInt
   */
  public static String leftPad(int value, int size, char ch) {
    String sValue = Integer.toString(value);
    return repeat(ch, size - sValue.length()) + sValue;    
  }
  
  /**
   * Replicates a character of a specific number of times.
   * 
   * @param ch the character to be replicated
   * @param times specifies the number of times <code>ch</code> will be replicated
   * @return a <tt>String</tt> encoding the replication of <code>ch</code>
   * @ady.rep StrUtil.replicate getReplication
   */
  public static String repeat(char ch, int times) {
    StringBuilder buffer = new StringBuilder();
    for(int i = 1; i <= times; i++) buffer.append(ch);
    return buffer.toString();
  }

  /**
   * Strips the prefix of a given string
   * 
   * @param content the string from which the given prefix will be removed
   * @param prefix the specified prefix to be removed
   * @return a new string with the <code>prefix</code> removed. If the <code>prefix</code>
   * does not end the <code>content</code>, then the <code>content</code> is returned.
   * @ady.rep StrUtil.removePrefix
   */
  public static String stripPrefix(String content, String prefix) {
    if(content.startsWith(prefix))
      return content.substring(prefix.length(), content.length());
    return content;
  }

  /**
   * Strips the suffix of a given string
   * 
   * @param content the string from which the given suffix will be removed
   * @param suffix the specified suffix to be removed
   * @return a new string with the <code>suffix</code> removed. If the <code>suffix</code>
   * does not end the <code>content</code>, then the <code>content</code> is returned.
   */
  public static String stripSuffix(String content, String suffix) {
    if(content.endsWith(suffix))
      return content.substring(0, content.length() - suffix.length());
    return content;
  }

  /**
   * Strips the prefix and suffix of a given string
   * 
   * @param content the string from which the given suffix and prefix will be removed
   * @param prefix the specified prefix to be removed
   * @param suffix the specified suffix to be removed
   * @return a new string with the <code>prefix</code> and <code>suffix</code> removed. If the 
   * <code>prefix</code> / <code>suffix</code> does not begin / end the <code>content</code>, then 
   * the <code>content</code> is returned.
   */
  public static String stripAffix(String content, String prefix, String suffix) {
    return stripSuffix(stripPrefix(content, prefix), suffix);
  }  
}
