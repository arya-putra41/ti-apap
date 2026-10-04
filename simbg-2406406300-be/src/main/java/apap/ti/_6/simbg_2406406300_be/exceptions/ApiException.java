package apap.ti._6.simbg_2406406300_be.exceptions;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
    protected HttpStatus status;

    protected ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
