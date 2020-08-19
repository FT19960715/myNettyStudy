package com.spring;

import java.util.regex.Pattern;

public class PatternTest {
    final static Pattern GLOB_PATTERN = Pattern.compile("\\?|\\*|\\{[^/]+?\\}");
    public static void main(String[] args) {

    }
}
