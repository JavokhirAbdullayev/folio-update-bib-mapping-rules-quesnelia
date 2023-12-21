package org.folio.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MappingRulesUtil {

    public static void replaceMappingRulesForMarcFields(ObjectNode rulesReplacement, ObjectNode targetMappingRules) {
        ObjectMapper mapper = new ObjectMapper();
        rulesReplacement.fields().forEachRemaining(marcFieldRule -> {
            JsonNode marcRulesNode = targetMappingRules.get(marcFieldRule.getKey());
            if (marcRulesNode == null || !marcRulesNode.isArray() || marcRulesNode.size() == 0) {
                log.warn("No rules found for MARC field \"{}\"", marcFieldRule.getKey());
                return;
            }

            ArrayNode entities = (ArrayNode) marcRulesNode.get(0).get("entity");
            if (entities == null) {
                log.warn("No entities found for MARC field \"{}\"", marcFieldRule.getKey());
                return;
            }

            // Find the target node and its index
            for (int i = 0; i < entities.size(); i++) {
                ObjectNode entity = (ObjectNode) entities.get(i);
                if (entity.get("target").asText().equals("contributors.name")) {
                    entities.remove(i);
                    entities.insert(0, entity);
                    addRequiredSubfield(mapper, entity);
                    break;
                }
            }

            ObjectNode entityWrapper = mapper.createObjectNode();
            entityWrapper.set("entity", entities);
            ArrayNode entityArray = mapper.createArrayNode().add(entityWrapper);
            targetMappingRules.set(marcFieldRule.getKey(), entityArray);
            log.info("Mapping rules for MARC field \"{}\" have been updated", marcFieldRule.getKey());
        });
    }

    private static void addRequiredSubfield(ObjectMapper mapper, ObjectNode targetEntity) {
        ArrayNode requiredSubfields = mapper.createArrayNode().add("a");
        targetEntity.set("requiredSubfield", requiredSubfields);
    }
}
