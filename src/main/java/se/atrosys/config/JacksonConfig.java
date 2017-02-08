package se.atrosys.config;

import javaslang.jackson.datatype.JavaslangModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * TODO write documentation
 */
@Configuration
public class JacksonConfig implements Jackson2ObjectMapperBuilderCustomizer {

	@Override
	public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
		jacksonObjectMapperBuilder.modulesToInstall(new JavaslangModule());
	}
}
