package net.creeperhost.chickens.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Pure-Java pedigree validation. No NeoForge classes: safe for ordinary JUnit.
 */
public final class ChickenPedigrees {
    public record Parents(String first, String second) {
        public boolean hasParents() { return !first.isEmpty() && !second.isEmpty(); }
    }

    private ChickenPedigrees() {}

    public static Map<String, Parents> validate(Map<String, ChickenSpecies> originals,
            Map<String, Parents> proposed) {
        for (var entry : proposed.entrySet()) {
            String child = entry.getKey();
            Parents parents = entry.getValue();
            if (!originals.containsKey(child) || parents == null
                    || (parents.first().isEmpty() != parents.second().isEmpty())) {
                throw new IllegalArgumentException("Invalid parent pair for " + child);
            }
            if (parents.hasParents() && (!originals.containsKey(parents.first())
                    || !originals.containsKey(parents.second()))) {
                throw new IllegalArgumentException("Unknown parent for " + child);
            }
        }
        Map<String, Parents> effective = new HashMap<>();
        for (var entry : originals.entrySet()) {
            ChickenSpecies type = entry.getValue();
            effective.put(entry.getKey(), proposed.getOrDefault(entry.getKey(),
                    new Parents(type.parentA(), type.parentB())));
        }
        Set<String> done = new HashSet<>();
        for (String id : effective.keySet()) {
            visit(id, effective, new HashSet<>(), done);
        }
        return Map.copyOf(proposed);
    }

    private static void visit(String id, Map<String, Parents> graph,
            Set<String> visiting, Set<String> done) {
        if (done.contains(id)) return;
        if (!visiting.add(id)) throw new IllegalArgumentException("Cyclic breeding pedigree at " + id);
        Parents parents = graph.get(id);
        if (parents.hasParents()) {
            visit(parents.first(), graph, visiting, done);
            visit(parents.second(), graph, visiting, done);
        }
        visiting.remove(id);
        done.add(id);
    }
}
