# Elasticsearch Indices Provisioning (Examples)

Indices for content-service and analytics-service. Apply via ES API or operator.

## Suggested indices
- content-service
  - posts-v1 (shards: 1, replicas: 1)
  - media-v1 (1,1)
- analytics-service
  - analytics-metrics-v1 (1,1)

## Sample index template (posts)
```json
PUT _index_template/posts-template
{
  "index_patterns": ["posts-*"],
  "template": {
    "settings": {"number_of_shards": 1, "number_of_replicas": 1},
    "mappings": {
      "properties": {
        "userId": {"type": "keyword"},
        "content": {"type": "text"},
        "tags": {"type": "keyword"},
        "createdAt": {"type": "date"}
      }
    }
  }
}
```

## Create index
```json
PUT posts-v1
```

