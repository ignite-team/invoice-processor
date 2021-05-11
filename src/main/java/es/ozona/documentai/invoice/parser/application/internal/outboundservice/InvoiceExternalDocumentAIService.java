package es.ozona.documentai.invoice.parser.application.internal.outboundservice;

import java.util.Map;

public interface InvoiceExternalDocumentAIService {

	public Map<String, String> extractEntities(String documentPath) throws Exception;
}
