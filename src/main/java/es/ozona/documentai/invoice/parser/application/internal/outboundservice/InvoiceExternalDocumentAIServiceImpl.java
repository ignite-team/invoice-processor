package es.ozona.documentai.invoice.parser.application.internal.outboundservice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.cloud.documentai.v1beta3.Document;
import com.google.cloud.documentai.v1beta3.DocumentProcessorServiceClient;
import com.google.cloud.documentai.v1beta3.ProcessRequest;
import com.google.cloud.documentai.v1beta3.ProcessResponse;
import com.google.protobuf.ByteString;

@Service
public class InvoiceExternalDocumentAIServiceImpl implements InvoiceExternalDocumentAIService {

	private static final Logger LOG = LoggerFactory.getLogger(InvoiceExternalDocumentAIServiceImpl.class);

	private static final String PROCESSOR_NAME_PATTERN = "projects/%s/locations/%s/processors/%s";
	private static final String PDF_MIME_TYPE = "application/pdf";

	@Value("${documentai.googleapis.endpoint}")
	private String apiEndPoing;

	@Value("${documentai.processor-secondary.location}")
	private String location;

	@Value("${documentai.processor-secondary.projectid}")
	private String projectId;

	@Value("${documentai.processor-secondary.processorid}")
	public String processorId;

	@Autowired
	DocumentProcessorServiceClient client;

	@SuppressWarnings("deprecation")
	@Override
	public Map<String, String> extractEntities(String documentPath) throws Exception {
		final Map<String, String> entities = new HashMap<String, String>();

		try {
			// Peticion
			ProcessRequest request = ProcessRequest.newBuilder().setName(getProcessorName())
					.setDocument(buildDocument(documentPath)).build();

			// Extraccion de las entidades
			ProcessResponse result = client.processDocument(request);
			Document documentResponse = result.getDocument();

			Document.Page firstPage = documentResponse.getPages(0);
			String text = documentResponse.getText();
			for (Document.Page.FormField field : firstPage.getFormFieldsList()) {
				String fieldName = getText(field.getFieldName().getTextAnchor(), text);
				String fieldValue = getText(field.getFieldValue().getTextAnchor(), text);

				 System.out.println("Extracted form fields pair:");
				 System.out.printf("\t(%s, %s))\n", fieldName, fieldValue);

				entities.put(fieldName, fieldValue);
			}

		} catch (Exception e) {
			if (client != null) {
				client.close();
			}
			throw e;
		} finally {
			if (client != null) {
				client.close();
			}
		}

		return entities;
	}

	private String getProcessorName() {
		return String.format(PROCESSOR_NAME_PATTERN, projectId, location, processorId);
	}

	private Document buildDocument(String documentPath) throws IOException {
		byte[] imageFileData = Files.readAllBytes(Paths.get(documentPath));
		ByteString content = ByteString.copyFrom(imageFileData);
		return Document.newBuilder().setContent(content).setMimeType(PDF_MIME_TYPE).build();
	}

	private static String getText(Document.TextAnchor textAnchor, String text) {
		if (textAnchor.getTextSegmentsList().size() > 0) {
			int startIdx = (int) textAnchor.getTextSegments(0).getStartIndex();
			int endIdx = (int) textAnchor.getTextSegments(0).getEndIndex();
			return text.substring(startIdx, endIdx);
		}
		return "[NO TEXT]";
	}

}
