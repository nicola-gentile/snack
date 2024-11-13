package org.snack.compiler.lexer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor
public enum SyntaxElement implements Token {

    IF("if"),
    ELSE("else"),
    END("end"),
    MATCH("match"),
    CASE("case"),
    DEF("def"),
    DEFP("defp"),
    IMPORT("import"),
    DO("do"),
    OPEN_ROUND_BRACKET("\\("),
    CLOSE_ROUND_BRACKET("\\)"),
    OPEN_SQUARE_BRACKET("\\["),
    CLOSE_SQUARE_BRACKET("\\]"),
    OPEN_CURLY_BRACKET("\\{"),
    CLOSE_CURLY_BRACKET("\\}"),
    COMMA(","),
    PIPE("\\|"),
    DOUBLE_DOT("\\.\\."),
    DOT("\\."),
    AMPERSAND("&"),
    COLON(":");
    @NonNull final String regex;

    @Getter(lazy = true)
    final private Pattern pattern = Pattern.compile(String.format("^(?:%s)", getRegex()));

}
