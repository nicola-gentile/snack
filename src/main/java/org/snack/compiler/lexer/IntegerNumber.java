package org.snack.compiler.lexer;

import lombok.NonNull;

import java.math.BigInteger;

public record IntegerNumber(BigInteger value) implements Token {

    private static final BigInteger INT8_MIN = new BigInteger(String.valueOf(Byte.MIN_VALUE));
    private static final BigInteger INT8_MAX = new BigInteger(String.valueOf(Byte.MAX_VALUE));
    private static final BigInteger INT16_MIN = new BigInteger(String.valueOf(Short.MIN_VALUE));
    private static final BigInteger INT16_MAX = new BigInteger(String.valueOf(Short.MAX_VALUE));
    private static final BigInteger INT32_MIN = new BigInteger(String.valueOf(Integer.MIN_VALUE));
    private static final BigInteger INT32_MAX = new BigInteger(String.valueOf(Integer.MAX_VALUE));
    private static final BigInteger INT64_MIN = new BigInteger(String.valueOf(Long.MIN_VALUE));
    private static final BigInteger INT64_MAX = new BigInteger(String.valueOf(Long.MAX_VALUE));
    private static final BigInteger UINT8_MIN = BigInteger.ZERO;
    private static final BigInteger UINT8_MAX = INT8_MAX.shiftLeft(1);
    private static final BigInteger UINT16_MIN = BigInteger.ZERO;
    private static final BigInteger UINT16_MAX = INT16_MAX.shiftLeft(1);
    private static final BigInteger UINT32_MAX = BigInteger.ZERO;
    private static final BigInteger UINT32_MIN = INT32_MAX.shiftLeft(1);
    private static final BigInteger UINT64_MIN = BigInteger.ZERO;
    private static final BigInteger UINT64_MAX = INT64_MAX.shiftLeft(1);

    private boolean lessThanOrEqualsToBigInteger(@NonNull BigInteger a, @NonNull BigInteger b) {
        return a.compareTo(b) <= 0;
    }

    private boolean inInclusiveRange(@NonNull BigInteger v, @NonNull BigInteger low, @NonNull BigInteger high) {
        return lessThanOrEqualsToBigInteger(low, v) &&
                lessThanOrEqualsToBigInteger(v, high);
    }

    public boolean fitInt8() {
        return inInclusiveRange(value, INT8_MIN, INT8_MAX);
    }
    
    public boolean fitUInt8() {
        return inInclusiveRange(value, UINT8_MIN, UINT8_MAX);
    }
    
    public boolean fitInt16() {
        return inInclusiveRange(value, INT16_MIN, INT16_MAX);
    }
    
    public boolean fitUInt16() {
        return inInclusiveRange(value, UINT16_MIN, UINT16_MAX);
    }

    public boolean fitInt32() {
        return inInclusiveRange(value, INT32_MIN, INT32_MAX);
    }

    public boolean fitUInt32() {
        return inInclusiveRange(value, UINT32_MIN, UINT32_MAX);
    }
    
    public boolean fitInt64() {
        return inInclusiveRange(value, INT64_MIN, INT64_MAX);
    }

    public boolean fitUInt64() {
        return inInclusiveRange(value, UINT64_MIN, UINT64_MAX);
    }

}

//12
//12.34
