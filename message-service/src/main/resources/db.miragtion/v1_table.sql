CREATE TABLE "messages"
(
    "id"            BIGSERIAL PRIMARY KEY NOT NULL,
    "sender_id"     bigint                NOT NULL,
    "sender_type"   varchar(50)           NOT NULL,
    "receiver_id"   bigint                NOT NULL,
    "receiver_type" varchar(50)           NOT NULL,
    "content"       text,
    "room_id"       varchar(50)           NOT NULL,
    "asset_id"      varchar(255),
    "secret_url"    varchar(255),
    "is_read"       boolean               NOT NULL DEFAULT false,
    "is_active"     boolean               NOT NULL DEFAULT true,
    "version"       int                   NOT NULL DEFAULT 1,
    "created_at"    bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"    bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "notifications"
(
    "id"          BIGSERIAL PRIMARY KEY NOT NULL,
    "sender_id"   bigint                NOT NULL,
    "sender_name" varchar(255),
    "user_id" bigint                NOT NULL,
    "content"     text,
    "is_read"     boolean               NOT NULL DEFAULT false,
    "notification_type" varchar(50) NOT NULL,
    "is_active"   boolean               NOT NULL DEFAULT true,
    "version"     int                   NOT NULL DEFAULT 1,
    "created_at"  bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"  bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);
