package com.beotkkot.qtudy.common;

public interface ResponseCode {
    // HTTP Status 200
    String SUCCESS = "SU";

    // HTTP Status 400
    String VALIDATION_FAILED = "VF";
    String NOT_EXISTED_USER = "NEU";
    String NOT_EXISTED_POST = "NEP";
    String NOT_EXISTED_COMMENT = "NEC";
    String INVALID_FORMAT = "IF";

    // HTTP Status 401
    String SIGN_IN_FAIL = "SF";
    String AUTHORIZATION_FAIL = "AF";

    // HTTP Status 403
    String NO_PERMISSION = "NP";

    // HTTP Status 500
    String DATABASE_ERROR = "DBE";
}
