package org.snack.compiler.lexer;

import lombok.Getter;
import lombok.NonNull;

import java.util.Map;
import java.util.Set;

public record SyntaxElement(@NonNull String value) implements Token {

    @Getter(lazy=true)
    final static private Set<String> syntaxELements = Set.of(
            "if", "do", "else", "end", "match", "case", "def", "defp", "import",
            "(", ")", "[", "]", "{", "}", ",", "|", ".", "..", "&"
    );

}
