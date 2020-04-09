package com.dtflys.redistart.script;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gongjun[jun.gong@thebeastshop.com]
 * @since 2018-06-21 17:32
 */
public class RedisScriptParser {
    private final static Logger log = LoggerFactory.getLogger(RedisScriptParser.class);

    private RedisScriptSource source;

    private String sourceText;

    private int readIndex = -1;

    private int lineNumber = 1;

    private RedisScriptManager redisScriptManager;

    private boolean isModule = false;

    private String moduleName;

    private final static char SINGLE_QUOTATION = '\'';

    private final static char DOUBLE_QUOTATION = '"';

    private final static String KW_INCLUDE = "include";

    private final static String KW_MODULE = "module";

    private final static String END_OF_MULTI_LINE_COMMENT = "--]]";

    private Map<String, RedisNamedScript> includedCached = new HashMap<>();

    private StringBuilder builder = new StringBuilder();


    public RedisScriptParser(RedisScriptManager redisScriptManager, RedisScriptSource source) {
        this.redisScriptManager = redisScriptManager;
        this.source = source;
        this.sourceText = source.getSource();
    }

    private void error(String message) {
        throw new RedisScriptParserError("\n\n    An Error occurred during parsing a redis script [\"" + source.getUrl().getPath() + "\" : line " + lineNumber + "]:\n        " + message + "\n");
    }

    private char nextChar() {
        readIndex++;
        return currentChar();
    }

    private void record(char ch) {
        builder.append(ch);
    }

    private void record(String str) {
        builder.append(str);
    }


    private boolean match(char expect) {
        char ch = currentChar();
        return ch == expect;
    }

    private void skip(int dist) {
        readIndex += dist;
    }

    private boolean matchNL() {
        char ch = currentChar();
        boolean ret = ch == '\n' || (ch == '\r' && look(1) == '\n');
        if (ret) {
            lineNumber++;
        }
        return ret;
    }

    private char currentChar() {
        if (readIndex >= sourceText.length()) {
            error("Index out of bounds reading source text!");
        }
        return sourceText.charAt(readIndex);
    }


    private char look(int dist) {
        if (readIndex + dist >= sourceText.length()) {
            error("Index out of bounds reading source text!");
        }
        return sourceText.charAt(readIndex + dist);
    }


    private int lookWord(String word) {
        int i = 0;
        for ( ; i < word.length() && hasNext(); i++) {
            char chs = look(i);
            char chw = word.charAt(i);
            if (chs != chw) {
                return -1;
            }
        }
        return i;
    }

    private String parseQuotationString(char quotation) {
        StringBuilder strbuilder = new StringBuilder();
        if (match(quotation)) {
            do {
                char ch = nextChar();
                if (match(quotation)) break;
                strbuilder.append(ch);
            } while (hasNext());
        }
        return strbuilder.toString();
    }


    private String parseString() {
        if (match(SINGLE_QUOTATION))  return parseQuotationString(SINGLE_QUOTATION);
        else if (match(DOUBLE_QUOTATION)) return parseQuotationString(DOUBLE_QUOTATION);
        return null;
    }

    private boolean hasNext() {
        return readIndex + 1 < sourceText.length();
    }


    private boolean skipWhiteSpace() {
        boolean hasNL = false;
        boolean hasSpace = false;
        boolean hasTab = false;
        boolean endOfFile = false;
        char ch = currentChar();
        if (hasNext() && (ch == '\n' || ch == '\r' || ch == ' ' || ch == '\t')) {
            if (matchNL()) {
                hasNL = true;
            } else if (match(' ')) {
                hasSpace = true;
            } else if (match('\t')) {
                hasTab = true;
            }
            if (!hasNext()) {
                endOfFile = true;
            }
            do {
                ch = nextChar();
                if (matchNL()) {
                    hasNL = true;
                } else if (match(' ')) {
                    hasSpace = true;
                } else if (match('\t')) {
                    hasTab = true;
                }
                if (!hasNext()) {
                    endOfFile = true;
                }
            } while (hasNext() && (ch == '\n' || ch == '\r' || ch == ' ' || ch == '\t'));

            if (builder.length() > 0) {
                char lastCh = builder.charAt(builder.length() - 1);
                if (hasNL) {
                    if (!endOfFile && lastCh != '\n') {
                        record('\n');
                    }
                }
                if (hasSpace) {
                    if (!endOfFile && lastCh != ' ' && lastCh != '\n' && lastCh != '\t') {
                        record(' ');
                    }
                }
                if (hasTab) {
                    if (!endOfFile && lastCh != '\t' && lastCh != ' ') {
                        record("  ");
                    }
                }
            }
        }
        return hasNL;
    }


