package es.ozona.documentai.invoice.parser.application.internal.outboundservice;

import java.util.Map;

public interface ExternalDocumentAIService {

	public Map<String, String> extractEntities(String documentPath) throws Exception;

}
