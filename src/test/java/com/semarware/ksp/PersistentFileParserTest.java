package com.semarware.ksp;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class PersistentFileParserTest {

    @Test
    public void parser() {
        final PersistentFileParser persistentFileParser = new PersistentFileParser("persistent.bak.sfs");
        final KSPFileDocument document = persistentFileParser.parse();
        Assertions.assertEquals(document.getElement().getName(), "GAME");
    }

}