package org.snack.compiler.lexer;

import lombok.*;
import org.snack.compiler.exception.BugReportException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor
public class Lexer {

    static private String anchorToBegin(@NonNull String regex) {
        return String.format("^(?:%s)", regex);
    }

    static private String prepareRegex(@NonNull String regex) {
        return anchorToBegin(regex);
    }

    @NonNull @Getter
    RegexScanner scanner;

    public static final String ESCAPE_CHARACTERS = "rtnbf'\\\\'";
    public static final String IDENTIFIER_REGEX = prepareRegex("\\$?[_a-zA-Z][_a-zA-Z0-9'?]*!?|[+\\-*/<>|~^&]+");
    public static final String REAL_NUM_REGEX = prepareRegex("[+-]?(?:([0-9]+\\.[0-9]*)|(\\.[0-9]+))(?:[eE][+-]?[0-9]+)?|[0-9]+[Ee][+-]?[0-9]+");
    public static final String INTEGER_NUM_REGEX = prepareRegex("([+-]?(?:0[xX][0-9A-Fa-f]+|0[bB][01]+|0[oO][0-7]+|[0-9]+))");
    public static final String ONE_LINE_COMMENT_REGEX = prepareRegex("#(\\n|[^{][^\\n]*)");
    public static final String MULTI_LINE_COMMENT_REGEX = prepareRegex("#\\{(.*?)\\}#");
    private static final String SINGLE_CHAR_REGEX = prepareRegex("'([^\\\\])'");
    private static final String ESCAPE_CHAR_REGEX =  prepareRegex(String.format("'\\\\([%s])'", ESCAPE_CHARACTERS));
    private static final String UNICODE_CHAR_CODE_REGEX =  prepareRegex("'\\\\[xu](\\p{XDigit}{2}(?:\\p{XDigit}{2})?)'");
    public static final String UNKNOWN_ESCAPE_CHARACTER_REGEX = prepareRegex(String.format("'\\\\([^%s])'", ESCAPE_CHARACTERS));
    public static final String STR_REGEX = "\"((.*)(?<!\\\\))\"";
    public static final String INVALID_CHARACTER_REGEX = "'\\\\?(.)?";
    public static final String INVALID_STR_REGEX = "(\"[^\\n]*)";

    @Getter(lazy=true)
    final private static Pattern identifierPattern = Pattern.compile(IDENTIFIER_REGEX);
    @Getter(lazy = true)
    final private static Pattern integerNumberPattern = Pattern.compile(INTEGER_NUM_REGEX);
    @Getter(lazy = true)
    final private static Pattern realNumberPattern = Pattern.compile(REAL_NUM_REGEX);
    @Getter(lazy = true)
    final private static Pattern oneLineCommentPattern = Pattern.compile(ONE_LINE_COMMENT_REGEX, Pattern.DOTALL);
    @Getter(lazy = true)
    final private static Pattern multiLineCommentPattern = Pattern.compile(MULTI_LINE_COMMENT_REGEX, Pattern.DOTALL);
    @Getter(lazy = true)
    final private static Pattern newLinePattern = Pattern.compile("\n");
    @Getter(lazy = true)
    final private static Pattern singleCharPattern = Pattern.compile(SINGLE_CHAR_REGEX);
    @Getter(lazy = true)
    final private static Pattern escapeCharPattern = Pattern.compile(ESCAPE_CHAR_REGEX);
    @Getter(lazy = true)
    final private static Pattern unicodeCharPattern = Pattern.compile(UNICODE_CHAR_CODE_REGEX);
    @Getter(lazy = true)
    final private static Pattern strPattern = Pattern.compile(STR_REGEX);
    @Getter(lazy = true)
    final private static Pattern invalidCharPattern = Pattern.compile(INVALID_CHARACTER_REGEX);
    @Getter(lazy = true)
    final private static Pattern invalidStrPattern = Pattern.compile(INVALID_STR_REGEX);
    @Getter(lazy = true)
    final private static Pattern unknownEscapeCharacterPattern = Pattern.compile(UNKNOWN_ESCAPE_CHARACTER_REGEX);

    private Optional<String> extractPattern(@NonNull Pattern pattern) {
        return getScanner().next(pattern);
    }

