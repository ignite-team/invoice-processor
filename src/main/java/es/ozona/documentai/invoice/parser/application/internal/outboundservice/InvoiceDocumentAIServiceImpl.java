package es.ozona.documentai.invoice.parser.application.internal.outboundservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceDocumentAIServiceImpl implements InvoiceDocumentAIService {

	private static final Logger LOG = LoggerFactory.getLogger(InvoiceDocumentAIServiceImpl.class);

	@Autowired
	ExternalDocumentAIService service;

	@Autowired
	Map<String, String> entityMap;

	@Override
	public Map<String, String> extractEntities(String documentPath) {

		final Map<String, String> filteredEntities = new HashMap<String, String>();

		try {

			final Map<String, String> extractedEntities = service.extractEntities(documentPath);

			for (String alias : entityMap.keySet()) {
				for (String entity : extractedEntities.keySet()) {
					if (entity.toLowerCase().trim().matches(alias)) {
						filteredEntities.put(entityMap.get(alias), extractedEntities.get(entity).replace(entity, "").trim());
					}
				}
			}

		} catch (Exception e) {
			LOG.error("No se ha podido procesar el documento, se devuelve la lista de entidades vacia", e);
		}
		return filteredEntities;
	}

}
