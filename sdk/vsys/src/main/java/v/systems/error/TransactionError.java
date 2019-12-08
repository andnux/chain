package v.systems.error;

public class TransactionError extends ApiError {

    private String status;
    private String details;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public TransactionError(String details) {
        this("error", details);
    }

    public TransactionError(String status, String details) {
        super(details);
        this.details = details;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return details;
    }
}
