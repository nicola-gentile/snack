package org.snack.compiler.lexer;

import lombok.NonNull;

public record InvalidToken(@NonNull String text, @NonNull String errorMessage) implements Token {
}
