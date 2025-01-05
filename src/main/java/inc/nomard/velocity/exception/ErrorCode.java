package inc.nomard.velocity.exception;

public enum ErrorCode {
    TEMPLATE_NOT_FOUND(1001),
    INVALID_TEMPLATE_FORMAT(1002),
    PROJECT_CREATION_FAILED(1003),
    GIT_INITIALIZATION_FAILED(1004),
    INVALID_PROJECT_TYPE(1005),
    INVALID_PROJECT_PATH(1006),
    PATH_ALREADY_EXISTS(1007),
    INVALID_PROJECT_NAME(1008);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
