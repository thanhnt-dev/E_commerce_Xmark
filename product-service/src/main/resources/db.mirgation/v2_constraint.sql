ALTER TABLE "Sub_categories"
    ADD FOREIGN KEY ("category_id") REFERENCES "Categories" ("id");

ALTER TABLE "Products"
    ADD FOREIGN KEY ("sub_category_id") REFERENCES "Sub_categories" ("id");

ALTER TABLE "Products"
    ADD FOREIGN KEY ("brand_id") REFERENCES "Brands" ("id");

ALTER TABLE "Product_Variants"
    ADD FOREIGN KEY ("product_id") REFERENCES "Products" ("id");

ALTER TABLE "Product_assets"
    ADD FOREIGN KEY ("product_id") REFERENCES "Products" ("id");