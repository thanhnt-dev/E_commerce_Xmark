ALTER TABLE "order_items"
    ADD FOREIGN KEY ("order_id") REFERENCES "orders" ("id");

ALTER TABLE "user_vouchers"
    ADD FOREIGN KEY ("voucher_id") REFERENCES "vouchers" ("id");

ALTER TABLE "order_vouchers"
    ADD FOREIGN KEY ("order_id") REFERENCES "orders" ("id");

ALTER TABLE "order_vouchers"
    ADD FOREIGN KEY ("voucher_id") REFERENCES "vouchers" ("id");