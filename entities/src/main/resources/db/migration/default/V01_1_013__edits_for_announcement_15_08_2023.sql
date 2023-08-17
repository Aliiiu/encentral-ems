-- author: WARITH
-- description: Rename the delivery_status column to status and a new column to specify which department the announcement is for
-- date: 15/08/2023

ALTER TABLE public.announcement_recipient
RENAME COLUMN delivery_status TO status;

ALTER TABLE public.announcement
ADD COLUMN announcement_for varchar(60) NOT NULL DEFAULT 'general';

ALTER TABLE public.announcement_recipient
DROP CONSTRAINT announcement_recipient_announcement_fk;

ALTER TABLE public.announcement_recipient
ADD CONSTRAINT announcement_recipient_announcement_fk
FOREIGN KEY (announcement_id) REFERENCES public.announcement (announcement_id)
ON DELETE CASCADE;

ALTER TABLE public.announcement_recipient
DROP CONSTRAINT announcement_recipient_employee_fk;

ALTER TABLE public.announcement_recipient
ADD CONSTRAINT announcement_recipient_employee_fk
FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id)
ON DELETE CASCADE;

ALTER TABLE public.announcement
ALTER COLUMN delivery_date TYPE timestamp with time zone;
