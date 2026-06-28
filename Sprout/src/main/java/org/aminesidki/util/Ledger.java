package org.aminesidki.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * Keeps ahold of actions that should happen once throughout the entire pipeline.
 */
public class Ledger {
    private final Map<String, Boolean> context = new HashMap<>();

    public Boolean findRecord(String condition){
        return Optional.of(context.get(condition)).orElse(false);
    }

    public void addRecord(String condition, boolean val){
        context.put(condition, val);
    }
}
