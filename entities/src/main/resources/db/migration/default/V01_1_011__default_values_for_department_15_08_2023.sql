-- author: WARITH
-- description: Default values for Employee Department
-- date: 15/08/2013


INSERT INTO public.department(department_id, department_name, working_hours, description, date_created, created_by)
VALUES  ('design', 'Product Design', 6, 'User Interface and Product Design Department', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('frontend', 'Frontend', 6, 'Frontend', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('backend', 'Backend', 6, 'Backend', NOW(), '{"employee_id":"system", "name":"system"}');
