package exceptions;

import java.time.DateTimeException;

public class InvalidDateTimeFormatException extends DateTimeException {
    public InvalidDateTimeFormatException(String message) {
        super(message);
    }
}
