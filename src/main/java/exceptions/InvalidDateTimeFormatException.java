package exceptions;

public class InvalidDateTimeFormatException extends RuntimeException {
  public InvalidDateTimeFormatException(String message) {
    super(message);
  }
}
