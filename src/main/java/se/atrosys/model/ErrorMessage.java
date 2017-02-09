package se.atrosys.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * TODO write documentation
 */
@Builder
@Data
@AllArgsConstructor
public class ErrorMessage {
	private String field;
	private String code;
	private String message;
}
