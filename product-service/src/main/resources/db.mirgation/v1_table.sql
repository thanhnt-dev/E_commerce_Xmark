CREATE TABLE "categories"
(
    "id"            BIGSERIAL PRIMARY KEY NOT NULL,
    "category_name" varchar               NOT NULL,
    "description"   text,
    "parent_id"     BIGINT,
    "is_active"     boolean               NOT NULL DEFAULT true,
    "version"       int                   NOT NULL DEFAULT 1,
    "created_at"    bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"    bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "sub_categories"
(
    "id"                BIGSERIAL PRIMARY KEY NOT NULL,
    "sub_category_name" varchar               NOT NULL,
    "description"       text,
    "category_id"       bigint                NOT NULL,
    "is_active"         boolean               NOT NULL DEFAULT true,
    "version"           int                   NOT NULL DEFAULT 1,
    "created_at"        bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"        bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "brands"
(
    "id"          BIGSERIAL PRIMARY KEY NOT NULL,
    "brand_name"  varchar(100)          NOT NULL,
    "logo_url"    text,
    "description" text,
    "is_active"   boolean               NOT NULL DEFAULT true,
    "version"     int                   NOT NULL DEFAULT 1,
    "created_at"  bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"  bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "products"
(
    "id"                      BIGSERIAL PRIMARY KEY NOT NULL,
    "product_sku"             varchar(15) UNIQUE    NOT NULL,
    "product_name"            varchar(255)          NOT NULL,
    "description"             text,
    "view_count"              bigint                NOT NULL DEFAULT 0,
    "origin"                  varchar               NOT NULL,
    "product_details"         json,
    "sale_status"             varchar,
    "sub_category_id"         bigint,
    "brand_id"                bigint,
    "shop_id"                 bigint                NOT NULL,
    "product_approval_status" varchar,
    "product_approval_by"     bigint,
    "reason_reject"           text,
    "is_active"               boolean               NOT NULL DEFAULT true,
    "version"                 int                   NOT NULL DEFAULT 1,
    "created_at"              bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"              bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "product_variants"
(
    "id"             BIGSERIAL PRIMARY KEY NOT NULL,
    "product_id"     bigint                NOT NULL,
    "product_size"   varchar,
    "quantity"       int                   NOT NULL,
    "original_price" bigint                NOT NULL,
    "resale_price"   bigint                NOT NULL,
    "condition"      varchar               NOT NULL,
    "is_active"      boolean               NOT NULL DEFAULT true,
    "version"        int                   NOT NULL DEFAULT 1,
    "created_at"     bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"     bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "product_assets"
(
    "id"         BIGSERIAL PRIMARY KEY NOT NULL,
    "media_key"  varchar               NOT NULL,
    "public_id"  varchar               NOT NULL,
    "product_id" bigint                NOT NULL,
    "is_active"  boolean               NOT NULL DEFAULT true,
    "version"    int                   NOT NULL DEFAULT 1,
    "created_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);