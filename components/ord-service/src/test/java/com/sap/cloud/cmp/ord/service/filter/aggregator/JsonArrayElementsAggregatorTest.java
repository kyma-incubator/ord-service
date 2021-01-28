package com.sap.cloud.cmp.ord.service.filter.aggregator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonArrayElementsAggregatorTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testAggregate_ReturnsUnmodifiedJson_WhenArrayElementNotFound() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#apis\",\"value\":[{\"ordId\":\"test-id\"}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#apis\",\"value\":[{\"ordId\":\"test-id\"}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsUnmodifiedJson_WhenArrayElementFoundButNotKnown() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#apis\",\"value\":[{\"unknownArray\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#apis\",\"value\":[{\"unknownArray\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenArrayElementFound() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#apis\",\"value\":[{\"tags\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#apis\",\"value\":[{\"tags\":[\"automotive\",\"finance\"]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenLabelsFound() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#apis\",\"value\":[{\"labels\":[{\"key\":\"country\",\"value\":\"DE\"},{\"key\":\"country\",\"value\":\"US\"}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#apis\",\"value\":[{\"labels\":{\"country\":[\"DE\",\"US\"]}}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenLabelsAndArrayElementFound() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#apis\",\"value\":[{\"tags\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}],\"labels\":[{\"key\":\"country\",\"value\":\"DE\"},{\"key\":\"country\",\"value\":\"US\"}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#apis\",\"value\":[{\"tags\":[\"automotive\",\"finance\"],\"labels\":{\"country\":[\"DE\",\"US\"]}}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsUnmodifiedJson_WhenArrayElementNotFoundInParentAndChildResource() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#packages(apis(id))\",\"value\":[{\"id\":\"99cced1d-7bbf-41e3-8368-527d686af033\",\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\"}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#packages(apis(id))\",\"value\":[{\"id\":\"99cced1d-7bbf-41e3-8368-527d686af033\",\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\"}]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenArrayElementFoundInParentResourceOnly() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#packages(apis(id))\",\"value\":[{\"tags\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}],\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\"}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#packages(apis(id))\",\"value\":[{\"tags\":[\"automotive\",\"finance\"],\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\"}]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenLabelsElementFoundInParentResourceOnly() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#packages(apis(id))\",\"value\":[{\"labels\":[{\"key\":\"country\",\"value\":\"DE\"},{\"key\":\"country\",\"value\":\"US\"}],\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\"}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#packages(apis(id))\",\"value\":[{\"labels\":{\"country\":[\"DE\",\"US\"]},\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\"}]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenArrayAndLabelsElementFoundInParentResourceOnly() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#packages(apis(id))\",\"value\":[{\"tags\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}],\"labels\":[{\"key\":\"country\",\"value\":\"DE\"},{\"key\":\"country\",\"value\":\"US\"}],\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\"}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#packages(apis(id))\",\"value\":[{\"tags\":[\"automotive\",\"finance\"],\"labels\":{\"country\":[\"DE\",\"US\"]},\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\"}]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenArrayElementFoundInNestedResourceOnly() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#packages(apis(id,tags))\",\"value\":[{\"ordId\":\"test-id\",\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"tags\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}]}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#packages(apis(id,tags))\",\"value\":[{\"ordId\":\"test-id\",\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"tags\":[\"automotive\",\"finance\"]}]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenLabelsElementFoundInNestedResourceOnly() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#packages(apis(id,labels))\",\"value\":[{\"ordId\":\"test-id\",\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"labels\":[{\"key\":\"country\",\"value\":\"DE\"},{\"key\":\"country\",\"value\":\"US\"}]}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#packages(apis(id,labels))\",\"value\":[{\"ordId\":\"test-id\",\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"labels\":{\"country\":[\"DE\",\"US\"]}}]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenArrayAndLabelsElementFoundInNestedResourceOnly() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#packages(apis(id,tags,labels))\",\"value\":[{\"ordId\":\"test-id\",\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"tags\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}],\"labels\":[{\"key\":\"country\",\"value\":\"DE\"},{\"key\":\"country\",\"value\":\"US\"}]}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#packages(apis(id,tags,labels))\",\"value\":[{\"ordId\":\"test-id\",\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"tags\":[\"automotive\",\"finance\"],\"labels\":{\"country\":[\"DE\",\"US\"]}}]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    // =======

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenArrayElementFoundInBothParentAndNestedResourceOnly() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#packages(apis(id,tags))\",\"value\":[{\"tags\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}],\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"tags\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}]}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#packages(apis(id,tags))\",\"value\":[{\"tags\":[\"automotive\",\"finance\"],\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"tags\":[\"automotive\",\"finance\"]}]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenLabelsElementFoundInBothParentAndNestedResourceOnly() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#packages(apis(id,labels))\",\"value\":[{\"labels\":[{\"key\":\"country\",\"value\":\"DE\"},{\"key\":\"country\",\"value\":\"US\"}],\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"labels\":[{\"key\":\"country\",\"value\":\"DE\"},{\"key\":\"country\",\"value\":\"US\"}]}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#packages(apis(id,labels))\",\"value\":[{\"labels\":{\"country\":[\"DE\",\"US\"]},\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"labels\":{\"country\":[\"DE\",\"US\"]}}]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenArrayAndLabelsElementFoundInBothParentAndNestedResource() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#packages(apis(id,tags,labels))\",\"value\":[{\"tags\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}],\"labels\":[{\"key\":\"country\",\"value\":\"DE\"},{\"key\":\"country\",\"value\":\"US\"}],\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"tags\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}],\"labels\":[{\"key\":\"country\",\"value\":\"DE\"},{\"key\":\"country\",\"value\":\"US\"}]}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#packages(apis(id,tags,labels))\",\"value\":[{\"tags\":[\"automotive\",\"finance\"],\"labels\":{\"country\":[\"DE\",\"US\"]},\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"tags\":[\"automotive\",\"finance\"],\"labels\":{\"country\":[\"DE\",\"US\"]}}]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenArrayElementFoundInParentResourceAndLabelsElementFoundInNestedResource() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#packages(apis(id,labels))\",\"value\":[{\"tags\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}],\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"labels\":[{\"key\":\"country\",\"value\":\"DE\"},{\"key\":\"country\",\"value\":\"US\"}]}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#packages(apis(id,labels))\",\"value\":[{\"tags\":[\"automotive\",\"finance\"],\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"labels\":{\"country\":[\"DE\",\"US\"]}}]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testAggregate_ReturnsModifiedJson_WhenLabelsElementFoundInParentResourceAndArrayElementFoundInNestedResource() throws Exception {
        JsonArrayElementsAggregator aggregator = new JsonArrayElementsAggregator(mapper);

        String content = "{\"@odata.context\":\"$metadata#packages(apis(id,tags))\",\"value\":[{\"labels\":[{\"key\":\"country\",\"value\":\"DE\"},{\"key\":\"country\",\"value\":\"US\"}],\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"tags\":[{\"value\":\"automotive\"},{\"value\":\"finance\"}]}]}]}";
        String expectedContent = "{\"@odata.context\":\"$metadata#packages(apis(id,tags))\",\"value\":[{\"labels\":{\"country\":[\"DE\",\"US\"]},\"apis\":[{\"@odata.id\":\"apis(23834341-071f-4f5b-bb02-ea4971d8b719)\",\"id\":\"23834341-071f-4f5b-bb02-ea4971d8b719\",\"tags\":[\"automotive\",\"finance\"]}]}]}";

        JsonNode jsonTree = mapper.readTree(content);
        aggregator.aggregate(jsonTree);
        String actualContent = mapper.writeValueAsString(jsonTree);

        assertEquals(expectedContent, actualContent);
    }
}