    private void skipComments() {
        boolean isSingleLine = skipSingleLineComments();
        if (!isSingleLine) {
            skipMultiLineComments();
        }
    }

    private boolean skipSingleLineComments() {
        char ch = currentChar();
        if (ch == '-' && hasNext() && look(1) == '-') {
            do {
                ch = nextChar();
                if (ch == '[' && hasNext() && look(1) == '[') {
                    nextChar();
                    return false;
                }
            } while (hasNext() && (look(1) != '\n' || (look(1) == '\r' && look(2) == '\n')));
        }
        return true;
    }

    private void skipMultiLineComments() {
        do {
            matchNL();
            int i = lookWord(END_OF_MULTI_LINE_COMMENT);
            if (i > 0) {
                skip(i - 1);
                return;
            } else {
                nextChar();
            }
        } while (hasNext());
    }

    private boolean isNeedInclude(String path) {
        if (includedCached.containsKey(path)) {
            return false;
        }
        return true;
    }

    public void include(String path) throws IOException {
        if (path == null || path.length() == 0) return;
        log.info("path="+path);
        String folder = "lua";
        String newPath = folder + '/' + path;

        URL url = Thread.currentThread().getContextClassLoader().getResource(newPath);
        if (!isNeedInclude(url.getPath())) {
            return;
        }
        RedisNamedScript script = null;
        try {
            script = redisScriptManager.loadScript(url);
        } catch (Throwable th) {
            error("include \"" + path + "\"" +
                    "\n                 ^" +
                    "\n        File \"" + url.getPath() + "\" cannot be found!\n" +
                    "\n Cause: " + th);
        }
        if (script == null) {
            error("Can not include script [" + path + "]!");
        }
        if (!script.isModule()) {
            error("include \"" + path + "\"" +
                  "\n                 ^" +
                  "\n        Script \"" + script.getName() + "\" is not a module, therefore it cannot be included!");
        }
//        record("---- include '" + path + "' ----\n");
        String scriptText = script.getScript();
        record(scriptText);
        includedCached.put(url.getPath(), script);
        includedCached.putAll(script.getIncludedModules());
    }

    public RedisNamedScript parse() throws IOException {
        char ch;
        log.info(" [Redis] 开始解析脚本[\"" + source.getName() + "\"]!");
        while (hasNext()) {
            nextChar();
            boolean hasNL = skipWhiteSpace();
            boolean canReadKeyword = hasNL || lineNumber == 1;
            ch = currentChar();
            if (ch == 'i' && canReadKeyword) {
                int i = lookWord(KW_INCLUDE);
                if (i > 0) {
                    skip(i);
                    skipWhiteSpace();
                    String path = parseString();
                    include(path);
                } else {
                    record(ch);
                }
            } else if (ch == 'm' && canReadKeyword) {
                int i = lookWord(KW_MODULE);
                if (i > 0) {
                    skip(i);
                    skipWhiteSpace();
                    this.moduleName = parseString();
                    this.isModule = true;
                } else {
                    record(ch);
                }
            } else if (ch == '-' && hasNext() && look(1) == '-') {
                skipComments();
            } else {
                record(ch);
            }
        }
        log.info(" [Redis] 解析脚本[\"" + source.getName() + "\"]完成!");
        return getScript();
    }

    private RedisNamedScript getScript() {
        RedisNamedScript script = new RedisNamedScript(
                source.getName(),
                source,
                builder.toString(),
                isModule,
                moduleName,
                includedCached);
        return script;
    }

}