    public Optional<IntegerNumber> scanIntegerNumber() {
        return extractPattern(getIntegerNumberPattern())
                .map(s -> {
                    int radix =
                            s.startsWith("0b") || s.startsWith("0B") ? 2 :
                            s.startsWith("0o") || s.startsWith("0O") ? 8 :
                            s.startsWith("0x") || s.startsWith("0X") ? 16 :
                                                                       10;
                    return new IntegerNumber(new BigInteger(radix == 10 ? s : s.substring(2), radix));
                });
    }

    public Optional<RealNumber> scanRealNumber() {
        return extractPattern(getRealNumberPattern())
                .map(s -> new RealNumber(new BigDecimal(s)));
    }

    public Optional<SyntaxElement> scanSyntaxElement(@NonNull SyntaxElement syntaxElement) {
        return getScanner().next(syntaxElement.getPattern())
                .map(s -> syntaxElement);
    }

    public Optional<Identifier> scanIdentifier() {
        return extractPattern(getIdentifierPattern())
                .map(Identifier::new);
    }

    private String extractOneLineComment(@NonNull String src) {
        val matcher = getOneLineCommentPattern().matcher(src);
        if(matcher.find()) {
            return matcher.group(1);
        }
        else {
            throw new BugReportException(String.format("Error parsing \"%s\" as one line comment", src));
        }
    }

    private String extractMultilineComment(@NonNull String src) {
        val matcher = getMultiLineCommentPattern().matcher(src);
        if(matcher.find()) {
            return matcher.group(1);
        }
        else {
            throw new BugReportException(String.format("Error parsing \"%s\" as multiline comment", src));
        }
    }

    public Optional<Comment> scanComment() {
        return getScanner().next(getOneLineCommentPattern()).map(s -> new Comment(extractOneLineComment(s)))
                .or(() -> getScanner().next(getMultiLineCommentPattern()).map(s -> new Comment(extractMultilineComment(s))));
    }

    public Optional<InvalidToken> scanInvalidToken() {
        return getScanner().next(getUnknownEscapeCharacterPattern()).map(s -> new InvalidToken(s, LexerErrorCode.UNKNOWN_ESCAPE_CHARACTER))
                .or(() -> getScanner().next(getInvalidStrPattern()).map(s -> new InvalidToken(s, LexerErrorCode.INVALID_STRING_TOKEN)))
                .or(() -> getScanner().next(getInvalidCharPattern()).map(s -> new InvalidToken(s, LexerErrorCode.INVALID_CHARACTER_TOKEN)));
    }

    public Optional<NewLine> scanNewLine() {
        return getScanner()
                .next(getNewLinePattern())
                .map(s -> new NewLine());
    }

    private int strToSingleChar(@NonNull String src) {
        val matcher = getSingleCharPattern().matcher(src);
        if(matcher.find()) {
            return matcher.group(1).charAt(0);
        }
        throw new BugReportException(String.format("Error in parsing \"%s\" as character", src));
    }

    private int strToEscapeChar(String src) {
        val matcher = getEscapeCharPattern().matcher(src);
        if(matcher.find()) {
            return switch (matcher.group(1).charAt(0)) {
                case 'n' -> '\n';
                case 't' -> '\t';
                case 'r' -> '\r';
                case '\\' -> '\\';
                case 'b' -> '\b';
                case '\'' -> '\'';
                case 'f' -> '\f';
                default -> throw new BugReportException("Unexpected escape character");
            };
        }
        throw new BugReportException(String.format("Could not match character '%s' as escape character", src));
    }

    private int strToUnicodeChar(String src) {
        val matcher = getUnicodeCharPattern().matcher(src);
        if(matcher.find()) {
            return Integer.parseInt(matcher.group(1), 16);
        }
        throw new BugReportException("Could not match unicode character");
    }

    public Optional<Char> scanChar() {
        return getScanner().next(getSingleCharPattern()).map(s -> new Char(strToSingleChar(s)))
                .or(() -> getScanner().next(getEscapeCharPattern()).map(s -> new Char(strToEscapeChar(s))))
                .or(() -> getScanner().next(getUnicodeCharPattern()).map(s -> new Char(strToUnicodeChar(s))));
    }

    public Optional<Str> scanStr() {
        return getScanner()
                .next(getStrPattern())
                .map(s -> {
                    val matcher = getStrPattern().matcher(s);
                    if(matcher.find()) {
                        return new Str(matcher.group(1));
                    }
                    throw new BugReportException(String.format("could not be possible to match the string \"%s\"", s));
                });
    }

}
