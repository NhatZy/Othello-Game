package networking.exceptions;

import java.io.*;

/**
 * Signals that a client is unavailable or cannot be reached.
 * <p>
 * This exception is typically thrown when there are issues connecting to a client.
 * <p>
 * It extends the {@link Exception} class and implements {@link Serializable}.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 */
public class ClientUnavailableException extends Exception implements Serializable {
    /**
     * Custom serialization ID to control versioning of the class during deserialization.
     */
    @Serial
    private static final long serialVersionUID = 230720233L;

    /**
     * Constructs a new {@code ClientUnavailableException} with the specified detail message.
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public ClientUnavailableException(String message) {
        super(message);
    }
}