CREATE TABLE IF NOT EXISTS user_events (
    id UUID DEFAULT generateUUIDv4(),
    user_id String,
    event_type LowCardinality(String),
    event_timestamp DateTime64(3),
    payload String,
    created_at DateTime64(3) DEFAULT now64(3)
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(event_timestamp)
ORDER BY (event_type, user_id, event_timestamp);
