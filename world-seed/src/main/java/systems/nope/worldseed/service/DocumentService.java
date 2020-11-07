package systems.nope.worldseed.service;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.model.Document;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.repository.DocumentRepository;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document update(Document documentOld, String contentNew) {
        Document documentNew = new Document(
                documentOld.getI(),
                documentOld.getVersion() + 1,
                contentNew,
                documentOld.getWorld()
                );

        documentRepository.save(documentNew);

        return documentNew;
    }

    public Document add(World world, String richtext) {
        return add(world, richtext, 1);
    }

    public Document add(World world, String richtext, int version) {
        Integer lastI = documentRepository.lastId();

        if (lastI == null)
            lastI = 1;

        Document document = new Document(
                lastI + 1,
                version,
                richtext,
                world
        );
        documentRepository.save(document);

        return document;
    }
}
