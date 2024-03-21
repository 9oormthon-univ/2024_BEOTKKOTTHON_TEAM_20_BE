package com.beotkkot.qtudy.common;

public interface ResponseMessage {
    // HTTP Status 200
    String SUCCESS = "Success.";

    // HTTP Status 400
    String VALIDATION_FAILED = "Validation Failed.";
    String NOT_EXISTED_USER = "This user does not exist.";
    String NOT_EXISTED_POST = "This post does not exist.";
    String NOT_EXISTED_COMMENT = "This comment does not exist.";
    String INVALID_FORMAT = "Invalid Format";

    // HTTP Status 401
    String SIGN_IN_FAIL = "Login information mismatch.";
    String AUTHORIZATION_FAIL = "Authorization Failed.";

    // HTTP Status 403
    String NO_PERMISSION = "Do not have permission.";

    // HTTP Status 500
    String DATABASE_ERROR = "Database error.";
}
