package apap.ti._6.simbg_2406406300_be.httpresponses;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    
    private HttpStatusCode code;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime timestamp;

    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .code(HttpStatus.OK)
                .message("Success")
                .timestamp(ZonedDateTime.now(ZoneId.of("UTC+07:00")))
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> created(T data) {
        return BaseResponse.<T>builder()
                .code(HttpStatus.CREATED)
                .message("Success")
                .timestamp(ZonedDateTime.now(ZoneId.of("UTC+07:00")))
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> badRequest(String message) {
        return BaseResponse.<T>builder()
                .code(HttpStatus.BAD_REQUEST)
                .message(message)
                .timestamp(ZonedDateTime.now(ZoneId.of("UTC+07:00")))
                .data(null)
                .build();
    }

    public static <T> BaseResponse<T> notFound(String message) {
        return BaseResponse.<T>builder()
                .code(HttpStatus.NOT_FOUND)
                .message(message)
                .timestamp(ZonedDateTime.now(ZoneId.of("UTC+07:00")))
                .data(null)
                .build();
    }

    public static <T> BaseResponse<T> conflict(String message) {
        return BaseResponse.<T>builder()
                .code(HttpStatus.CONFLICT)
                .message(message)
                .timestamp(ZonedDateTime.now(ZoneId.of("UTC+07:00")))
                .data(null)
                .build();
    }

    public static <T> BaseResponse<T> unhandled(String message) {
        return BaseResponse.<T>builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(message)
                .timestamp(ZonedDateTime.now(ZoneId.of("UTC+07:00")))
                .data(null)
                .build();
    }
}
