package networking.exceptions;

import java.io.*;

/**
 * Exception thrown to indicate that the server is currently unavailable.
 * <p>
 * This can occur due to issues such as a server shutdown or network problems.
 * <p>
 * It extends the {@link Exception} class and implements {@link Serializable}.
 * @author Dinh Thuy Nhat Vy
 * @version 1.0, 12/07/2023
 */
public class ServerUnavailableException extends Exception implements Serializable {
    /**
     * Custom serialization ID to control versioning of the class during deserialization.
     */
    @Serial
    private static final long serialVersionUID = 230720232L;

    /**
     * Constructs a new {@code ServerUnavailableException} with the specified detail message.
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public ServerUnavailableException(String message) {
        super(message);
    }
}