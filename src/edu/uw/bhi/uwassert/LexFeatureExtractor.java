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

package edu.uw.bhi.uwassert;

import name.adibejan.string.StringUtil;
import name.adibejan.string.Token;
import name.adibejan.util.Config;
import name.adibejan.util.IntPair;
import name.adibejan.util.LabeledIntPair;
import name.adibejan.nlp.TextProcessor;
import name.adibejan.wrapper.OpenNLP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import negex.NegEx;
import context.ConText;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.System.out;

/**
 * Feature extractor
 *
 * @author Cosmin Adrian Bejan, Nic Dobbins
 * @version 1.0
 * @since JDK1.6 | July 2011
 */
public class LexFeatureExtractor {
    private List<LabeledIntPair> bionegSignals;  // n-gram , freq, n
    private List<LabeledIntPair> kinshipSignals; // n-gram , freq, n
    public static List<String> negPrefixes = Arrays.asList("ab de di il im in ir re un no mel mal mis".split("\\s+"));

    private final int WINDOWSIZE = 5;

    /**
     * Extracts features from a given text
     */
    public List<String> extractFeatures(String sentence, IntPair oConceptBoundaries) {
        SentenceLevelResources resource = new SentenceLevelResources();
        String pretokenized = sentence.trim().replaceAll("\\s+", " ");
        String[] osentence = pretokenized.split("\\s+");
        resource.toks = OpenNLP.tokenize(pretokenized);

        IntPair nConceptBoundaries = TextProcessor.getTokenIndexes(Arrays.asList(osentence),
                Arrays.asList(resource.toks), oConceptBoundaries);
        String conceptName = resource.getTokenSequence(nConceptBoundaries, " ");
        AssertAnnotation instance = AssertAnnotation.getTestInstance(nConceptBoundaries, conceptName);
        return extractFeatures(instance, resource);
    }

    /**
     * Extracts features from a given token sequence
     */
    public List<String> extractFeatures(String[] pretokenizedSentence, IntPair oConceptBoundaries) {
        SentenceLevelResources resource = new SentenceLevelResources();
        resource.toks = pretokenizedSentence;

        int first = oConceptBoundaries.getFirst();
        int second = oConceptBoundaries.getSecond();
        int lastTokIdx = pretokenizedSentence.length-1;
        if (first < 0|| first > lastTokIdx || second < 0 || second > lastTokIdx) {
            throw new InvalidParameterException("Concept boundary indicies must be within indices of the tokens");
        }
        
        String conceptName = resource.getTokenSequence(oConceptBoundaries, " ");
        AssertAnnotation instance = AssertAnnotation.getTestInstance(oConceptBoundaries, conceptName);
        return extractFeatures(instance, resource);
    }

