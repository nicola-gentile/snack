package org.snack.compiler.lexer;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor
public class Lexer {

//    @Getter
//    @AllArgsConstructor
//    enum LexerError {
//        INVALID_NUMBER_ERROR_MESSAGE(
//                "\"%s\" is not a valid literal number",
//                1,
//                "(?:0[^xXoObB]|[0-9])"
//        ),
//        UNKNOWN_CHARACTER_ERROR_MESSAGE(
//                "'%c' is unknown",
//                2,
//                "[^A-Za-z0-9\\.\\[\\]\\(\\)\\{\\}+\\-*/^:,<>=$\"'?&|~@%#]"
//        );
//
//
//        @NonNull final private String messageFormat;
//        final private int errorCode;
//        @NonNull final private String regex;
//    }

    static private String anchorToBegin(@NonNull String regex) {
        return String.format("^(?:%s)", regex);
    }

    static private String skipSeparator(@NonNull String regex) {
        return String.format("[\\s\\r\\t]*%s", regex);
    }

    static private Pattern makeMultilinePattern(@NonNull String regex) {
        return Pattern.compile(regex, Pattern.MULTILINE);
    }

    static private Pattern makePattern(@NonNull String regex) {
        return makeMultilinePattern(anchorToBegin(skipSeparator(regex)));
    }

    @NonNull @Getter
    RegexScanner scanner;

    public static final String IDENTIFIER_REGEX = "\\$?[_a-zA-Z][_a-zA-Z0-9'?]*!?|[+\\-*/<>|~^&]+";
    public static final String REAL_NUM_REGEX = "[+-]?(?:([0-9]+\\.[0-9]*)|(\\.[0-9]+))(?:[eE][+-]?[0-9]+)?|[0-9]+[Ee][+-]?[0-9]+";
    public static final String INTEGER_NUM_REGEX = "[+-]?(?:0[xX][0-9A-Fa-f]+|0[bB][01]+|0[oO][0-7]+|[0-9]+)";
    public static final String COMMENT_REGEX = "#\\{.*\\}#|#[^\\n]*";

    @Getter(lazy=true)
    final private static Pattern identifierPattern = makePattern(IDENTIFIER_REGEX);
    @Getter(lazy = true)
    final private static Pattern integerNumberPattern = makePattern(INTEGER_NUM_REGEX);
    @Getter(lazy = true)
    final private static Pattern realNumberPattern = makePattern(REAL_NUM_REGEX);
    @Getter(lazy = true)
    final private static Pattern commentPattern = makePattern(COMMENT_REGEX);

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
        val pattern = makePattern(syntaxElement.getRegex());
        return extractPattern(pattern)
                .map(s -> syntaxElement);
    }

    public Optional<Identifier> scanIdentifier() {
        return extractPattern(getIdentifierPattern())
                .map(Identifier::new);
    }

    public Optional<Comment> scanComment() {
        return extractPattern(getCommentPattern())
                .map(s -> {
                    val pattern = Pattern.compile("#\\{(.*)}#|#([^\\n]*)");
                    val matcher = pattern.matcher(s);
                    return new Comment(matcher.find() ?//should always be true
                        matcher.group(1) :
                        "");
                });
    }

    public Optional<InvalidToken> scanInvalidToken() {
        return null;
    }
}
