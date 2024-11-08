package org.snack.compiler.lexer;

import lombok.NonNull;

public record Comment(@NonNull String text) implements Token {

}
