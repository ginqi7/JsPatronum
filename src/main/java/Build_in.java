package main.java;

import java.util.HashSet;
import java.util.Set;

public class Build_in {
	public static final Set<String> name = new HashSet<String>();
	public static final Set<Character> notNameSymbol = new HashSet<Character>();

    static {
        name.add("console");
        name.add("log");
        name.add("concat");
        name.add("slice");
        name.add("map");
        name.add("reduce");
        name.add("filter");
        name.add("constructor");
        name.add("toExponential");
        name.add("toFixed");
        name.add("toLocaleString");
        name.add("toPrecision");
        name.add("toString");
        name.add("valueOf");
        name.add("toSource");
        name.add("charAt");
        name.add("charCodeAt");
        name.add("indexOf");
        name.add("lastIndexOf");
        name.add("localeCompare");
        name.add("length");
        name.add("match");
        name.add("replace");
        name.add("search");
        name.add("substr");
        name.add("substring");
        name.add("toLocaleLowerCase");
        name.add("toLocaleUpperCase");
        name.add("toLowerCase");
        name.add("toUpperCase");

        notNameSymbol.add('+');
        notNameSymbol.add('-');
        notNameSymbol.add('*');
        notNameSymbol.add('/');
        notNameSymbol.add(',');
        notNameSymbol.add(';');
        notNameSymbol.add('=');
        notNameSymbol.add('>');
        notNameSymbol.add('<');
        notNameSymbol.add('!');
        notNameSymbol.add('{');
        notNameSymbol.add('}');
        notNameSymbol.add('&');
        notNameSymbol.add('|');

    }
}
