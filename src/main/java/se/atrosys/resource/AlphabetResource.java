package se.atrosys.resource;

import javaslang.collection.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.atrosys.model.CharAt;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * TODO write documentation
 */
@RestController
@RequestMapping("/alphabet")
public class AlphabetResource {
	private List<Character> chars;

	@RequestMapping("/charAt")
	public CharAt charAt(@RequestParam @Validated @Max(23) @Min(1) int pos) {
		if (chars == null) {
			chars = List.range('a', 'z');
		}

		return CharAt.builder()
				.lowerCase(chars.get(pos - 1))
				.upperCase(Character.toUpperCase(chars.get(pos - 1)))
				.build();

	}
}
