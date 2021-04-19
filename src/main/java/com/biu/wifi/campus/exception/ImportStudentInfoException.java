/**
 *
 */
package com.biu.wifi.campus.exception;

public class ImportStudentInfoException extends RuntimeException {

    private static final long serialVersionUID = 5237549537950011490L;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ImportStudentInfoException(String message) {
        super(message);
        this.message = message;
    }
}
