-- 1. Eliminamos por completo las dos columnas viejas
ALTER TABLE "promotion" DROP COLUMN IF EXISTS "discount_type";
ALTER TABLE "promotion" DROP COLUMN IF EXISTS "visibility";

-- 2. Creamos la columna visibility de cero con las reglas correctas
ALTER TABLE "promotion" ADD COLUMN "visibility" VARCHAR(10) NOT NULL DEFAULT 'GLOBAL'
    CHECK ("visibility" IN ('GLOBAL', 'PRIVATE'));