package com.semarware.ksp;

public class Contract {
    private final String name;
    private final String constructionVesselId;
    private final String vesselName;

    public Contract(String name, String constructionVesselId, String vesselName) {
        this.name = name;
        this.constructionVesselId = constructionVesselId;
        this.vesselName = vesselName;
    }

    public static Contract fromElement(KSPDocumentElement c) {
        final String value = c.getValue("constructionVslId").orElseThrow().getValue();
        final String vesselName = c.getValue("vesselName").orElseThrow().getValue();
        final String name = "Construction contract for " + vesselName;
        return new Contract(name, value, vesselName);
    }

    @Override
    public String toString() {
        return "Contract{" +
                "name='" + name + '\'' +
                ", constructionVesselId='" + constructionVesselId + '\'' +
                ", vesselName='" + vesselName + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getConstructionVesselId() {
        return constructionVesselId;
    }

    public String getVesselName() {
        return vesselName;
    }
}
