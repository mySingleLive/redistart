package com.dtflys.redistart.controls.editor;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpan;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONEditor extends CodeArea {

    private static final String KEYWORD_NULL_PATTERN = "null";

    private static final String KEYWORD_BOOLEAN_PATTERN = "(true|false)";

    private static final String IDENTITY_PARENT = "([a-z]|[A-Z]|\\_|\\$)([a-z]|[A-Z]|[0-9]|\\_|\\$)*";

    private static final String STRING_PATTERN_SINGLE_QUOTE = "\'([^\'\\\\]|\\\\.)*\'";

    private static final String STRING_PATTERN_DOUBLE_QUOTE = "\"([^\"\\\\]|\\\\.)*\"";

    private static final String BRACKET_PATTERN = "\\[|\\]";

    private static final String BRACE_PATTERN = "\\{|\\}";

    private static final String NUMBER_PATTERN = "(\\+|\\-)?[0-9]+(\\.[0-9]*(f|F)?)?";

    private static final String COLON_PATTERN = ":";

    private static final String COMMA_PATTERN = ",";

    private static final Pattern PATTERN = Pattern.compile(
                "(?<NULL>" + KEYWORD_NULL_PATTERN + ")"
                    + "|(?<BOOLEAN>" + KEYWORD_BOOLEAN_PATTERN + ")"
                    + "|(?<IDENTITY>" + IDENTITY_PARENT + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<NUMBER>" + NUMBER_PATTERN + ")"
                    + "|(?<COLON>" + COLON_PATTERN + ")"
                    + "|(?<STRINGDQ>" + STRING_PATTERN_DOUBLE_QUOTE + ")"
                    + "|(?<STRINGSQ>" + STRING_PATTERN_SINGLE_QUOTE + ")"
                    + "|(?<COMMA>" + COMMA_PATTERN + ")"
    );


    public void init() {
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));

        Subscription cleanupWhenNoLongerNeedIt = this
                // plain changes = ignore style changes that are emitted when syntax highlighting is reapplied
                // multi plain changes = save computation by not rerunning the code multiple times
                //   when making multiple changes (e.g. renaming a method at multiple parts in file)
                .multiPlainChanges()
                // do not emit an event until 500 ms have passed since the last emission of previous stream
                .successionEnds(Duration.ofMillis(100))

                // run the following code block when previous stream emits an event
                .subscribe(ignore -> this.setStyleSpans(0, computeHighlighting(this.getText())));

    }



    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        List<RSSpan<Collection<String>>> spans = new ArrayList<>();
        while(matcher.find()) {
            String styleClass = null;
            if ((matcher.group("NULL")) != null) {
                styleClass = "json-null";
            } else if ((matcher.group("BOOLEAN")) != null) {
                styleClass = "json-boolean";
            }  else if ((matcher.group("IDENTITY")) != null) {
                styleClass = "json-error";
            } else if (matcher.group("STRINGDQ") != null) {
                styleClass = "json-string";
            } else if (matcher.group("STRINGSQ") != null) {
                styleClass = "json-string";
            } else if (matcher.group("BRACKET") != null) {
                styleClass = "json-bracket";
            } else if (matcher.group("BRACE") != null) {
                styleClass = "json-brace";
            } else if (matcher.group("NUMBER") != null) {
                styleClass = "json-number";
            } else if (matcher.group("COLON") != null) {
                styleClass = "json-colon";
            } else if (matcher.group("COMMA") != null) {
                styleClass = "json-comma";
            } else {
                styleClass = "json-error";
            }
            spans.add(new RSSpan(Collections.emptyList(), matcher.start() - lastKwEnd));
            Set<String> styles = new HashSet<>();
            styles.add(styleClass);
            spans.add(new RSSpan(styles, matcher.end() - matcher.start()));
            lastKwEnd = matcher.end();
        }
        spans.add(new RSSpan(Collections.emptyList(), text.length() - lastKwEnd));

        for (int i = 0; i < spans.size(); i++) {
            RSSpan span = spans.get(i);
            Collection<String> styles = (Collection<String>) span.getStyle();
            if (styles.size() > 0) {
                if (styles.contains("json-colon")) {
                    if (i > 1) {
                        int j = i - 1;
                        while (j >= 0) {
                            RSSpan before = spans.get(j);
                            Collection<String> beforeStyles = (Collection<String>) before.getStyle();
                            if (!beforeStyles.isEmpty()) {
                                beforeStyles.clear();
                                beforeStyles.add("json-key");
                                break;
                            }
                            j--;
                            continue;
                        }
                    }
                }
            }
        }

        for (RSSpan rsSpan : spans) {
            StyleSpan<Collection<String>> span = new StyleSpan(rsSpan.getStyle(), rsSpan.getLength());
            spansBuilder.add(span);
        }
        return spansBuilder.create();
    }


/*
    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass = null;
            String id = null;
            if ((id = matcher.group("IDENTITY")) != null)styleClass = "json-identity";
            else if (matcher.group("STRINGDQ") != null) styleClass = "json-string";
            else if (matcher.group("STRINGSQ") != null) styleClass = "json-string";
            else if (matcher.group("BRACKET") != null) styleClass = "json-bracket";
            else if (matcher.group("BRACE") != null) styleClass = "json-brace";
            else if (matcher.group("NUMBER") != null) styleClass = "json-number";
            else if (matcher.group("COLON") != null) styleClass = "json-colon";
            else if (matcher.group("COMMA") != null) styleClass = "json-comma";
            else styleClass = "json-default";
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
*/

}
