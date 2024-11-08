package org.snack.compiler.lexer;

import java.math.BigDecimal;

public record RealNumber(BigDecimal value) implements Token {

    private static final Integer FLOAT32_MANTIX_BIT_LENGTH = 23;
    private static final Integer FLOAT32_EXPONENT_BIT_LENGTH = 8;
    private static final Integer FLOAT64_MANTIX_BIT_LENGTH = 52;
    private static final Integer FLOAT64_EXPONENT_BIT_LENGTH = 11;

    public boolean fitFloat32() {
        return value.unscaledValue().bitLength() <= FLOAT32_MANTIX_BIT_LENGTH && value.scale() <= (1<<FLOAT32_EXPONENT_BIT_LENGTH);
    }

    public boolean fitFloat64() {
        return value.unscaledValue().bitLength() <= FLOAT64_MANTIX_BIT_LENGTH && value.scale() <= (1<<FLOAT64_EXPONENT_BIT_LENGTH);
    }

}
