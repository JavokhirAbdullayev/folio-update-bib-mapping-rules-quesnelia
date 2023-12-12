package org.folio.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

public class MappingRulesUtilTest {

    @Test
    public void shouldReplaceSpecifiedMappingRules() {
        ObjectNode rulesReplacement = FileWorker.getJsonObject("rules/quesneliaUpdateFieldMappingRules.json");
        ObjectNode mappingRulesWithCustomizations = FileWorker.getJsonObject(
          "rules/rules_with_differences_in_100_110_111.json");
        ObjectNode expectedUpdatedRules = FileWorker.getJsonObject("rules/expectedUpdatedMappingRules.json");

        MappingRulesUtil.replaceMappingRulesForMarcFields(rulesReplacement, mappingRulesWithCustomizations);
        Assert.assertEquals(expectedUpdatedRules, mappingRulesWithCustomizations);
    }
}