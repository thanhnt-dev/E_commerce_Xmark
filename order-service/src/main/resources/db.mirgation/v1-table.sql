CREATE TABLE "orders"
(
    "id"             BIGSERIAL PRIMARY KEY NOT NULL,
    "order_code"     varchar(40) UNIQUE    NOT NULL,
    "user_id"        bigint                NOT NULL,
    "shop_id"        bigint                NOT NULL,
    "phone"          varchar(10),
    "name"           varchar(50),
    "address"        varchar(100),
    "delivery_id"    bigint                ,
    "note"           text,
    "payment_type"   varchar(20)           NOT NULL,
    "total_price"    decimal(10, 2),
    "shipping_fee"   decimal(10, 2)        NOT NULL DEFAULT 0,
    "discount_total" decimal(10, 2)        NOT NULL DEFAULT 0,
    "total_amount"   decimal(10, 2)        NOT NULL,
    "status"         varchar(15)           NOT NULL,
    "is_active"      boolean               NOT NULL DEFAULT true,
    "version"        int                   NOT NULL DEFAULT 1,
    "created_at"     bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"     bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "order_items"
(
    "id"         BIGSERIAL PRIMARY KEY NOT NULL,
    "order_id"   bigint                NOT NULL,
    "product_variant_id" bigint                NOT NULL,
    "product_variant_name" varchar(100) ,
    "quantity"   int                   NOT NULL,
    "price_unit" decimal(10, 2)        NOT NULL,
    "is_active"  boolean               NOT NULL DEFAULT true,
    "version"    int                   NOT NULL DEFAULT 1,
    "created_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "vouchers"
(
    "id"              BIGSERIAL PRIMARY KEY NOT NULL,
    "code"            varchar(20) UNIQUE    NOT NULL,
    "discount_type"   varchar(20)           NOT NULL,
    "discount_value"  int                   NOT NULL,
    "min_order_value" int                   NOT NULL DEFAULT 0,
    "max_discount"    int,
    "start_date"      bigint                NOT NULL,
    "end_date"        bigint                NOT NULL,
    "usage_limit"     int                   NOT NULL DEFAULT 1,
    "is_active"       boolean               NOT NULL DEFAULT true,
    "version"         int                   NOT NULL DEFAULT 1,
    "created_at"      bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"      bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "user_vouchers"
(
    "id"         BIGSERIAL PRIMARY KEY NOT NULL,
    "user_id"    bigint                NOT NULL,
    "voucher_id" bigint                NOT NULL,
    "is_used"    boolean               NOT NULL DEFAULT false,
    "used_at"    bigint,
    "is_active"  boolean               NOT NULL DEFAULT true,
    "version"    int                   NOT NULL DEFAULT 1,
    "created_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at" bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

CREATE TABLE "order_vouchers"
(
    "id"              BIGSERIAL PRIMARY KEY NOT NULL,
    "order_id"        bigint                NOT NULL,
    "voucher_id"      bigint                NOT NULL,
    "discount_amount" int                   NOT NULL,
    "is_active"       boolean               NOT NULL DEFAULT true,
    "version"         int                   NOT NULL DEFAULT 1,
    "created_at"      bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric),
    "updated_at"      bigint                NOT NULL DEFAULT (EXTRACT(epoch FROM now()) * 1000::numeric)
);

