package org.snack.utils;

import lombok.NonNull;

public class CompareUtils {

    static public <T> boolean lt(@NonNull Comparable<T> a, T b) {
        return a.compareTo(b) < 0;
    }

    static public <T> boolean eq(@NonNull Comparable<T> a, T b) {
        return a.compareTo(b) == 0;
    }

    static public <T> boolean gt(@NonNull Comparable<T> a, T b) {
        return !lt(a, b);
    }

    static public <T> boolean le(@NonNull Comparable<T> a, T b) {
        return lt(a, b) || eq(a, b);
    }

    static public <T> boolean ge(@NonNull Comparable<T> a, T b) {
        return eq(a, b) || gt(a, b);
    }

}
