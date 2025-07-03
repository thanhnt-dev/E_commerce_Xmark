ALTER TABLE "shop_request" ADD FOREIGN KEY ("shop_id") REFERENCES "shops" ("id");

ALTER TABLE "shop_info_assets" ADD FOREIGN KEY ("shop_id") REFERENCES "shops" ("id");

ALTER TABLE "shop_products" ADD FOREIGN KEY ("shop_id") REFERENCES "shops" ("id");

ALTER TABLE "shop_followers" ADD FOREIGN KEY ("shop_id") REFERENCES "shops" ("id");

ALTER TABLE "shop_subscriptions" ADD FOREIGN KEY ("shop_id") REFERENCES "shops" ("id");

ALTER TABLE "shop_subscriptions" ADD FOREIGN KEY ("subscription_id") REFERENCES "subscription_plans" ("id");

// docker run --name storeservice -e POSTGRES_USER=storeservice -e POSTGRES_PASSWORD=12345 -e POSTGRES_DB=storeservice -p 5433:5432 -d postgres
