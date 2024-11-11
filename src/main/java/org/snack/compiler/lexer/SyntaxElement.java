package org.snack.compiler.lexer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
public enum SyntaxElement implements Token {

    IF("if"),
    ELSE("else"),
    END("end"),
    MATCH("match"),
    CASE("case"),
    DEF("def"),
    DEFP("defp"),
    IMPORT("import"),
    OPEN_ROUND_BRACKET("("),
    CLOSE_ROUND_BRACKET(")"),
    OPEN_SQUARE_BRACKET("["),
    CLOSE_SQUARE_BRACKET("]"),
    COMMA(","),
    PIPE("|"),
    DOUBLEDOT(".."),
    DOT("."),
    AMPERSAND("&");

    @Getter
    @NonNull
    final String text;

}
