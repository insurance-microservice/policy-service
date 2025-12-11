package test.avows.policy.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import test.avows.policy.common.ApiResponse;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setDetail(ex.getError());

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        return handleApiException(new ApiException(
                400,
                "Data integrity violation",
                ex.getMostSpecificCause().getMessage()
        ));
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiResponse> handleTransaction(TransactionSystemException ex) {
        Throwable rootCause = ex.getRootCause();

        return handleApiException(new ApiException(
                400,
                "Transaction error",
                rootCause != null ? rootCause.getMessage() : ex.getMessage()
        ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleValidation(ConstraintViolationException ex) {
        return handleApiException(new ApiException(
                400,
                "Validation failed",
                ex.getMessage()
        ));
    }
}
