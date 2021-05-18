package com.semarware.ksp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Fixer {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.printf("Usage: fixer filename.sfs%n");
            System.exit(1);
        }
        final Path path = Paths.get(args[0]);
        if (!Files.exists(path)) {
            System.out.printf("File not found %s%n", args[0]);
            System.exit(1);
        }
        final PersistentFileParser persistentFileParser = new PersistentFileParser(args[0].strip());
        persistentFileParser.parse();
        final List<Vessel> vessels = persistentFileParser.getVesselElements().stream()
                .filter(c -> c.getName().equalsIgnoreCase("VESSEL"))
                .map(Vessel::fromElement)
                .collect(Collectors.toList());
        final List<Contract> contracts = persistentFileParser.getContractElements().stream()
                .filter(c -> c.getName().equalsIgnoreCase("CONTRACT"))
                .filter(c -> c.getValue("state").isPresent())
                .filter(c -> c.getValue("constructionVslId").isPresent())
//                .filter(c -> c.getChildren().stream().anyMatch(child -> child.getValue("name").map(dv -> dv.getValue().equalsIgnoreCase("ConstructionParameter")).isPresent()))
                .map(Contract::fromElement)
                .collect(Collectors.toList());
        if (contracts.isEmpty()) {
            System.out.printf("No active contracts found%n");
            System.exit(1);
        }
        final List<Replacement> replacements = new ArrayList<>();
        for (Contract c : contracts) {
            System.out.printf("for contract: %s need to replace vessel id with vessel id of name: %s%n", c, c.getVesselName());
            final List<Vessel> matchingVessels = vessels.stream()
                    .filter(v -> v.getName().equalsIgnoreCase(c.getVesselName()))
                    .collect(Collectors.toList());
            if (matchingVessels.stream().anyMatch(v -> v.getVesselId().equalsIgnoreCase(c.getConstructionVesselId()))) {
                System.out.printf("Looks like this contract is already referencing valid vessel id: %s %n", matchingVessels);
                continue;
            }
            if (matchingVessels.size() != 1) {
                System.out.printf("Can't fix this contract. Found %s matching vessels by name: %s\t%s%n", matchingVessels.size(),
                        c.getVesselName(), c);
                continue;
            }
            final Vessel vessel = matchingVessels.get(0);
            System.out.printf("Need to replace value: %s with %s in persistent file %n", c.getConstructionVesselId(), vessel.getVesselId());
            replacements.add(new Replacement(c.getConstructionVesselId(), vessel.getVesselId()));
        }
        System.out.printf("Found %s replacements to do%n", replacements.size());
        if (replacements.size() > 0) {
            final PersistentFileReplacer replacer = new PersistentFileReplacer(args[0].strip(), replacements);
            replacer.fix();
        }
    }
}
