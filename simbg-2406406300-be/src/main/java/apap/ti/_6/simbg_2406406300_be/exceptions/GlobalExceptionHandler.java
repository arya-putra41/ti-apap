package apap.ti._6.simbg_2406406300_be.exceptions;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import apap.ti._6.simbg_2406406300_be.httpresponses.BaseResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // Kelas untuk menangani exception umum
        @ExceptionHandler(ApiException.class)
        public ResponseEntity<BaseResponse<Void>> handleApiException(ApiException exception) {
                return ResponseEntity.status(exception.getStatus())
                                .body(BaseResponse.<Void>builder()
                                                .code(exception.getStatus())
                                                .timestamp(ZonedDateTime.now(ZoneId.of("UTC+07:00")))
                                                .message(exception.getMessage())
                                                .data(null)
                                                .build());
        }

        // Kelas untuk menangani argument invalid (digunakan misalnya saat ada field form yang kosong) (400)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<BaseResponse<Void>> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException exception) {
                String message = exception.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> error.getDefaultMessage())
                                .collect(Collectors.joining(";"));

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(BaseResponse.badRequest(message));

        }

        // Kelas untuk menangani conflict
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<BaseResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(BaseResponse.conflict(exception.getMessage()));
        }

        // Kelas untuk menangani exception lainnya (500)
        @ExceptionHandler(Exception.class)
        public ResponseEntity<BaseResponse<Void>> handleUnhandledException(Exception exception) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(BaseResponse.unhandled(exception.getMessage()));
        }
}
