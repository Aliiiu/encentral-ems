-- author: WARITH
-- description: Changing the emergency_contact relationship to one-to-one in order to conform with the UI design
-- date: 14/08/2013

ALTER TABLE public.emergency_contact
DROP COLUMN last_name;

ALTER TABLE public.emergency_contact
RENAME COLUMN first_name TO full_name;

ALTER TABLE public.emergency_contact
ALTER COLUMN address DROP NOT NULL,
ALTER COLUMN email DROP NOT NULL;

ALTER TABLE public.emergency_contact
ADD CONSTRAINT emergency_contact_unique_employee_fk
UNIQUE (employee_id);
