package org.snack.compiler.lexer;

import lombok.NonNull;

public record Identifier(@NonNull String id) implements Token {

    public static final
    String VARIABLE_REGEX = "[A-Za-z_][A-Za-z0-9'?]*";
    public static final
    String DATA_REGEX = "[A-Za-z_][A-Za-z0-9'?]*";
    public static final
    String TRAIT_REGEX = "[A-Za-z_][A-Za-z0-9'?]*";
    public static final
    String PURE_FUNCTION_REGEX = "[A-Za-z_][A-Za-z0-9'?]*";
    public static final
    String SYMBOL_FUNCTION_REGEX = "[+\\-*/<>|~^&]+";
    public static final
    String DIRTY_FUNCTION_REGEX = "[A-Za-z_][A-Za-z0-9'?]*!";
    public static final
    String MODULE_REGEX = "[a-z]([a-z_]*[a-z])?";
    public static final
    String SELF_FIELD_REGEX = "\\$[A-Za-z_][A-Za-z0-9'?!]*";
    public static final
    String METADATA_FIELD_REGEX = "\\$[A-Za-z_][A-Za-z0-9'?]";

    public boolean isVariableName() {
        return id.matches(VARIABLE_REGEX);
    }

    public boolean isModuleName() {
        return id.matches(MODULE_REGEX);
    }

    public boolean isDataName() {
        return id.matches(DATA_REGEX);
    }

    public boolean isTraitName() {
        return id.matches(TRAIT_REGEX);
    }

    public boolean isFunctionName() {
        return isPureFunctionName() ||
                isDirtyFunctionName() ||
                isSymbolFunctionName();
    }

    public boolean isPureFunctionName() {
        return id.matches(PURE_FUNCTION_REGEX);
    }

    public boolean isDirtyFunctionName() {
        return id.matches(DIRTY_FUNCTION_REGEX);
    }

    public boolean isSymbolFunctionName() {
        return id.matches(SYMBOL_FUNCTION_REGEX);
    }

    public boolean isSelfFieldName() {
        return id.matches(SELF_FIELD_REGEX);
    }

    public boolean isMetadataFieldName() {
        return id.matches(METADATA_FIELD_REGEX);
    }

}
