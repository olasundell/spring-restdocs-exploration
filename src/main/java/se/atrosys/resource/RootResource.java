package se.atrosys.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.atrosys.model.Response;

/**
 * TODO write documentation
 */
@RestController
public class RootResource {
	@RequestMapping(path = "/")
	public Response get() {
		return Response.builder().value("foo").build();
	}
}
