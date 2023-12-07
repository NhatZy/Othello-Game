package networking.exceptions;

import java.io.*;

/**
 * Exception thrown to indicate a user-initiated exit request.
 * <p>
 * This exception is used when the user initiates a request to exit the application.
 * <p>
 * It extends the {@link Exception} class and implements {@link Serializable}.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 */
public class UserExitRequestException extends Exception implements Serializable {
    /**
     * Custom serialization ID to control versioning of the class during deserialization.
     */
    @Serial
    private static final long serialVersionUID = 230720234L;

    /**
     * Constructs a new {@code UserExitRequestException} with the specified detail message.
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public UserExitRequestException(String message) {
        super(message);
    }
}