package v.systems.entity;

import java.io.Serializable;

public class Result  implements Serializable {
    private boolean ok;
    private String message;

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Result success() {
        return new Result(true, "Success");
    }

    public static Result fail(String errorMessage) {
        return new Result(false, errorMessage);
    }

    public Result(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }
}
