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

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Closeable;

/**
 * Tool for executing shell commands
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6
 * @ady.rep ExecCaller
 */
public class Shell {
    private static String SHELLLOGFILE;
    static TextWriter logWriter;

    static {
        SHELLLOGFILE = System.getProperty("SHELLLOGFILE");
        if (SHELLLOGFILE == null)
            throw new ConfigurationException("SHELLLOGFILE is not set. Please use the -D option in your java command.");
        logWriter = new TextWriter(SHELLLOGFILE);
    }

    public static void logCommand(String[] cmd) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cmd.length; i++)
            builder.append("[" + cmd[i] + "] ");
        logWriter.println("CMD: " + builder.toString());
    }

    /**
     * Executes a shell command at a specific path
     * 
     * @ady.rep runProcess
     */
    public static void run(String path, String command) {
        try {
            String[] cmd = new String[1];
            cmd[0] = path + File.separator + command;
            Runtime rt = Runtime.getRuntime();
            logCommand(cmd);
            Process proc = rt.exec(cmd);

            InputStream iserror = proc.getErrorStream();
            InputStream isinput = proc.getInputStream();
            StreamGobbler errorGobbler = new StreamGobbler(iserror, "LOG");
            StreamGobbler outputGobbler = new StreamGobbler(isinput, "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = proc.waitFor();
        } catch (Throwable t) {
            logWriter.println("Error when executing [" + command + "] in [" + path + "]:" + t);
        }
    }

    /**
     * Runs a sequence of commands in shell
     *
     * @ady.rep runProcess
     */
    public static void run(String path, String commands, String delim) {
        Process proc = null;
        try {
            String[] cmd = commands.split(delim);
            cmd[0] = path + File.separator + cmd[0];
            Runtime rt = Runtime.getRuntime();
            logCommand(cmd);
            proc = rt.exec(cmd);

            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "LOG");
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = proc.waitFor();
        } catch (Throwable t) {
            logWriter.println("Error when executing [" + commands + "] in [" + path + "]:" + t);
        } finally {
            if (proc != null) {
                close(proc.getOutputStream());
                close(proc.getInputStream());
                close(proc.getErrorStream());
                proc.destroy();
            }
        }
    }

    /**
     * Runs a shell command. Useful for commands with stream redirection E.g.,
     * String[] cmd = {"/bin/sh", "-c", "/bin/ls > out.dat"}; will properly redirect
     * the ls output in out.dat
     */
    public static void run(String[] cmd) {
        Process proc = null;
        try {
            logCommand(cmd);
            proc = Runtime.getRuntime().exec(cmd);

            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "LOG");
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            int exitVal = proc.waitFor();
        } catch (Throwable t) {
            logWriter.println("Error when executing command: " + t);
        } finally {
            if (proc != null) {
                close(proc.getOutputStream());
                close(proc.getInputStream());
                close(proc.getErrorStream());
                proc.destroy();
            }
        }
    }

    private static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                // ignored
            }
        }
    }
}

class StreamGobbler extends Thread {
    InputStream is;
    String type;

    StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null)
                Shell.logWriter.println(type + ">" + line);
            // is.close();
            // isr.close();
            // br.close();
        } catch (IOException ioe) {
            Shell.logWriter.println(ioe.getMessage());
            // log.warn(ioe.getMessage(), ioe);
            // ioe.printStackTrace();
        }
    }
}
