package org.snack.compiler.lexer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.io.InputStream;
import java.io.Reader;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

@AllArgsConstructor
public class Lexer {

    @NonNull
    Scanner scanner;

    public static final String IDENTIFIER_REGEX = "\\$?[_a-zA-Z][_a-zA-Z0-9'?]*!?|[+\\-*/<>|~^&]+";
    public static final String REAL_NUM_REGEX = "(?:([0-9]+\\.[0-9]*)|(\\.[0-9]+))(?:[eE][0-9]+)?";
    public static final String INTEGER_NUM_REGEX = "[0-9]+|0[xX][0-9A-Fa-f]+|0[bB][01]+|0[oO][0-7]+";
    @Getter(lazy=true)
    final private static Pattern identifierPattern = Pattern.compile(IDENTIFIER_REGEX);
    @Getter(lazy = true)
    final private static Pattern realNumberPattern = Pattern.compile(REAL_NUM_REGEX);
    @Getter(lazy = true)
    final private static Pattern integerNumberPattern = Pattern.compile(INTEGER_NUM_REGEX);

    public Optional<IntegerNumber> scanIntegerNumber() {
        return scanner.hasNext(getIntegerNumberPattern()) ?
                Optional.of(new IntegerNumber(scanner.next(getIntegerNumberPattern()))) :
                Optional.empty();
    }

    public Optional<RealNumber> scanRealNumber() {
        return scanner.hasNext(getRealNumberPattern()) ?
                Optional.of(new RealNumber(scanner.next(getRealNumberPattern()))) :
                Optional.empty();
    }

    public Optional<SyntaxElement> scanExactly(@NonNull String keyword) {
        return scanner.hasNext(keyword) ?
                Optional.of(new SyntaxElement(scanner.next(keyword))) :
                Optional.empty();
    }

    public Optional<Identifier> scanIdentifier() {
        return scanner.hasNext(getIdentifierPattern()) ?
                Optional.of(new SyntaxElement(scanner.next(getIdentifierPattern()))) :
                Optional.empty();
    }

}
