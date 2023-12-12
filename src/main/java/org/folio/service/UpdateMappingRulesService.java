package org.folio.service;

import static org.folio.FolioMappingRulesUpdateApp.exitWithMessage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.folio.client.AuthClient;
import org.folio.client.SRMClient;
import org.folio.model.Configuration;
import org.folio.util.FileWorker;
import org.folio.util.HttpWorker;
import org.folio.util.MappingRulesUtil;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateMappingRulesService {

    private static final String QUESNELIA_MAPPING_RULES_PATH = "mappingRules/quesneliaMappingRules/";

    private Configuration configuration;
    private SRMClient srmClient;
    private String MARC_BIB = "marc-bib";

    public void start() {
        configuration = FileWorker.getConfiguration();
        var httpWorker = new HttpWorker(configuration);
        var authClient = new AuthClient(configuration, httpWorker);
        
        httpWorker.setOkapiToken(authClient.authorize());
        srmClient = new SRMClient(httpWorker);

        updateMappingRules();

        exitWithMessage("Script execution completed");
    }

    private void updateMappingRules() {
        JsonNode existingMappingRules = srmClient.retrieveMappingRules(MARC_BIB);

        ObjectNode quesneliaClassificationRules = FileWorker.getJsonObject(QUESNELIA_MAPPING_RULES_PATH + "fieldMappingRules.json");

        if (!quesneliaClassificationRules.isEmpty()) {
            MappingRulesUtil.replaceMappingRulesForMarcFields(quesneliaClassificationRules, (ObjectNode) existingMappingRules);
            srmClient.updateMappingRules(existingMappingRules, MARC_BIB);
            log.info("Mapping rules for \"classification\" field have been successfully updated on the target environment for the following MARC fields: {}",
                extractMarcFields(quesneliaClassificationRules));
        }
    }

    private static List<String> extractMarcFields(ObjectNode preparedPoppyClassificationRules) {
        List<String> fields = new ArrayList<>();
        preparedPoppyClassificationRules.fieldNames().forEachRemaining(fields::add);
        return fields;
    }

}
