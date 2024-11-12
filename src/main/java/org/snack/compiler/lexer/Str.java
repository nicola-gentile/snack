package org.snack.compiler.lexer;

import lombok.NonNull;

public record Str(@NonNull String value) implements Token {
}
