-- author: WARITH
-- description: More value for permissions and role has permissions
-- date: 17/08/2023

INSERT INTO public.permission(permission_id, permission_name, permission_action, permission_description, date_created, created_by)
VALUES  ('create_announcement_recipient',    'Create AnnouncementRecipient',     'CREATE',       'Create AnnouncementRecipient Record',      NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_announcement_recipient',      'Read AnnouncementRecipient',       'READ',         'Read AnnouncementRecipient Record',        NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_announcement_recipient',    'Update AnnouncementRecipient',     'UPDATE',       'Update AnnouncementRecipient Record',      NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_announcement_recipient',    'Delete AnnouncementRecipient',     'DELETE',       'Delete AnnouncementRecipient Record',      NOW(),    '{"employee_id":"system", "name":"system"}');

INSERT INTO public.role_has_permission(role_has_permission_id, role_id, permission_id, date_created, created_by)
VALUES  ('user_read_announcement_recipient',      'user',         'read_announcement_recipient',         NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_update_announcement_recipient',    'user',         'update_announcement_recipient',       NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_announcement_recipient',     'admin',        'read_announcement_recipient',         NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_announcement_recipient',   'admin',        'update_announcement_recipient',       NOW(),    '{"employee_id":"system", "name":"system"}');
