package es.ozona.documentai.invoice.parser.application.internal.outboundservice;

import java.util.Map;

public interface InvoiceDocumentAIService {
	
	public Map<String, String> extractEntities(String documentPath);
}
