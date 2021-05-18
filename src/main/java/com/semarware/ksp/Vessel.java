package com.semarware.ksp;

public class Vessel {
    private final String name;
    private final String vesselId;
    private final String type;

    public Vessel(String name, String vesselId, String type) {
        this.name = name;
        this.vesselId = vesselId;
        this.type = type;
    }


    public static Vessel fromElement(KSPDocumentElement c) {
        final String name = c.getValue("name").orElseThrow().getValue();
        final String vesselId = c.getValue("persistentId").orElseThrow().getValue();
        final String type = c.getValue("type").orElseThrow().getValue();
        return new Vessel(name, vesselId, type);
    }

    public String getName() {
        return name;
    }

    public String getVesselId() {
        return vesselId;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Vessel{" +
                "name='" + name + '\'' +
                ", vesselId='" + vesselId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
