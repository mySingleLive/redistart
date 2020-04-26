package com.dtflys.redistart.controls.editor;

import org.fxmisc.richtext.CaretSelectionBind;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpan;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.text.BreakIterator;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONEditor extends AbstractCodeEditor {

    private static final String KEYWORD_NULL_PATTERN = "null";

    private static final String KEYWORD_BOOLEAN_PATTERN = "(true|false)";

    private static final String SPACE_PATTERN = "[\\s\t]+";

    private static final String NL_PATTERN = "[\n\r]+";

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
                    + "|(?<NL>" + NL_PATTERN + ")"
                    + "|(?<SPACE>" + SPACE_PATTERN + ")"
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

    private Integer selectedWordStart = null;
    private Integer selectedWordEnd = null;
    private Integer selectedWordParagraph = null;

    private Subscription cleanupWhenNoLongerNeedIt;


    public void init() {
         super.init();
         editorScrollPane.getStylesheets().add("/css/json.css");
         getStyleClass().setAll("string-value-json-text");
/*
        setOnNewSelectionDrag(point2D -> {
            CaretSelectionBind csb = getCaretSelectionBind();
            int paragraph = csb.getParagraphIndex();
            int position = csb.getColumnPosition();
            System.out.println("Paragraph = " + paragraph + ", position = " + position + ", selection = " + getSelection().toString());
        });
*/

        cleanupWhenNoLongerNeedIt = this
                .multiPlainChanges()
                .successionEnds(Duration.ofMillis(2))
                .subscribe(ignore -> this.setStyleSpans(0, computeHighlighting(this.getText())));

        setOnKeyTyped(event -> {
            String ch = event.getCharacter();
            int pos = getCaretPosition();
            int col = getCaretColumn();
            int paragraph = getCurrentParagraph();
            if (paragraph > 0) {
                paragraph--;
            }
            StyleSpans<Collection<String>> styleSpans = getStyleSpans(paragraph);

            if (ch.equals("\n") || ch.equals("\r")) {
                int indents = 0;
                StyleSpan<Collection<String>> firstSpan = styleSpans.getStyleSpan(0);
                StyleSpan<Collection<String>> lastSpan = styleSpans.getStyleSpan(styleSpans.getSpanCount() - 1);
                if (firstSpan.getStyle().contains("json-space")) {
                    Iterator<String> iterator = firstSpan.getStyle().iterator();
                    String str = iterator.next();
                    if (!Character.isDigit(str.charAt(0))) {
                        str = iterator.next();
                    }
                    indents = Integer.parseInt(str);
                }
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < indents; i++) {
                    builder.append(" ");
                }
                insertText(pos, builder.toString());
            }
        });
    }

    @Override
    public void selectWord() {
        if (this.getLength() != 0) {
            CaretSelectionBind<?, ?, ?> csb = this.getCaretSelectionBind();
            int paragraph = csb.getParagraphIndex();
            int position = csb.getColumnPosition();
            String paragraphText = this.getText(paragraph);
            BreakIterator breakIterator = BreakIterator.getWordInstance(this.getLocale());
            breakIterator.setText(paragraphText);
            breakIterator.preceding(position);
            int start = breakIterator.current();

            while(start > 0 && paragraphText.charAt(start - 1) == '_') {
                --start;
                if (start > 0 && !breakIterator.isBoundary(start - 1)) {
                    breakIterator.preceding(start);
                    start = breakIterator.current();
                }
            }

            breakIterator.following(position);
            int end = breakIterator.current();
            int len = paragraphText.length();

            while(end < len && paragraphText.charAt(end) == '_') {
                ++end;
                if (end < len && !breakIterator.isBoundary(end + 1)) {
                    breakIterator.following(end);
                    end = breakIterator.current();
                } else if (Character.isDigit(paragraphText.charAt(end))) {
                    ++end;
                }
            }

            csb.selectRange(paragraph, start, paragraph, end);
            selectedWordStart = start;
            selectedWordEnd = end;
            selectedWordParagraph = paragraph;
        }

    }


    @Override
    public void moveSelectedText(int position) {
        super.moveSelectedText(position);
    }

    @Override
    public void setText(String text) {
        super.setText(text);

    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        List<RSSpan<Collection<String>>> spans = new ArrayList<>();
        int indent = 0;
        String str = "";
        while(matcher.find()) {
            String styleClass = null;
            if ((matcher.group("NULL")) != null) {
                styleClass = "json-null";
            } else if ((matcher.group("NL")) != null) {
                styleClass = "json-nl";
                indent = 0;
            } else if ((str = (matcher.group("SPACE"))) != null) {
                styleClass = "json-space";
                int spStart = matcher.start();
                if (spStart > 0) {
                    char ch = getText().charAt(spStart - 1);
                    if (ch == '\n' || ch == '\r') {
                        indent = str.length();
                    }
                } else {
                    indent = 0;
                }
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
            spans.add(new RSSpan(Collections.emptyList(), matcher.start() - lastKwEnd, indent));
            Set<String> styles = new HashSet<>();
            styles.add(styleClass);
            if (styleClass.equals("json-space")) {
                styles.add(indent + "");
            }
            spans.add(new RSSpan(styles, matcher.end() - matcher.start(), indent));
            lastKwEnd = matcher.end();
        }
        spans.add(new RSSpan(Collections.emptyList(), text.length() - lastKwEnd, indent));

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
                } /*else if (styles.contains("json-comma")) {
                    for (int j = i + 1; j < spans.size(); j++) {
                        RSSpan after = spans.get(j);
                        Collection<String> afterStyle = (Collection<String>) after.getStyle();
                        if (!afterStyle.isEmpty()) {
                            if (afterStyle.contains("json-bracket")
                                    || afterStyle.contains("json-brace")
                                    || afterStyle.contains("json-comma")) {
                                styles.clear();
                                styles.add("json-error");
                            }
                            break;
                        }
                    }
                }*/
                }
        }

        for (RSSpan rsSpan : spans) {
            spansBuilder.add(rsSpan);
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
