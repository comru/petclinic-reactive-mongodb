package ru.amplicode.rp.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;

@ControllerAdvice
public class ProblemDetailsExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    @NonNull
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(@NonNull WebExchangeBindException ex,
                                                                          @NonNull HttpHeaders headers,
                                                                          @NonNull HttpStatusCode status,
                                                                          ServerWebExchange exchange) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }

        ProblemDetail problemDetail = ex.updateAndGetBody(getMessageSource(), getLocale(exchange));
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        var fieldErrorModels = new ArrayList<FieldErrorModel>();

        for (ObjectError error : allErrors) {
            if (error instanceof FieldError fieldError) {
                fieldErrorModels.add(new FieldErrorModel(
                        fieldError.getCode(),
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getObjectName()
                ));
            }
        }

        fieldErrorModels.sort(Comparator.comparing(fieldErrorModel -> fieldErrorModel.field));
        problemDetail.setProperties(new LinkedHashMap<>(Map.of("fieldErrors", fieldErrorModels)));

        return createResponseEntity(problemDetail, headers, status, exchange);
    }


    private static Locale getLocale(ServerWebExchange exchange) {
        Locale locale = exchange.getLocaleContext().getLocale();
        return (locale != null ? locale : Locale.getDefault());
    }

    private record FieldErrorModel(String code,
                                   String field,
                                   String message,
                                   String objectName) {
    }
}
