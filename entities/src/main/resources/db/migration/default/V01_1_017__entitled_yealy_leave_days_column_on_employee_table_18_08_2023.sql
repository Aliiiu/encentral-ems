-- author: ALIU
-- description: New column on Employee Table: Entitled Yearly Leave days defaulted to 21;
-- date: 17/08/2013

ALTER TABLE public.employee
ADD COLUMN entitled_yearly_leave_days INT NOT NULL  DEFAULT 21;
