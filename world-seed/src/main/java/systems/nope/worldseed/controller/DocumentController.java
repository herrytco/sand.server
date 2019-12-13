package systems.nope.worldseed.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.nope.worldseed.model.Document;
import systems.nope.worldseed.model.LastDocument;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.repository.DocumentRepository;
import systems.nope.worldseed.repository.WorldRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    public DocumentController(DocumentRepository documentRepository, WorldRepository worldRepository) {
        this.documentRepository = documentRepository;
        this.worldRepository = worldRepository;
    }

    private final DocumentRepository documentRepository;
    private final WorldRepository worldRepository;

    @GetMapping("/world/{worldId}")
    public ResponseEntity<?> allForWorld(
            @PathVariable int worldId
    ) {
        World targetWorld;

        try {
            targetWorld = worldRepository.getOne(worldId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("World with ID '%d' not found.", worldId));
        }

        List<LastDocument> docData = documentRepository.getLatestForWorld(targetWorld);

        return ResponseEntity.ok(
                docData.stream().map(
                        lastDocument -> documentRepository.getOne(
                                new Document.Pk(
                                        lastDocument.getDocumentId(),
                                        lastDocument.getVersion()
                                )
                        )
                )
        );
    }

    @GetMapping("/world/{worldId}/id/{id}")
    public ResponseEntity<?> one(
            @PathVariable int worldId,
            @PathVariable int id
    ) {
        World targetWorld;

        try {
            targetWorld = worldRepository.getOne(worldId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("World with ID '%d' not found.", worldId));
        }

        try {
            LastDocument docData = documentRepository.getLatest(id, targetWorld);

            return ResponseEntity.ok(
                    documentRepository.getOne(
                            new Document.Pk(docData.getDocumentId(), docData.getVersion())
                    )
            );
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public List<Document> all() {
        return documentRepository.findAll();
    }
}
