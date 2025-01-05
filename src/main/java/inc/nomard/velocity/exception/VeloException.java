package inc.nomard.velocity.exception;

public class VeloException extends RuntimeException {
    private final ErrorCode code;

    public VeloException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}