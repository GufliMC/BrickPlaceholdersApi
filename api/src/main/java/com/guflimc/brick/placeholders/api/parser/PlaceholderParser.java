package com.guflimc.brick.placeholders.api.parser;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderParser {

    private String data;

    private PlaceholderParser(String data) {
        this.data = data;
    }

    public static PlaceholderParser of(@NotNull String placeholder) {
        return new PlaceholderParser(placeholder);
    }

    //

    private final Pattern QUOTED = Pattern.compile("^([\"'])((?:\\\\\\1|(?:(?!\\1)).)*)(\\1)");

    public boolean hasNext() {
        return data.length() > 0;
    }

    public void skip(int length) {
        if (length > data.length())
            throw new IllegalArgumentException("Cannot skip more than the remaining length.");

        data = data.substring(length);
    }

    public String until(char c) {
        if (data.length() == 0)
            throw new IllegalStateException("Cannot continue when there is no remaining data.");

        // first try quotes
        Matcher mr = QUOTED.matcher(data);
        if ( mr.find() ) {
            if ( mr.end() != data.length() && data.charAt(mr.end()) != c )
                throw new IllegalArgumentException("The given character '" + c + "' does not appear right after the quoted data.");

            data = data.substring(mr.end());
            return mr.group(2);
        }

        int index = data.indexOf(c);

        // return everything
        if (index == -1) {
            String result = data;
            data = "";
            return result;
        }

        // return until character
        String result = data.substring(0, index);
        data = data.substring(index);
        return result;
    }

    public String until(char c, int skip) {
        String result = until(c);
        if ( data != null && data.length() >= skip ) {
            skip(skip);
        }
        return result;
    }

    public String remaining() {
        String result = data;
        data = "";
        return result;
    }

}
