package se.atrosys.resource;

import javaslang.collection.List;
import javaslang.collection.Stream;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import se.atrosys.model.ErrorMessage;

import javax.validation.ConstraintViolationException;

/**
 * TODO write documentation
 */
@RestControllerAdvice
public class ErrorResource {
	@RequestMapping(produces = "application/json")
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody List<ErrorMessage> handleConstraintViolationException(ConstraintViolationException ex) {
		return Stream.ofAll(ex.getConstraintViolations())
				.map(cv -> ErrorMessage.builder()
						.field(cv.getPropertyPath().toString().replaceFirst(".*?[.]",""))
						.code(cv.getMessageTemplate())
						.message(cv.getMessage())
						.build())
				.toList();
	}

}
