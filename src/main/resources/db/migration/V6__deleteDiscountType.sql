
ALTER TABLE "promotion" DROP COLUMN IF EXISTS "discount_type";
ALTER TABLE "promotion" DROP COLUMN IF EXISTS "visibility";

ALTER TABLE "promotion" ADD COLUMN "visibility" VARCHAR(10) NOT NULL DEFAULT 'GLOBAL'
    CHECK ("visibility" IN ('GLOBAL', 'PRIVATE'));