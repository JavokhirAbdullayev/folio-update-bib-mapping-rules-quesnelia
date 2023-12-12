package org.folio.util;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MappingRulesUtil {

    public static void replaceMappingRulesForMarcFields(ObjectNode rulesReplacement, ObjectNode targetMappingRules) {
        rulesReplacement.fields().forEachRemaining(marcFieldRule -> {
            targetMappingRules.set(marcFieldRule.getKey(), marcFieldRule.getValue());
            log.info("Mapping rules for MARC field \"{}\" have been updated", marcFieldRule.getKey());
        });
    }

    public static ObjectNode getMappingRulesExcludingByMarcFields(ObjectNode sourceMappingRules, Set<String> marcFieldsToFilter) {
        ObjectNode filteredMappingRules = JsonNodeFactory.instance.objectNode();
        sourceMappingRules.fields().forEachRemaining(marcFieldRule -> {
            if (!marcFieldsToFilter.contains(marcFieldRule.getKey())) {
                filteredMappingRules.set(marcFieldRule.getKey(), marcFieldRule.getValue());
            }
        });
        return filteredMappingRules;
    }

}