    /*
     * Extracts features for instances where the discourse resources are not
     * available
     */
    public List<String> extractFeatures(AssertAnnotation instance, SentenceLevelResources sentRes) {

        String feature = null;
        String digit7 = StringUtil.leftPad(1, 7, '0');
        String prefix = digit7 + " " + instance.getAssertionValue();

        Concept concept = instance.getConcept();
        concept.testSameName(sentRes);
        String sentFlat = TextProcessor.flatten(sentRes.toks, " ");

        int LIMIT_W6 = 7;
        List<String> features = new ArrayList<String>();
        StringBuilder builder_W6 = new StringBuilder();
        for (int i = Math.max(0, concept.getStart() - LIMIT_W6); i < Math.min(sentRes.toks.length,
                concept.getEnd() + LIMIT_W6); i++) {
            builder_W6.append(sentRes.toks[i]);
            builder_W6.append(" ");
        }
        String sentFlat_W6 = builder_W6.toString().trim();

        IntPair closestSignalBefore = getClosestSignalBefore(sentRes.toks, concept.getBoundaries());
        IntPair closestNegSignalBefore = getClosestNegSignalBefore(sentRes.toks, concept.getBoundaries());

        if (AssertConfig.isSelected("NEGSIGNALCLOSESTLEFT_COMMARESTRICTED")) {
            if (closestNegSignalBefore != null) {
                int pos = -1;
                for (int i = concept.getStart() - 1; i >= 0; i--)
                    if (sentRes.toks[i].equals(",") || sentRes.toks[i].toLowerCase().equals("and")
                            || sentRes.toks[i].toLowerCase().equals("or")) {
                        pos = i;
                        break;
                    }
                if (pos != -1 && closestNegSignalBefore.getFirst() > pos) {
                    feature = prefix + " NEGSIGNALCLOSESTLEFT_COMMARESTRICTED#true";
                    features.add(feature);
                }
            }
        }

        if (AssertConfig.isSelected("SIGNALCLOSESTLEFT_WINDOWSIZE")) {
            if (closestSignalBefore != null) {
                if (concept.getStart() - closestSignalBefore.getSecond() <= WINDOWSIZE) {
                    for (String signal : getSignals()) {
                        if (signal.equals(getTokenSequence(sentRes.toks, closestSignalBefore))) {
                            feature = prefix + " SIGNALCLOSESTLEFT_WINDOWSIZE#" + signal;
                            features.add(feature);
                        }
                    }
                }
            }
        }

        if (AssertConfig.isSelected("STEM")) {
            for (int i = 0; i < sentRes.toks.length; i++) {
                if (!Token.isPunctuations(sentRes.toks[i])) {
                    feature = prefix + " STEM#" + sentRes.toks[i].toLowerCase();
                    features.add(feature);
                }
            }
        }

        if (AssertConfig.isSelected("QMARK_RIGHT")) {
            if (concept.getEnd() < sentRes.toks.length - 1) {
                if (sentRes.toks[concept.getEnd() + 1].equals("?")) {
                    feature = prefix + " QMARK_RIGHT#true";
                    features.add(feature);
                }
            }
        }

        if (AssertConfig.isSelected("PRESENT_SPECIAL")) {
            feature = prefix + " PRESENT_SPECIAL#true";
            extractLeftSequence("with a history of", concept, sentRes, prefix, feature);
            extractLeftSequence("found to have", concept, sentRes, prefix, feature);
            extractLeftSequence("noted to have", concept, sentRes, prefix, feature);
            extractLeftSequence("which showed", concept, sentRes, prefix, feature);
            extractLeftSequence("also had", concept, sentRes, prefix, feature);
            extractLeftSequence("complicated by", concept, sentRes, prefix, feature);
            extractLeftSequence("status post", concept, sentRes, prefix, feature);
            extractLeftSequence("notable for", concept, sentRes, prefix, feature);
            extractLeftSequence("demonstrated", concept, sentRes, prefix, feature);
            extractLeftSequence("given", concept, sentRes, prefix, feature);
            extractLeftSequence("which revealed", concept, sentRes, prefix, feature);
            extractLeftSequence("+", concept, sentRes, prefix, feature);
            extractLeftSequence("continued to have", concept, sentRes, prefix, feature);
            extractLeftSequence("patient has a history of", concept, sentRes, prefix, feature);
            extractLeftSequence("showing", concept, sentRes, prefix, feature);
            extractLeftSequence("setting of", concept, sentRes, prefix, feature);
        }

        if (AssertConfig.isSelected("ABSENT_SPECIAL")) {
            feature = prefix + " ABSENT_SPECIAL#true";
            extractLeftSequence("no evidence of", concept, sentRes, prefix, feature);
            extractLeftSequence("had no", concept, sentRes, prefix, feature);
            extractLeftSequence("showed no", concept, sentRes, prefix, feature);
            extractLeftSequence("denied", concept, sentRes, prefix, feature);
            extractLeftSequence("were no", concept, sentRes, prefix, feature);
            extractLeftSequence("negative for", concept, sentRes, prefix, feature);
            extractLeftSequence("he denies", concept, sentRes, prefix, feature);
            extractLeftSequence("did not have", concept, sentRes, prefix, feature);
            extractLeftSequence("having no", concept, sentRes, prefix, feature);
            extractLeftSequence("he has no", concept, sentRes, prefix, feature);
            extractLeftSequence("she had no", concept, sentRes, prefix, feature);
            extractLeftSequence("denies any", concept, sentRes, prefix, feature);
            extractLeftSequence("revealed no", concept, sentRes, prefix, feature);
            extractLeftSequence("no signs of", concept, sentRes, prefix, feature);
            extractLeftSequence("as having no", concept, sentRes, prefix, feature);
            extractLeftSequence("out for", concept, sentRes, prefix, feature);
            extractLeftSequence("patient denies", concept, sentRes, prefix, feature);
            extractLeftSequence("patient had no", concept, sentRes, prefix, feature);
            extractLeftSequence("she denies", concept, sentRes, prefix, feature);
        }

        if (AssertConfig.isSelected("POSSIBLE_SPECIAL")) {
            feature = prefix + " POSSIBLE_SPECIAL#true";
            extractLeftSequence("r/o", concept, sentRes, prefix, feature);
            extractLeftSequence("questionable", concept, sentRes, prefix, feature);
            extractLeftSequence("versus", concept, sentRes, prefix, feature);
            extractLeftSequence("possible", concept, sentRes, prefix, feature);
            extractLeftSequence("possibly", concept, sentRes, prefix, feature);
            extractLeftSequence("probable", concept, sentRes, prefix, feature);
            extractLeftSequence("presumed", concept, sentRes, prefix, feature);
            extractLeftSequence("vs.", concept, sentRes, prefix, feature);
            extractLeftSequence("r / o", concept, sentRes, prefix, feature);
            extractLeftSequence("either", concept, sentRes, prefix, feature);
            extractLeftSequence("representing", concept, sentRes, prefix, feature);
            extractLeftSequence("exclude", concept, sentRes, prefix, feature);
            extractLeftSequence("most likely", concept, sentRes, prefix, feature);
            extractLeftSequence("likely have", concept, sentRes, prefix, feature);
            extractLeftSequence("suspicion for", concept, sentRes, prefix, feature);
            extractLeftSequence("question", concept, sentRes, prefix, feature);
            extractLeftSequence("appears to be", concept, sentRes, prefix, feature);
            extractLeftSequence("suggesting", concept, sentRes, prefix, feature);
            extractLeftSequence("may have", concept, sentRes, prefix, feature);
            extractLeftSequence("to likely have", concept, sentRes, prefix, feature);
            extractLeftSequence("to rule out", concept, sentRes, prefix, feature);
            extractLeftSequence("question of", concept, sentRes, prefix, feature);
            extractLeftSequence("vs", concept, sentRes, prefix, feature);
            extractLeftSequence("represent", concept, sentRes, prefix, feature);

            extractRightSequence("versus", concept, sentRes, prefix, feature);
            extractRightSequence("vs", concept, sentRes, prefix, feature);
            extractRightSequence("vs.", concept, sentRes, prefix, feature);
        }

        if (AssertConfig.isSelected("POSSIBLE_SPECIAL2")) {
            feature = prefix + " POSSIBLE_SPECIAL2#true";
            extractLeftSequence("not rule out", concept, sentRes, prefix, feature);
            extractLeftSequence("could be", concept, sentRes, prefix, feature);
            extractLeftSequence("probably", concept, sentRes, prefix, feature);
            extractLeftSequence("chance of", concept, sentRes, prefix, feature);
            extractLeftSequence("were suggestive of", concept, sentRes, prefix, feature);
            extractLeftSequence("is suggestive of", concept, sentRes, prefix, feature);
            extractLeftSequence("checked for", concept, sentRes, prefix, feature);
            extractLeftSequence("possibility of", concept, sentRes, prefix, feature);
            extractLeftSequence("worrisome for", concept, sentRes, prefix, feature);
            extractLeftSequence("may not be", concept, sentRes, prefix, feature);
            extractLeftSequence("suspicion of", concept, sentRes, prefix, feature);
            extractLeftSequence("may reflect", concept, sentRes, prefix, feature);
            extractLeftSequence("questionable history of", concept, sentRes, prefix, feature);
            extractLeftSequence("question of history of", concept, sentRes, prefix, feature);
            extractLeftSequence("was evidence of", concept, sentRes, prefix, feature);
            extractLeftSequence("most likely related to", concept, sentRes, prefix, feature);
            extractLeftSequence("possibly was due to", concept, sentRes, prefix, feature);
            extractLeftSequence("which was equivocal for", concept, sentRes, prefix, feature);
            extractLeftSequence("probability of", concept, sentRes, prefix, feature);
            extractLeftSequence("suspected to be", concept, sentRes, prefix, feature);
            extractLeftSequence("presumptive", concept, sentRes, prefix, feature);
            extractRightSequence("not excluded", concept, sentRes, prefix, feature);
            extractRightSequence("difficult", concept, sentRes, prefix, feature);
            extractRightSequence("would be a consideration", concept, sentRes, prefix, feature);
            extractRightSequence("is possible", concept, sentRes, prefix, feature);
            extractRightSequence("also a possibility", concept, sentRes, prefix, feature);
        }

        if (AssertConfig.isSelected("HAS_KINSHIP_INSENTENCE")) {
            boolean flag = false;
            for (int i = 0; i < sentRes.toks.length; i++) {
                if (isSignal(kinshipSignals, sentRes.toks[i].toLowerCase())) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                feature = prefix + " HAS_KINSHIP_INSENTENCE#true";
                features.add(feature);
            } else {
                // feature = prefix+" HAS_KINSHIP_INSENTENCE#false";
                // feWriter.println(feature);
            }
        }

        if (AssertConfig.isSelected("CONCEPTSTEMEXPRESSION")) {
            StringBuilder builder = new StringBuilder();
            for (int i = concept.getStart(); i <= concept.getEnd(); i++) {
                if (!Token.isPunctuations(sentRes.toks[i])) {
                    builder.append(sentRes.toks[i]);
                    builder.append(Token.DEFAULT_DELIM);
                }
            }
            feature = prefix + " CONCEPTSTEMEXPRESSION#" + builder.toString().toLowerCase();
            features.add(feature);
        }

        if (AssertConfig.isSelected("NEGPREFIX")) {
            String lemma = null;
            for (int i = concept.getStart(); i <= concept.getEnd(); i++) {
                lemma = sentRes.toks[i].toLowerCase();
                if (!Token.isPunctuations(lemma)) {
                    for (String negPrefix : negPrefixes) {
                        if (!lemma.equals(negPrefix) && lemma.startsWith(negPrefix)) {
                            feature = prefix + " NEGPREFIX__" + negPrefix + "#true";
                            features.add(feature);
                            break;
                        }
                    }
                }
            }
        }

        int LIMIT_NEGPREFIX = 5;
        if (AssertConfig.isSelected("NEGPREFIX_LEFTWINDOW")) {
            String lemma = null;
            for (int i = Math.max(0, concept.getStart() - LIMIT_NEGPREFIX); i < concept.getStart(); i++) {
                lemma = sentRes.toks[i].toLowerCase();
                if (!Token.isPunctuations(lemma)) {
                    for (String negPrefix : negPrefixes) {
                        if (!lemma.equals(negPrefix) && lemma.startsWith(negPrefix)) {
                            feature = prefix + " NEGPREFIX_LEFTWINDOW__" + negPrefix + "#true";
                            features.add(feature);
                            break;
                        }
                    }
                }
            }
        }

        if (AssertConfig.isSelected("NEGPREFIX_RIGHTWINDOW")) {
            String lemma = null;
            for (int i = concept.getEnd() + 1; i < Math.min(sentRes.toks.length,
                    concept.getEnd() + LIMIT_NEGPREFIX + 1); i++) {
                lemma = sentRes.toks[i].toLowerCase();
                if (!Token.isPunctuations(lemma)) {
                    for (String negPrefix : negPrefixes) {
                        if (!lemma.equals(negPrefix) && lemma.startsWith(negPrefix)) {
                            feature = prefix + " NEGPREFIX_RIGHTWINDOW__" + negPrefix + "#true";
                            features.add(feature);
                            break;
                        }
                    }
                }
            }
        }

        if (AssertConfig.isSelected("WORDLEFT1_UNCASE")) {
            if (concept.getStart() > 0) {
                feature = prefix + " WORDLEFT1_UNCASE#" + sentRes.toks[concept.getStart() - 1].toLowerCase();
                features.add(feature);
            }
        }

        if (AssertConfig.isSelected("STEMLEFT1_UNCASE")) {
            if (concept.getStart() > 0) {
                feature = prefix + " STEMLEFT1_UNCASE#" + sentRes.toks[concept.getStart() - 1].toLowerCase();
                features.add(feature);
            }
        }

        if (AssertConfig.isSelected("WORDLEFT2_UNCASE")) {
            if (concept.getStart() > 1) {
                feature = digit7 + " " + instance.getAssertionValue() + " WORDLEFT2_UNCASE#"
                        + sentRes.toks[concept.getStart() - 2].toLowerCase();
                features.add(feature);
            }
        }

        if (AssertConfig.isSelected("STEMTRIGRAMLEFT_UNCASE")) {
            if (concept.getStart() > 2) {
                feature = prefix + " STEMTRIGRAMLEFT_UNCASE#" + sentRes.toks[concept.getStart() - 1].toLowerCase()
                        + "||" + sentRes.toks[concept.getStart() - 2].toLowerCase() + "||"
                        + sentRes.toks[concept.getStart() - 3].toLowerCase();
                features.add(feature);
            }
        }

        if (AssertConfig.isSelected("NEGEX")) {
            feature = prefix + " NEGEX#" + NegEx.analyzeNegation(sentFlat, concept.getName()).toLowerCase();
            features.add(feature);
        }

        if (AssertConfig.isSelected("NEGEX_W6")) {
            feature = prefix + " NEGEX_W6#" + NegEx.analyzeNegation(sentFlat_W6, concept.getName()).toLowerCase();
            features.add(feature);
        }

        if (AssertConfig.isSelected("CONTEXT_EXPERIENCER")) {
            String experiencer = ConText.analyzeExperiencer(sentFlat).replaceAll("\\s+", "_");
            feature = prefix + " CONTEXT_EXPERIENCER#" + experiencer;
            features.add(feature);
        }

        if (AssertConfig.isSelected("CONTEXT_TEMPORALITY_W6")) {
            feature = prefix + " CONTEXT_TEMPORALITY_W6#" + ConText.analyzeTemporality(sentFlat_W6);
            features.add(feature);
        }

        if (AssertConfig.isSelected("WORD_POSITION")) {
            int pos = 0;
            for (int i = 0; i < sentRes.toks.length; i++) {
                if (!Token.isPunctuations(sentRes.toks[i])) {
                    if (i < concept.getStart())
                        pos = i - concept.getStart();
                    else if (i <= concept.getEnd())
                        pos = 0;
                    else
                        pos = i - concept.getEnd();

                    feature = digit7 + " " + instance.getAssertionValue() + " WORD_POSITION#" + sentRes.toks[i] + "__"
                            + pos;
                    features.add(feature);
                }
            }
        }
        return features;
    }

