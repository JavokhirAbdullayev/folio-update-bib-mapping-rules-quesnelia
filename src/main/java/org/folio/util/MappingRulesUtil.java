package org.folio.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MappingRulesUtil {

    public static void replaceMappingRulesForMarcFields(ObjectNode rulesReplacement, ObjectNode targetMappingRules) {
        rulesReplacement.fields().forEachRemaining(marcFieldRule -> {
            targetMappingRules.set(marcFieldRule.getKey(), marcFieldRule.getValue());
            log.info("Mapping rules for MARC field \"{}\" have been updated", marcFieldRule.getKey());
        });
    }
}
