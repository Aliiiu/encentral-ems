-- author: WARITH
-- description: Rename the delivery_status column to status and a new column to specify which department the announcement is for
-- date: 15/08/2023

ALTER TABLE public.announcement
RENAME COLUMN delivery_status TO status;

ALTER TABLE public.announcement
ADD COLUMN announcement_for varchar(60) NOT NULL DEFAULT 'general';
