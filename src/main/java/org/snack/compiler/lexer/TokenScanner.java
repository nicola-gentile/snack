package org.snack.compiler.lexer;

import lombok.NonNull;

import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TokenScanner implements AutoCloseable, RegexScanner {

    public TokenScanner(@NonNull Readable readable) {
        this.scanner = new Scanner(readable);
    }

    public TokenScanner(@NonNull String source) {
        this.scanner = new Scanner(source);
    }

    @NonNull
    Scanner scanner;

    @Override
    public void close() throws Exception {
        scanner.close();
    }

    @Override
    public Optional<String> next(@NonNull Pattern pattern) {
        return Optional.ofNullable(scanner.findWithinHorizon(pattern, 0));
    }

}
