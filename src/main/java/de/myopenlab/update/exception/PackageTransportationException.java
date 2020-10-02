package de.myopenlab.update.exception;

/**
 * Exception that could occur during downloading or copying of Packages
 */
public class PackageTransportationException extends Exception{

    public PackageTransportationException() {
        super();
    }

    public PackageTransportationException(String message) {
        super(message);
    }

    public PackageTransportationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PackageTransportationException(Throwable cause) {
        super(cause);
    }

    protected PackageTransportationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
