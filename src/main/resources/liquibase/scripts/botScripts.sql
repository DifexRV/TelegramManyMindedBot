--liquidbase formated sql

-- changeset rvoroshnin:1
CREATE TABLE notification_task (
    id SERIAL PRIMARY KEY,
    chat_id BIGINT,
    message_text TEXT,
    notification_date TIMESTAMP WITH TIME ZONE
)

-- changeset rvoroshnin:2
ALTER TABLE notification_task DROP COLUMN date_time;
