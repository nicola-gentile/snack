package org.snack.compiler.lexer;

import lombok.NonNull;

import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

public class LexerScanner implements AutoCloseable, RegexScanner {

    private static final String DELIMITER_REGEX = "[\\s\\r\\t]*";

    private void prepareScanner() {
        this.scanner.useDelimiter(DELIMITER_REGEX);
    }

    public LexerScanner(@NonNull Readable readable) {
        this.scanner = new Scanner(readable);
        prepareScanner();
    }

    public LexerScanner(@NonNull String source) {
        this.scanner = new Scanner(source);
        prepareScanner();
    }

    @NonNull
    Scanner scanner;

    @Override
    public void close() throws Exception {
        scanner.close();
    }

    @Override
    public Optional<String> next(@NonNull Pattern pattern) {
        return Optional.of(scanner.findInLine(pattern));
    }

}
