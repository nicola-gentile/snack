package org.snack.compiler.lexer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
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
    OPEN_ROUND_BRACKET("\\("),
    CLOSE_ROUND_BRACKET("\\)"),
    OPEN_SQUARE_BRACKET("\\["),
    CLOSE_SQUARE_BRACKET("\\]"),
    OPEN_CURLY_BRACKET("\\{"),
    CLOSE_CURLY_BRACKET("\\}"),
    COMMA(","),
    PIPE("\\|"),
    DOUBLEDOT("\\.\\."),
    DOT("\\."),
    AMPERSAND("&");
    @NonNull final String regex;

}
