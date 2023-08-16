-- author: WARITH
-- description: Rename the delivery_status column to status and a new column to specify which department the announcement is for
-- date: 15/08/2023

ALTER TABLE public.announcement
RENAME COLUMN delivery_status TO status;

ALTER TABLE public.announcement
ADD COLUMN announcement_for varchar(60) NOT NULL DEFAULT 'general';

ALTER TABLE public.announcement_recipient
DROP FOREIGN KEY announcement_recipient_announcement_fk,
ADD CONSTRAINT announcement_recipient_announcement_fk
FOREIGN KEY (announcement_id) REFERENCES public.announcement (announcement_id)
ON DELETE CASCADE;

ALTER TABLE public.announcement_recipient
DROP FOREIGN KEY announcement_recipient_employee_fk,
ADD CONSTRAINT announcement_recipient_employee_fk
FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id)
ON DELETE CASCADE;

