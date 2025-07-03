CREATE TABLE "users"
(
    "id"                   BIGSERIAL PRIMARY KEY NOT NULL,
    "email"                varchar(255) UNIQUE   NOT NULL,
    "password"             varchar(255),
    "first_name"           varchar(20)           NOT NULL,
    "last_name"            varchar(30)           NOT NULL,
    "gender"               varchar(10),
    "user_provider"        varchar(10),
    "avatar"               text,
    "public_id"            text,
    "phone"                varchar(20) UNIQUE,
    "date_of_birth"        bigint                NOT NULL,
    "is_first_login"       boolean                        DEFAULT true,
    "is_enable_two_factor" boolean                        DEFAULT false,
    "point_balance"        int                   NOT NULL DEFAULT 0,
    "is_active"            boolean               NOT NULL DEFAULT true,
    "version"              int                   NOT NULL DEFAULT 1,
    "created_at"           bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"           bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "roles"
(
    "id"         BIGSERIAL PRIMARY KEY NOT NULL,
    "role_name"  varchar(100)          NOT NULL,
    "is_active"  boolean               NOT NULL DEFAULT true,
    "version"    int                   NOT NULL DEFAULT 1,
    "created_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "user_roles"
(
    "id"         BIGSERIAL PRIMARY KEY NOT NULL,
    "user_id"    bigint                NOT NULL,
    "role_id"    bigint                NOT NULL,
    "is_active"  boolean               NOT NULL DEFAULT true,
    "version"    int                   NOT NULL DEFAULT 1,
    "created_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "refresh_tokens"
(
    "id"            BIGSERIAL PRIMARY KEY NOT NULL,
    "user_id"       bigint                NOT NULL,
    "refresh_token" text                  NOT NULL,
    "is_active"     boolean               NOT NULL DEFAULT true,
    "version"       int                   NOT NULL DEFAULT 1,
    "created_at"    bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"    bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "address"
(
    "id"            BIGSERIAL PRIMARY KEY NOT NULL,
    "name"          varchar(40)           NOT NULL,
    "phone"         varchar(10)           NOT NULL,
    "type"          varchar(20)           NOT NULL,
    "detail"        varchar(100),
    "province_name" varchar,
    "district_name" varchar,
    "ward_name"     varchar,
    "user_id"       bigint                NOT NULL,
    "is_default"    boolean               NOT NULL DEFAULT false,
    "is_active"     boolean               NOT NULL DEFAULT true,
    "version"       int                   NOT NULL DEFAULT 1,
    "created_at"    bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"    bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "point_transactions"
(
    "id"          BIGSERIAL PRIMARY KEY NOT NULL,
    "user_id"     bigint                NOT NULL,
    "order_id"    bigint                NOT NULL,
    "points"      int                   NOT NULL,
    "type"        varchar(20)           NOT NULL,
    "description" text,
    "is_active"   boolean               NOT NULL DEFAULT true,
    "version"     int                   NOT NULL DEFAULT 1,
    "created_at"  bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"  bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

INSERT INTO roles (role_name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER'),
       ('ROLE_STORE_OWNER');