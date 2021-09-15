package com.lennertsoffers.elementalstones.customClasses.tools;

import java.util.ArrayList;
import java.util.Collections;

public class StringListTools {
    public static String[] mirrorX(String[] inputString) {
        int length= inputString.length;
        for (int i = 0; i < length / 2; i++) {
            String mirrorLineTop = inputString[i];
            String mirrorLineBottom = inputString[length - (i + 1)];

            inputString[i] = mirrorLineBottom;
            inputString[length - (i + 1)] = mirrorLineTop;
        }
        return inputString;
    }

    public static String[] mirrorY(String[] inputString) {
        for (int i = 0; i < inputString.length; i++) {
            String line = inputString[i];
            ArrayList<Character> arrayList = new ArrayList<>();
            for (Character character : line.toCharArray()) {
                arrayList.add(character);
            }
            Collections.reverse(arrayList);
            StringBuilder builder = new StringBuilder(arrayList.size());
            for (Character character : arrayList) {
                builder.append(character);
            }
            inputString[i] = builder.toString();
        }
        return inputString;
    }

    public static String[] rotate(String[] inputString) {
        ArrayList<String> stringList = new ArrayList<>();
        for (int i = 0; i < inputString[0].length(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : inputString) {
                stringBuilder.append(s.charAt(i));
            }
            stringList.add(stringBuilder.toString());
        }
        String[] outputString = new String[stringList.size()];
        for (int i = 0; i < stringList.size(); i++) {
            outputString[i] = stringList.get(i);
        }
        return outputString;
    }
}