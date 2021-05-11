package es.ozona.documentai.invoice.parser.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.ozona.documentai.invoice.parser.application.internal.outboundservice.DocumentAIService;

@Controller
@RequestMapping("/api/v1")
public class DocumentController {
	private static final Logger LOG = LoggerFactory.getLogger(DocumentController.class);

	@Autowired
	DocumentAIService service;

	@GetMapping(path = "/documents/parse")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@ResponseBody
	public ResponseEntity<?> parse(@RequestParam(required = true) String path) {
		LOG.info(path);
		return ResponseEntity.ok(service.extractEntities(path));
	}

}
