package com.semarware.ksp;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PersistentFileParser {

    //    private final List<String> lines;
    private final AtomicInteger currentLine = new AtomicInteger(0);
    private final AtomicInteger depth = new AtomicInteger(0);
    private final List<String> buf = new ArrayList<>();
    private final List<String> recent = new ArrayList<>();
    private final AtomicReference<String> lastRead = new AtomicReference<>();
    private final List<KSPDocumentElement> contractElements = new ArrayList<>();
    private final List<KSPDocumentElement> vesselElements = new ArrayList<>();

    private static final String ELEMENT_NAME_PATTERN_STR = "^[a-zA-Z0-9_/]+$";
    private static final Pattern ELEMENT_NAME_PATTERN = Pattern.compile(ELEMENT_NAME_PATTERN_STR);

    private static final String VALUE_LINE_PATTERN_STR = "^\\s*(?<key>[a-zA-Z0-9_]+)\\s*=\\s*(?<value>.*)$";
    private static final Pattern VALUE_LINE_PATTERN = Pattern.compile(VALUE_LINE_PATTERN_STR);

    private static final String OPEN_TAG = "{";
    private static final String CLOSE_TAG = "}";
    private final Scanner scanner;
//    private final ObjectMapper objectMapper;

    public PersistentFileParser(String path) {
//        this.objectMapper = new ObjectMapper();

        try {
            this.scanner = new Scanner(new File(path));
//            this.lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to open file", e);
        }
    }

    public KSPFileDocument parse() {
//        new KSPFileDocument()
        final KSPDocumentElement root = readElement();
//        System.out.printf("Read in doc: %s%n", root.getName());
        return new KSPFileDocument(root);
    }

    private KSPDocumentElement readElement() {
        final String elementName = parseElementName();

        final KSPDocumentElement element = new KSPDocumentElement(elementName);
        readOpenTag();
        while (!isCloseTag()) {

            if (isElementStart()) {
//                System.out.printf("Found element start: %s\tDescending one level deeper. cur depth: %s%n", lastRead.get(), depth.getAndAdd(1));

                final KSPDocumentElement childElement = readElement();
                depth.decrementAndGet();
                element.addChild(childElement);
            } else {
                final KSPDocumentValue value = readValue();
                element.addValue(value);
            }
        }
        if (element.getName().equals("CONTRACT")) {

            final Optional<KSPDocumentValue> state = element.getValue("state");
            if (state.isPresent() && state.get().getValue().equals("Active")) {
//                System.out.printf("read contract: %s\t%s%n", state, toJsonString(element));
                contractElements.add(element);
            }
        }
        if (element.getName().equals("VESSEL")) {
            final List<KSPDocumentElement> values = element.getChildren();
            values.clear();
            final Optional<KSPDocumentValue> name = element.getValue("name");
//            if (name.isPresent() && name.get().getValue().equals("Aging Duna CSS-X Satellite K1J-K9")) {
//                System.out.printf("read vessel: %s\t%s%n", element.getValue("name"),
//                        toJsonString(element));
//            }
            this.vesselElements.add(element);
        }
        return element;

    }


    private KSPDocumentValue readValue() {
        final String s = nextLine().strip();
        if (s.isBlank()) {
            throw new IllegalArgumentException(String.format("failed to parse current line as key,value. Line num: %s, contents: '%s'",
                    currentLine.get(), s));
        }
        final Matcher matcher = VALUE_LINE_PATTERN.matcher(s);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("failed to parse current line as key = value. Line num: %s, contents: '%s'",
                    currentLine.get(), s));
        }
        final String key = matcher.group("key");
        final String value = matcher.group("value").strip();
        if (key.isBlank()) {
            throw new IllegalArgumentException(String.format("Failed to parse line as key = value. Key='%s', value='%s'. Line: %s",
                    key, value, s));
        }
        return new KSPDocumentValue(key, value);
    }

    private boolean isCloseTag() {
        final String s = nextLine();
        final boolean result = s.strip().equals(CLOSE_TAG);
        if (!result) {
            putBack(s);
        }

        return result;
    }

    private void readOpenTag() {
        final String s = nextLine().strip();
        if (s.isBlank()) {
            throw new IllegalArgumentException(String.format("failed to parse current line as open tag. contents: '%s'",
                    s));
        }
        if (!s.equals(OPEN_TAG)) {
            throw new IllegalArgumentException(String.format("failed to parse current line as open tag. contents: '%s'",
                    s));
        }
    }

    private String parseElementName() {
        final String s = nextLine().strip();
        if (s.isBlank()) {
            throw new IllegalArgumentException(String.format("failed to parse current line as element name. Line num: %s, contents: '%s'",
                    currentLine.get(), s));
        }
        final Matcher matcher = ELEMENT_NAME_PATTERN.matcher(s);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(String.format("failed to parse current line as element name. Line num: %s, line: %s%n recent: '%s'",
                    currentLine.get(), s, String.join("\n", recent)));
        }
        currentLine.getAndAdd(1);
        return s;
    }

    private boolean isElementStart() {
        final String s = nextLine().strip();
        if (s.isBlank()) {
            putBack(s);
            return false;
        }
        final Matcher matcher = ELEMENT_NAME_PATTERN.matcher(s);
        final boolean result = matcher.matches();
        putBack(s);
        return result;
    }

    private String nextLine() {

        final String retVal;
        if (!buf.isEmpty()) {
            retVal = buf.remove(buf.size() - 1);
        } else {
            retVal = scanner.nextLine();
            this.currentLine.incrementAndGet();
        }
        while (recent.size() > 5) {
            recent.remove(0);
        }
        if (!retVal.equals(lastRead.get())) {
            recent.add(retVal);
            lastRead.set(retVal);
        }
        return retVal;
    }

    private void putBack(String line) {
        buf.add(line);
    }


    private String toJsonString(KSPDocumentElement element) {
        return "NA";
//        try {
//            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(element);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
    }

    public List<KSPDocumentElement> getVesselElements() {
        return vesselElements;
    }

    public List<KSPDocumentElement> getContractElements() {
        return contractElements;
    }
}