package com.semarware.ksp;

public class KSPFileDocument {
    private final KSPDocumentElement element;

    public KSPFileDocument(KSPDocumentElement element) {
        this.element = element;
    }

    public KSPDocumentElement getElement() {
        return element;
    }

    @Override
    public String toString() {
        return "KSPFileDocument{" +
                "element=" + element +
                '}';
    }
}