    public void loadSignals(String ASSERTRESOURCES) {
        bionegSignals = new ArrayList<LabeledIntPair>();
        loadSignals(bionegSignals, ASSERTRESOURCES + Config.getP("path.bionegsignals"));
        kinshipSignals = new ArrayList<LabeledIntPair>();
        loadSignals(kinshipSignals, ASSERTRESOURCES + Config.getP("path.kinshipsignals"));
    }

    public void loadSignals(List<LabeledIntPair> signals, String filePath) {
        StringBuilder builder = new StringBuilder();
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String line = null;
            int cnt = 1;
            while ((line = input.readLine()) != null) {
                String[] toks = line.split("\\s+");
                assert toks.length >= 2 : "Not valid time signal line [" + line + "]";

                int freq = Integer.parseInt(toks[0]);
                int len = toks.length - 1;
                builder.delete(0, builder.length());
                for (int i = 1; i < toks.length - 1; i++) {
                    builder.append(toks[i]);
                    builder.append(Token.DEFAULT_DELIM);
                }
                builder.append(toks[toks.length - 1]);
                signals.add(new LabeledIntPair(freq, len, builder.toString()));
                // if(cnt == threshold) break;
                // cnt++;
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
        Collections.sort(signals, IntPair.getDescComparatorByFirstInt());
    }

    public boolean isSignal(String expression) {
        if (isSignal(bionegSignals, expression))
            return true;
        if (isSignal(kinshipSignals, expression))
            return true;
        return false;
    }

    public boolean isNegSignal(String expression) {
        if (isSignal(bionegSignals, expression))
            return true;
        return false;
    }

    public boolean isSignal(List<LabeledIntPair> signals, String expression) {
        for (LabeledIntPair signal : signals)
            if (signal.getLabel().equals(expression))
                return true;
        return false;
    }

    public IntPair getClosestSignalBefore(String[] toks, IntPair reference) {
        List<IntPair> signalIntervals = getSignals(toks);
        if (signalIntervals.isEmpty())
            return null;
        return IntPair.getClosestBefore(reference, signalIntervals);
    }

    public IntPair getClosestNegSignalBefore(String[] toks, IntPair reference) {
        List<IntPair> signalIntervals = getNegSignals(toks);
        if (signalIntervals.isEmpty())
            return null;
        return IntPair.getClosestBefore(reference, signalIntervals);
    }

    public List<String> getSignals() {
        List<String> signals = new ArrayList<String>();
        signals.addAll(getSignals(bionegSignals));
        signals.addAll(getSignals(kinshipSignals));
        return signals;
    }

    public List<String> getSignals(List<LabeledIntPair> signalPairs) {
        List<String> signals = new ArrayList<String>();
        for (LabeledIntPair signalPair : signalPairs) {
            signals.add(signalPair.getLabel());
        }
        return signals;
    }

    /**
     * max lenght: 5
     */
    public List<IntPair> getSignals(String[] toks) {
        List<IntPair> signalIntervals = new ArrayList<IntPair>();

        for (int limit = 4; limit >= 0; limit--) {
            for (int i = 0; i < toks.length - limit; i++) {
                if (isSignal(getTokenSequence(toks, i, limit)))
                    IntPair.insertSpecial(new IntPair(i, i + limit), signalIntervals);
            }
        }
        return signalIntervals;
    }

    public List<IntPair> getNegSignals(String[] toks) {
        List<IntPair> signalIntervals = new ArrayList<IntPair>();

        for (int limit = 4; limit >= 0; limit--) {
            for (int i = 0; i < toks.length - limit; i++) {
                if (isNegSignal(getTokenSequence(toks, i, limit)))
                    IntPair.insertSpecial(new IntPair(i, i + limit), signalIntervals);
            }
        }
        return signalIntervals;
    }

    // should call the other method with new IntPair
    public String getTokenSequence(String[] toks, int start, int limit) {
        StringBuilder builder = new StringBuilder();
        int k;
        for (k = 0; k < limit; k++) {
            builder.append(toks[start + k].toLowerCase());
            builder.append(Token.DEFAULT_DELIM);
        }
        builder.append(toks[start + k].toLowerCase());

        return builder.toString();
    }

    public String getTokenSequence(String[] toks, IntPair interval) {
        StringBuilder builder = new StringBuilder();
        int i;
        for (i = interval.getFirst(); i < interval.getSecond(); i++) {
            builder.append(toks[i].toLowerCase());
            builder.append(Token.DEFAULT_DELIM);
        }
        builder.append(toks[i].toLowerCase());

        return builder.toString();
    }

    public String extractLeftSequence(String expression, Concept concept, SentenceLevelResources sentRes, String prefix, String feature) {
        String[] toks = expression.split("\\s+");
        if (concept.getStart() > toks.length - 1) {
            String[] words = new String[toks.length];
            for (int i = 0; i < toks.length; i++) {
                words[i] = sentRes.toks[concept.getStart() - (toks.length - i)].toLowerCase();
            }
            boolean flag = true;
            for (int i = 0; i < toks.length; i++)
                if (!words[i].equals(toks[i])) {
                    flag = false;
                    break;
                }
            if (flag) {
                return feature;
            }
        }
        return null;
    }

    public String extractRightSequence(String expression, Concept concept, SentenceLevelResources sentRes, String prefix, String feature) {
        String[] toks = expression.split("\\s+");
        if (concept.getEnd() < sentRes.toks.length - toks.length) {
            String[] words = new String[toks.length];
            for (int i = 0; i < toks.length; i++) {
                words[i] = sentRes.toks[concept.getEnd() + i + 1].toLowerCase();
            }
            boolean flag = true;
            for (int i = 0; i < toks.length; i++)
                if (!words[i].equals(toks[i])) {
                    flag = false;
                    break;
                }
            if (flag) {
                return feature;
            }
        }
        return null;
    }
}
