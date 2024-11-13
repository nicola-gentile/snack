package org.snack.compiler.exception;

import lombok.NonNull;

public class BugReportException extends RuntimeException {

    private static String formatError(@NonNull String message) {
        return String.format("Snack compiler bug report:> %s", message);
    }

    public BugReportException(String message) {
        super(formatError(message));
    }

    public BugReportException(String message, Throwable cause) {
        super(formatError(message), cause);
    }

    public BugReportException(Throwable cause) {
        super(cause);
    }

    public BugReportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(formatError(message), cause, enableSuppression, writableStackTrace);
    }
}
