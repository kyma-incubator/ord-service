# Querying Over Fields of Type Array of Strings

Open Resource Discovery Service (ORD Service) provides all query capabilities that are supported by the OData V4 standard.

Additionally, ORD Service provides a custom implementation that enables querying over arrays of strings.

## ArrayElement Object

To enable filtering in arrays of strings, it is itroduced an intermediary entity called `ArrayElement`.

The following snippet is part of the edmx document that describes the ORD Service API and shows how the `ArrayElement` object is described in it:
```xml
<EntityType Name="api">
  ...
  <Property Name="tags" Type="Collection(com.sap.cloud.cmp.ord.service.ArrayElement)"/>
  ...
</EntityType>
<ComplexType Name="ArrayElement">
  <Property Name="value" Type="Edm.String" MaxLength="2147483647"/>
</ComplexType>
```
The following excerpt shows the same `ArrayElement` object description in JSON:
```json
"api": {
  "$Kind": "EntityType",
  ...
  "tags": {
    "$Type": "com.sap.cloud.cmp.ord.service.ArrayElement",
    "$Collection": true
  }
  ...
},
"ArrayElement": {
  "$Kind": "ComplexType",
  "value": {
    "$Type": "Edm.String",
    "$MaxLength": 2147483647
   }
}
```

As a result, each array of strings is modelled internally as shown in the following excerpt:
```json
"tags": [
  {
    "value": "tag1"
  },
  {
    "value": "tag2"
  }
]
```
And not like:
```json
"tags": [
  "tag1",
  "tag2"
]
```

> NOTE: You can control the returned output by providing the parameter `compact=true` as a query parameter. Then, the response is formatted as a normal JSON array. However, internally it will be handled as an array of `ArrayElement` objects and the query should be structured accordingly.

## Query Syntax

Technically, the query is based on the `ArrayElement` object and the query syntax is the regular OData V4 query syntax for arrays of objects. For example, the following query command returns all APIs that contain the tag "nck":

```
/open-resource-discovery-service/v0/apis?$filter=tags/any(d:d/value eq 'nck')
```
