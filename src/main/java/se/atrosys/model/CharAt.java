package se.atrosys.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * TODO write documentation
 */
@Data
@Builder
@AllArgsConstructor
public class CharAt {
	private char lowerCase;
	private char upperCase;
}
