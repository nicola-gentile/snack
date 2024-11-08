package org.snack.compiler.lexer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

@AllArgsConstructor
public class Lexer {

    @NonNull
    Scanner scanner;

    public static final String IDENTIFIER_REGEX = "\\$?[_a-zA-Z][_a-zA-Z_0-9'\\?]*!?|[+\\-*/<>|~^&]+";

    @Getter(lazy=true)
    final private static Pattern identifierPattern = Pattern.compile("^\\$?[_a-zA-Z][_a-zA-Z0-9'?]*!?");

    public Optional<IntegerNumber> scanIntegerNumber() {
        return scanner.hasNextBigInteger() ?
                Optional.of(new IntegerNumber(scanner.nextBigInteger())) :
                Optional.empty();
    }

    public Optional<RealNumber> scanRealNumber() {
        return scanner.hasNextBigDecimal() ?
                Optional.of(new RealNumber(scanner.nextBigDecimal())) :
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
