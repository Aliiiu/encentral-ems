CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

ALTER TABLE public.option
ALTER COLUMN option_id
SET DEFAULT uuid_generate_v4();

ALTER TABLE public.option_type
RENAME COLUMN option_type TO option_type_name;

ALTER TABLE public.option_type
RENAME COLUMN option_description TO option_type_description;


INSERT INTO public.option_type (option_type_id, option_type_name, option_type_description, date_created, created_by)
VALUES  ('country', 'Country', 'Country', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('state', 'State', 'State', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('gender', 'Gender', 'Gender', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('marital_status', 'Marital Status', 'Marital Status', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('certification', 'Certification', 'Certification', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('leave_type', 'Leave Type', 'Leave Type', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('event_type', 'Event Type', 'Event Type', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('document_type', 'Document Type', 'Document Type', NOW(), '{"employee_id":"system", "name":"system"}');


INSERT INTO public.option(option_id, option_type_id, option_value, date_created, created_by)
VALUES  ('male', 'gender', 'Male', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('female', 'gender', 'Female', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('other', 'gender', 'Others', NOW(), '{"employee_id":"system", "name":"system"}');


INSERT INTO public.option(option_id, option_type_id, option_value, date_created, created_by)
VALUES  ('nigeria', 'country', 'Nigeria', NOW(), '{"employee_id":"system", "name":"system"}');


INSERT INTO public.option(option_id, option_type_id, option_value, date_created, created_by)
VALUES  ('single', 'marital_status', 'Single', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('married', 'marital_status', 'Married', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('engaged', 'marital_status', 'Engaged', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('widowed', 'marital_status', 'Widowed', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('divorced', 'marital_status', 'Divorced', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('separated', 'marital_status', 'Separated', NOW(), '{"employee_id":"system", "name":"system"}');


INSERT INTO public.option(option_id, option_type_id, option_value, date_created, created_by)
VALUES  ('ba', 'certification', 'Bachelor of Arts', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('bed', 'certification', 'Bachelor of Education', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('bsc', 'certification', 'Bachelor of Science', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('llb', 'certification', 'Bachelor of Law', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('nysc', 'certification', 'National Youth Service Corps', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('ond', 'certification', 'Ordinary National Diploma', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('hnd', 'certification', 'Higher National Diploma', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('msc', 'certification', 'Master of Science', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('phd', 'certification', 'Doctor of Philosophy', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('jsce', 'certification', 'Junior School Certificate', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('ssce', 'certification', 'Senior School Certificate', NOW(), '{"employee_id":"system", "name":"system"}');
