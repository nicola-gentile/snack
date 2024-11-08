package org.snack.compiler.lexer;

import lombok.NonNull;

public class Characters {

    static public boolean isLowerCase(@NonNull Integer c) {
        return ('a' <= c && c <= 'z');
    }

    static public boolean isUpperCase(@NonNull Integer c) {
        return ('A' <= c && c <= 'Z');
    }

    static public boolean isLetter(@NonNull Integer c) {
        return isLowerCase(c) || isUpperCase(c);
    }

    static public boolean isDecDigit(@NonNull Integer c) {
        return ('0' <= c && c <= '9');
    }

    static public boolean isHexDigit(@NonNull Integer c) {
        return isDecDigit(c) || ('a' <= c && c <= 'f') || ('A' <= c && c <= 'F');
    }

    static public boolean isBinDigit(@NonNull Integer c) {
        return c=='0'||c=='1';
    }

    static public boolean isOctDigit(@NonNull Integer c) {
        return '0' <= c && c <= '7';
    }

    static public boolean isLetterOrUnderscore(@NonNull Integer c) {
        return Characters.isLetter(c) || c=='_';
    }

    static public boolean isSnackVariableIdentifierStart(@NonNull Integer c) {
        return Characters.isLetterOrUnderscore(c);
    }

    static public boolean isSnackDataIdentifierStart(@NonNull Integer c) {
        return Characters.isLetterOrUnderscore(c);
    }

    static public boolean isSnackTraitIdentifierStart(@NonNull Integer c) {
        return Characters.isLetterOrUnderscore(c);
    }

    static public boolean isSnackFunctionIdentifierStart(@NonNull Integer c) {
        return Characters.isLetterOrUnderscore(c);
    }

    static public boolean isSnackModuleIdentifierStart(@NonNull Integer c) {
        return isLowerCase(c);
    }

    static public boolean isSnackVariableIdentifierPart(@NonNull Integer c) {
        return Characters.isLetterOrUnderscore(c) || Characters.isDecDigit(c) || c=='\'' || c=='?';
    }

    static public boolean isSnackDataIdentifierPart(@NonNull Integer c) {
        return Characters.isSnackVariableIdentifierPart(c);
    }

    static public boolean isSnackTraitIdentifierPart(@NonNull Integer c) {
        return Characters.isSnackVariableIdentifierPart(c);
    }

    static public boolean isSnackModuleIdentifierPart(@NonNull Integer c) {
        return isSnackModuleIdentifierStart(c);
    }

    static public boolean isSnackFunctionIdentifierPart(@NonNull Integer c) {
        return Characters.isSnackVariableIdentifierPart(c) || c=='!';
    }


}
