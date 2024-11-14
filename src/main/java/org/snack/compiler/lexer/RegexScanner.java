package org.snack.compiler.lexer;

import lombok.NonNull;

import java.util.Optional;
import java.util.regex.Pattern;

public interface RegexScanner {

    default Optional<String> next(@NonNull String regex) {
        return next(Pattern.compile(regex));
    }

    Optional<String> next(@NonNull Pattern pattern);

    boolean isEmpty();

}
