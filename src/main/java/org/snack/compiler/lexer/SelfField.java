package org.snack.compiler.lexer;

import lombok.NonNull;

public record SelfField(@NonNull String id) implements Token {

    public String getRealName() {
        return id().substring(1);
    }

}
