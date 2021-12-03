

package QueryImplementation;

public final class ExceptionHandler extends Exception {

    private final String errorMessage;

    public ExceptionHandler(final String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String displayException() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "Exception is " +"errorMessage='" + errorMessage +"}";
    }
}