CREATE TABLE "shops"
(
    "id"                  BIGSERIAL PRIMARY KEY NOT NULL,
    "name"                varchar(50)           NOT NULL,
    "description"         text,
    "avatar"              text,
    "public_id"           text,
    "location"            varchar,
    "verification_status" varchar,
    "identity_number"     varchar               NOT NULL,
    "owner_name"          varchar(50)           NOT NULL,
    "email"               varchar(50) UNIQUE    NOT NULL,
    "phone"               varchar(10) UNIQUE    NOT NULL,
    "tax_code"            varchar(20),
    "business_type"       varchar               NOT NULL,
    "category"            bigint,
    "owner_id"            bigint UNIQUE         NOT NULL,
    "is_active"           boolean               NOT NULL DEFAULT true,
    "version"             int                   NOT NULL DEFAULT 1,
    "created_at"          bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"          bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "shop_requests"
(
    "id"         BIGSERIAL PRIMARY KEY NOT NULL,
    "shop_id"    bigint                NOT NULL,
    "admin_id"   bigint                NOT NULL,
    "reason"     text,
    "is_active"  boolean               NOT NULL DEFAULT true,
    "version"    int                   NOT NULL DEFAULT 1,
    "created_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);
CREATE TABLE "shop_info_assets"
(
    "id"         BIGSERIAL PRIMARY KEY NOT NULL,
    "media_key"  text                  NOT NULL,
    "public_id"  text                  NOT NULL,
    "shop_id"    bigint                NOT NULL,
    "is_active"  boolean               NOT NULL DEFAULT true,
    "version"    int                   NOT NULL DEFAULT 1,
    "created_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);
CREATE TABLE "shop_products"
(
    "id"         BIGSERIAL PRIMARY KEY NOT NULL,
    "product_id" bigint                NOT NULL,
    "shop_id"    bigint,
    "is_active"  boolean               NOT NULL DEFAULT true,
    "version"    int                   NOT NULL DEFAULT 1,
    "created_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);
CREATE TABLE "shop_followers"
(
    "id"         BIGSERIAL PRIMARY KEY NOT NULL,
    "shop_id"    bigint                NOT NULL,
    "user_id"    bigint                NOT NULL,
    "is_active"  boolean               NOT NULL DEFAULT true,
    "version"    int                   NOT NULL DEFAULT 1,
    "created_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);
CREATE TABLE "subscription_plans"
(
    "id"             BIGSERIAL PRIMARY KEY NOT NULL,
    "name"           varchar(50)           NOT NULL,
    "description"    text,
    "price"          bigint                NOT NULL,
    "duration_days"  int                   NOT NULL,
    "priority_level" int                   NOT NULL DEFAULT 0,
    "is_active"      boolean               NOT NULL DEFAULT true,
    "version"        int                   NOT NULL DEFAULT 1,
    "created_at"     bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"     bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);
CREATE TABLE "shop_subscriptions"
(
    "id"              BIGSERIAL PRIMARY KEY NOT NULL,
    "shop_id"         bigint                NOT NULL,
    "subscription_id" bigint                NOT NULL,
    "expired_At"      bigint                NOT NULL,
    "status"          varchar(20)           NOT NULL DEFAULT 'active',
    "is_active"       boolean               NOT NULL DEFAULT true,
    "version"         int                   NOT NULL DEFAULT 1,
    "created_at"      bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"      bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);