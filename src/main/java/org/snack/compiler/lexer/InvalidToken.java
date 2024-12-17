package org.snack.compiler.lexer;

import lombok.NonNull;

import java.util.Map;

public record InvalidToken(
        @NonNull String text,
        @NonNull LexerErrorCode errorCode
) implements Token {

    private static final Map<LexerErrorCode, String> codeMessageMap = Map.of(
            LexerErrorCode.INVALID_CHARACTER_TOKEN, "Invalid character syntax",
            LexerErrorCode.INVALID_STRING_TOKEN, "Invalid string syntax",
            LexerErrorCode.UNKNOWN_ESCAPE_CHARACTER, "Unknown escape character"
    );

    public String getErrorMessage() {
        return codeMessageMap.get(errorCode());
    }

}
