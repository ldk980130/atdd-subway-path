package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class SubwayException extends RuntimeException {

    private final HttpStatus httpStatus;

    public SubwayException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
