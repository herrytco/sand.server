package systems.nope.worldseed.model;

public class LastDocument {
    private Integer documentId;
    private Integer version;

    public LastDocument(Integer documentId, Integer version) {
        this.documentId = documentId;
        this.version = version;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public Integer getVersion() {
        return version;
    }
}
