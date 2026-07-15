-- 1. Eliminar la restricción actual de payment_method
ALTER TABLE public.sale_operation DROP CONSTRAINT sale_operation_payment_method_check;

-- 2. Agregar la nueva restricción con los valores correctos
-- Asegúrate de incluir todos los que necesitas (minúsculas o mayúsculas según tu preferencia)
ALTER TABLE public.sale_operation
    ADD CONSTRAINT sale_operation_payment_method_check
        CHECK (payment_method IN ('efectivo', 'yape_plin', 'YAPE_PLIN', 'TARJETA'));

-- 3. Si también quieres ser más flexible con los estados de pago:
ALTER TABLE public.sale_operation DROP CONSTRAINT sale_operation_payment_status_check;

ALTER TABLE public.sale_operation
    ADD CONSTRAINT sale_operation_payment_status_check
        CHECK (payment_status IN ('pendiente', 'pagado', 'anulado', 'PAGADO', 'PENDIENTE'));