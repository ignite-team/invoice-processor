package es.ozona.documentai.invoice.parser.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.documentai.v1beta3.DocumentProcessorServiceClient;
import com.google.cloud.documentai.v1beta3.DocumentProcessorServiceSettings;

@Configuration
public class ApplicationConfig {

	@Value("${documentai.googleapis.endpoint}")
	public String apiEndPoing;

	@Bean
	public DocumentProcessorServiceClient getDocumentProcessorServiceClient() throws IOException {
		return DocumentProcessorServiceClient
				.create(DocumentProcessorServiceSettings.newBuilder().setEndpoint(apiEndPoing).build());
	}

	@Bean
	public List<Entity> getEntities() throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper mapper = new ObjectMapper();

		final InputStreamReader input = new InputStreamReader(
				ApplicationConfig.class.getClassLoader().getResourceAsStream("entities.json"));

		final List<Entity> entities = mapper.readValue(input, new TypeReference<List<Entity>>() {
		});

		return entities;
	}
	
	@Bean
	public Map<String, String> getEntityMap(List<Entity> entities) {
		Map<String,String> entityMap = new HashMap<String, String>();
		for (Entity entity: entities) {
			
			for (String alias: entity.getAlias().split(",")) {
				entityMap.put(alias, entity.getName());
			}
			
		}
		return entityMap;
	}
}
