package v.systems.error;

import androidx.annotation.Nullable;

public class VException extends Exception {

    private String message;

    @Nullable
    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VException(String message) {
        super(message);
        this.message = message;
    }

    public VException() {
        super();
        this.message = "Unexpected error occurred.";
    }
}
