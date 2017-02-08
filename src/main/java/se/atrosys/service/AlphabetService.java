package se.atrosys.service;

import javaslang.collection.List;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import se.atrosys.model.CharAt;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * TODO write documentation
 */
@Validated
@Service
public class AlphabetService {
	private List<Character> chars;
	@PostConstruct
	public void initChars() {
		if (chars == null) {
			chars = List.range('a', 'z');
		}
	}

	public CharAt getCharAt(@Valid @Max(23) @Min(1) Integer pos) {
		return CharAt.builder()
				.lowerCase(chars.get(pos - 1))
				.upperCase(Character.toUpperCase(chars.get(pos - 1)))
				.build();
	}
}
