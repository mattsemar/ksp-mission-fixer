package com.semarware.ksp;

import java.util.*;

public class KSPDocumentElement {
    private final String name;
    private final List<KSPDocumentValue> values = new ArrayList<>();
    private final Map<String, Integer> valueLookup = new HashMap<>();
    private final List<KSPDocumentElement> children = new ArrayList<>();

    public KSPDocumentElement(String name) {
        this.name = name;
    }

    public void addValue(KSPDocumentValue value) {
        this.values.add(value);
//        if (this.valueLookup.containsKey(value.getKey())) {
//            System.out.printf("losing lookup index for key: %s%n", value.getKey());
//        }
        this.valueLookup.put(value.getKey(), this.values.size() - 1);
    }

    public void addChild(KSPDocumentElement child) {
        this.children.add(child);
    }

    @Override
    public String toString() {
        return "KSPDocumentElement{" +
                "name='" + name + '\'' +
                ", values=" + values +
                ", children=" + children +
                '}';
    }

    public String getName() {
        return name;
    }

    public Optional<KSPDocumentValue> getValue(String name) {
        final Integer index = this.valueLookup.get(name);
        if (index == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.values.get(index));
    }

    public List<KSPDocumentElement> getChildren() {
        return children;
    }

    public List<KSPDocumentValue> getValues() {
        return values;
    }
}
