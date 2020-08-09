package systems.nope.worldseed.document;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.world.World;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document update(Document documentOld, String contentNew) {
        Document documentNew = new Document(
                contentNew,
                documentOld.getWorld(),
                documentOld.getId().getId(),
                documentOld.getId().getVersion() + 1
        );

        documentRepository.save(documentNew);

        return documentNew;
    }

    public Document add(World world, String richtext) {
        return add(world, richtext, 1);
    }

    public Document add(World world, String richtext, int version) {
        Integer lastId = documentRepository.lastId();

        if (lastId == null)
            lastId = 1;
        else
            lastId = lastId + 1;

        Document document = new Document(
                richtext,
                world,
                lastId,
                version
        );
        documentRepository.save(document);

        return document;
    }
}
