package networking.exceptions;

import java.io.*;

/**
 * Exception thrown to indicate a protocol-related error.
 * <p>
 * This exception is used when the communication protocol between server and client is violated.
 * <p>
 * It extends the {@link Exception} class and implements {@link Serializable}.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 */
public class ProtocolException extends Exception implements Serializable {
    /**
     * Custom serialization ID to control versioning of the class during deserialization.
     */
    @Serial
    private static final long serialVersionUID = 230720231L;

    /**
     * Constructs a new {@code ProtocolException} with the specified detail message.
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public ProtocolException(String message) {
        super(message);
    }
}