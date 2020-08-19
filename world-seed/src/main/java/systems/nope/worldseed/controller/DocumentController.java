package systems.nope.worldseed.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.worldseed.model.Document;
import systems.nope.worldseed.repository.DocumentRepository;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentRepository documentRepository;

    public DocumentController(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @GetMapping
    public List<Document> all() {
        return documentRepository.findAll();
    }
}
