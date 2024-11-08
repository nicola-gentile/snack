package org.snack.compiler.lexer;

import lombok.NonNull;

public record Identifier(String id) implements Token {

    public final static String VARIABLE_REGEX = "[A-Za-z_][A-Za-z0-9'?]*";
    public final static String DATA_REGEX = "[A-Za-z_][A-Za-z0-9'?]*";
    public final static String TRAIT_REGEX = "[A-Za-z_][A-Za-z0-9'?]*";
    public final static String FUNCTION_REGEX = "[A-Za-z_][A-Za-z0-9'?]*!?";
    public final static String PURE_FUNCTION_REGEX = "[A-Za-z_][A-Za-z0-9'?]*";
    public final static String SYMBOL_FUNCTION_REGEX = "[+\\-*/<>|~^&]+";
    public final static String DIRTY_FUNCTION_REGEX = "[A-Za-z_][A-Za-z0-9'?]*!";
    public final static String MODULE_REGEX = "[a-z]([a-z_]*[a-z])?";
    public final static String MACRO_REGEX = "\\$[A-Za-z_0-9]+";

    public boolean isValidVariableName() {
        return id.matches(VARIABLE_REGEX);
    }

    public boolean isValidModuleName() {
        return id.matches(MODULE_REGEX);
    }

    public boolean isValidDataName() {
        return id.matches(DATA_REGEX);
    }

    public boolean isValidTraitName() {
        return id.matches(TRAIT_REGEX);
    }

    public boolean isValidFunctionName() {
        return isValidPureFunctionName() || isValidDirtyFunctionName() || isValidSymbolFunctionName();
    }

    public boolean isValidPureFunctionName() {
        return id.matches(PURE_FUNCTION_REGEX);
    }

    public boolean isValidDirtyFunctionName() {
        return id.matches(DIRTY_FUNCTION_REGEX);
    }

    public boolean isValidSymbolFunctionName() {
        return id.matches(SYMBOL_FUNCTION_REGEX);
    }

    public boolean isValidMacroName() {
        return id.matches(MACRO_REGEX);
    }


}
