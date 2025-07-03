ALTER TABLE "user_roles"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "user_roles"
    ADD FOREIGN KEY ("role_id") REFERENCES "roles" ("id");

ALTER TABLE "refresh_tokens"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "address"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");

ALTER TABLE "point_transactions"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");