package VisualLogic.exception;

import java.io.IOException;

/**
 * Exceptions that are thrown while doing IO work such as copying or reading files
 */
public class VisualLogicIoException extends IOException {

    public VisualLogicIoException() {
        super();
    }

    public VisualLogicIoException(String message) {
        super(message);
    }

    public VisualLogicIoException(String message, Throwable cause) {
        super(message, cause);
    }

    public VisualLogicIoException(Throwable cause) {
        super(cause);
    }
}
