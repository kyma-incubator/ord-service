# Querying over fields of type array of strings

ORD Service provides all of the rich querying capabilities that are supported by OData V4 standard.

On top of that, ORD Service provides a custom implementation for querying over arrays of strings.

## ArrayElement object

In order to achieve filtering on array of strings, we itroduce an intermediary entity - `ArrayElement`.

Let's see how this looks like in the edmx document:
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
And in JSON:
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

This way each array of strings is modelled internally like:
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

> NOTE: You can control the returned output by providing the `compact=true` as a query parameter. Then the response will look like a normal json array, however internally it will be again array of ArrayElement objects and the querying should be structured accordingly.

## Query syntax

Qyerying is based on the ArrayElement object and the query syntax is normal ODataV4 query syntax for array of object. For example:

```
/open-resource-discovery-service/v0/apis?$filter=tags/any(d:d/value eq 'tag1') - Give me APIs that have tag "tag1"
```
