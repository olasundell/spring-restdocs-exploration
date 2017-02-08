package se.atrosys.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.atrosys.model.CharAt;
import se.atrosys.service.AlphabetService;

/**
 * TODO write documentation
 */
@RestController
@RequestMapping("/alphabet")
public class AlphabetResource {
	private final AlphabetService service;

	@Autowired
	public AlphabetResource(AlphabetService service) {
		this.service = service;
	}

	@RequestMapping("/charAt")
	public CharAt charAt(@RequestParam Integer pos) {
		return service.getCharAt(pos);
	}
}
