--liquidbase formated sql

-- changeset rvoroshnin:1
CREATE TABLE notification_task (
    id SERIAL PRIMARY KEY,
    chat_id SERIAL,
    message_text TEXT,
    notification_date TIMESTAMP WITH TIME ZONE
)