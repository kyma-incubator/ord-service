package com.sap.cloud.cmp.ord.service.filter.aggregator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

public class JsonArrayElementsAggregator {

    private static final String[] subresources = new String[]{"value" /*This is the key returned when you list objects*/, "products", "packages", "consumptionBundles", "apis", "events", "capabilities", "vendors", "systemInstances"};
    private static final String[] arrays = new String[]{"tags", "countries", "lineOfBusiness", "industry", "entryPoints", "successors", "correlationIds", "partners"};
    private static final String[] labels = new String[]{"labels", "documentationLabels"};
    private static final String[] embeddedResources = new String[]{"extensible"};

    private ObjectMapper mapper;

    public JsonArrayElementsAggregator() {
        this.mapper = new ObjectMapper();
    }

    public JsonArrayElementsAggregator(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void aggregate(JsonNode jsonTree) {
        aggregateArrayElements(jsonTree);
    }

    private void aggregateArrayElements(JsonNode jsonTree) {
        if (jsonTree != null) {
            if (jsonTree.isArray()) {
                Iterator<JsonNode> it = jsonTree.elements();
                while (it.hasNext()) {
                    JsonNode el = it.next();
                    processResource(el);
                    for (String subresource : subresources) {
                        aggregateArrayElements(el.get(subresource));
                    }
                }
            } else {
                processResource(jsonTree);
                for (String subresource : subresources) {
                    aggregateArrayElements(jsonTree.get(subresource));
                }
            }
        }
    }

    private void processResource(JsonNode resource) {
        for (String arrayName : arrays) {
            JsonNode array = resource.get(arrayName);
            if (array != null && array.isArray()) {
                JsonNode convertedArray = convertArray(array);
                ((ObjectNode) resource).replace(arrayName, convertedArray);
            }
        }

        for (String labelResource : labels) {
            JsonNode labels = resource.get(labelResource);
            if (labels != null && labels.isArray()) {
                JsonNode convertedLabels = convertLabels(labels);
                ((ObjectNode) resource).replace(labelResource, convertedLabels);
            }
        }

        for (String embeddedResource : embeddedResources) {
            JsonNode extensible = resource.get(embeddedResource);
            if (extensible != null && extensible.isObject()) {
                Boolean isEmpty = true;
                Iterator<JsonNode> it = extensible.elements();
                while (it.hasNext()) {
                    JsonNode el = it.next();
                    if (!el.isNull()) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    ((ObjectNode) resource).replace(embeddedResource, null);
                }
            }
        }
    }

    private JsonNode convertArray(JsonNode array) {
        if (array == null || !array.isArray()) {
            return array;
        }
        List<String> result = new ArrayList<>();
        Iterator<JsonNode> it = array.elements();
        while (it.hasNext()) {
            JsonNode el = it.next();
            JsonNode value = el.get("value");
            if (value != null) {
                result.add(value.textValue());
            }
        }
        return mapper.valueToTree(result);
    }

    private JsonNode convertLabels(JsonNode labels) {
        if (labels == null || !labels.isArray()) {
            return labels;
        }
        Map<String, List<String>> result = new HashMap<>();
        Iterator<JsonNode> it = labels.elements();
        while (it.hasNext()) {
            JsonNode el = it.next();
            JsonNode key = el.get("key");
            JsonNode value = el.get("value");
            if (key != null && value != null) {
                List<String> label = result.get(key.textValue());
                if (label == null) {
                    label = new ArrayList<>();
                }
                label.add(value.textValue());
                result.put(key.textValue(), label);
            }
        }
        return mapper.valueToTree(result);
    }
}
