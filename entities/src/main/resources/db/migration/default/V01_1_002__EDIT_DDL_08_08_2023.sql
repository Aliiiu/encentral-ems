-- author: DEMILADE
-- description: Script to add an end_date column to the leave_request_table
-- date: 08/08/2023

ALTER TABLE public.leave_request
ADD COLUMN end_date DATE;

UPDATE public.leave_request
SET end_date = start_date + (duration * INTERVAL '1 DAY');

ALTER TABLE public.leave_request
ALTER duration DROP NOT NULL;

ALTER TABLE public.employee
ALTER leave_days SET DEFAULT 0;
