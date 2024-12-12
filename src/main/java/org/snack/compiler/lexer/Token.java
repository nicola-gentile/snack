package org.snack.compiler.lexer;

public sealed interface Token permits
        IntegerNumber,
        RealNumber,
        Identifier,
        SyntaxElement,
        Char,
        Str,
        InvalidToken,
        Comment,
        NewLine,
        SelfField
{
}
