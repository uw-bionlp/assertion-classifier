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

package name.adibejan.learning.features;

import name.adibejan.string.Token;
import name.adibejan.util.Pair;
import name.adibejan.util.Triple;
import name.adibejan.util.ConfigurationException;
import name.adibejan.string.StringUtil;
import name.adibejan.io.TextWriter;
import name.adibejan.io.TextWriterManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

//import java.util.HashSet;
import java.util.TreeSet;
import java.util.Set;

import static java.lang.System.out;

/**
 * Tool for transforming readable format feature files into SVM format files
 *
 * @author Cosmin Adrian Bejan
 * @version 1.0
 * @since JDK1.6 | July 2011
 */

public abstract class FeatureManager {
    protected FeatureWeightingScheme wscheme;
    protected DataSplittingScheme sscheme;
    protected int instanceCounter;

    /**
     * Selects the current instance splitting scheme
     */
    public void setDataSplittingScheme(DataSplittingScheme sscheme) {
        this.sscheme = sscheme;
    }

    /**
     * Selects the current feature weighting scheme
     */
    public void setFeatureWeightingScheme(FeatureWeightingScheme wscheme) {
        this.wscheme = wscheme;
    }

    /**
     * Verifies if all the configuration attributes are set
     */
    protected void checkSetting() {
        assert sscheme != null : "The data splitting scheme is not set!";
        assert wscheme != null : "The feature weighting scheme is not set!";
    }

    /**
     * Initializes all the writers associated with a specific feature file and data
     * splitting scheme.
     */
    public void initWriterManager(TextWriterManager writerManager, String featureFile) {
        Triple<String, String, String> fileTriple = StringUtil.split3FilePath(featureFile);
        switch (sscheme) {
            case NOSPLITTING:
                writerManager.add("nosplit", fileTriple.getFirst() + fileTriple.getSecond() + "_nosplit.dat");
                break;
            case TENFOLD:
                for (int i = 1; i <= 10; i++) {
                    writerManager.add("trainfold" + i,
                            fileTriple.getFirst() + fileTriple.getSecond() + "_trainfold" + i + ".dat");
                    writerManager.add("testfold" + i,
                            fileTriple.getFirst() + fileTriple.getSecond() + "_testfold" + i + ".dat");
                }
                break;
            case FIVEFOLD:
                for (int i = 1; i <= 5; i++) {
                    writerManager.add("trainfold" + i,
                            fileTriple.getFirst() + fileTriple.getSecond() + "_trainfold" + i + ".dat");
                    writerManager.add("testfold" + i,
                            fileTriple.getFirst() + fileTriple.getSecond() + "_testfold" + i + ".dat");
                }
                break;
        }
    }

    /**
     * Dispatches a specific data insance into folds
     */
    protected void manageInstance(DataInstance instance, TextWriterManager writerManager) {
        TextWriter writer = null;
        switch (sscheme) {
            case NOSPLITTING:
                writer = writerManager.get("nosplit");
                print(instance, writer);
                break;
            case TENFOLD:
                for (int fold = 1; fold <= 10; fold++) {
                    if ((instanceCounter + 10 - fold) % 10 == 0)
                        writer = writerManager.get("testfold" + fold);
                    else
                        writer = writerManager.get("trainfold" + fold);
                    print(instance, writer);
                }
                break;
            case FIVEFOLD:
                for (int fold = 1; fold <= 5; fold++) {
                    if ((instanceCounter + 5 - fold) % 5 == 0)
                        writer = writerManager.get("testfold" + fold);
                    else
                        writer = writerManager.get("trainfold" + fold);
                    print(instance, writer);
                }
                break;
        }
    }

    /**
     * Dispatches a specific data insance into a specified fold
     */
    protected void manageInstance(DataInstance instance, TextWriterManager writerManager, int FOLD) {
        TextWriter writer = null;
        switch (sscheme) {
            case TENFOLD:
                for (int fold = 1; fold <= 10; fold++) {
                    if ((FOLD + 10 - fold) % 10 == 0)
                        writer = writerManager.get("testfold" + fold);
                    else
                        writer = writerManager.get("trainfold" + fold);
                    print(instance, writer);
                }
                break;
            case FIVEFOLD:
                for (int fold = 1; fold <= 5; fold++) {
                    if ((FOLD + 5 - fold) % 5 == 0)
                        writer = writerManager.get("testfold" + fold);
                    else
                        writer = writerManager.get("trainfold" + fold);
                    print(instance, writer);
                }
                break;
        }
    }

    private void print(DataInstance instance, TextWriter writer) {
        switch (wscheme) {
            case NOWEIGHTING:
                writer.println(instance.getNoWeightingFormat());
                return;
            case FREQUENCY:
                writer.println(instance.getFrequencyWeightingFormat());
                return;
        }
        throw new ConfigurationException("Unsupported weighting scheme [" + wscheme.name() + "]");
    }

    /**
     * Format learning file: 0000001 PRESENT WORD#The
     */
    public static Set<String> extractFeatures(String filePath) {
        Set<String> features = new TreeSet<String>();
        Pair<String, String> featurePair = null;
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String line = null;
            while ((line = input.readLine()) != null) {
                String[] toks = line.split("\\s+");
                if (toks.length != 3)
                    continue;
                featurePair = StringUtil.split2First(toks[2], Token.SHARP_DELIM);
                if (featurePair.getSecond().equals(""))
                    continue;
                features.add(featurePair.getFirst());
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
        return features;
    }
}
