package org.snack.compiler.lexer;

import lombok.Getter;
import lombok.NonNull;

import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TokenScanner implements AutoCloseable, RegexScanner {

    @Getter(lazy = true)
    private static final
    Pattern delimiterPattern = Pattern.compile("[ \\r\\t]*");

    @NonNull
    Scanner scanner;

    private void prepareScanner() {
        scanner.useDelimiter("[ \t\r]+");
    }

    public TokenScanner(@NonNull Readable readable) {
        this.scanner = new Scanner(readable);
        prepareScanner();
    }

    public TokenScanner(@NonNull String source) {
        this.scanner = new Scanner(source);
        prepareScanner();
    }

    @Override
    public void close() throws Exception {
        scanner.close();
    }

    @Override
    public Optional<String> next(@NonNull Pattern pattern) {
        scanner.skip(getDelimiterPattern());
        return Optional.ofNullable(scanner.findWithinHorizon(pattern, 0));
    }

    @Override
    public boolean isEmpty() {
        return !scanner.hasNextLine();
    }
}
