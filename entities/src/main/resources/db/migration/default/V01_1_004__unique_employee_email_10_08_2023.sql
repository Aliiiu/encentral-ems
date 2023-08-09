-- author: DEMILADE
-- description: Script to add unique constraint to the employee table on the employee_email column
-- date: 08/08/2023

ALTER TABLE public.employee
ADD CONSTRAINT employee_employee_email_uq UNIQUE (employee_email);