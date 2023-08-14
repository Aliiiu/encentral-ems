INSERT INTO public.role(role_id, role_name, role_description, date_created, created_by)
VALUES  ('user', 'User', 'Regular User', NOW(), '{"employee_id":"system", "name":"system"}'),
        ('admin', 'Admin', 'Admin User', NOW(), '{"employee_id":"system", "name":"system"}');

INSERT INTO public.permission(permission_id, permission_name, permission_action, permission_description, date_created, created_by)
VALUES  ('create_announcement',              'Create Announcement',              'CREATE',       'Create Announcement Record',               NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_announcement',                'Read Announcement',                'READ',         'Read Announcement Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_announcement',              'Update Announcement',              'UPDATE',       'Update Announcement Record',               NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_announcement',              'Delete Announcement',              'DELETE',       'Delete Announcement Record',               NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_app_config',                'Create AppConfig',                 'CREATE',       'Create AppConfig Record',                  NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_app_config',                  'Read AppConfig',                   'READ',         'Read AppConfig Record',                    NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_app_config',                'Update AppConfig',                 'UPDATE',       'Update AppConfig Record',                  NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_app_config',                'Delete AppConfig',                 'DELETE',       'Delete AppConfig Record',                  NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_attendance',                'Create Attendance',                'CREATE',       'Create Attendance Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_attendance',                  'Read Attendance',                  'READ',         'Read Attendance Record',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_attendance',                'Update Attendance',                'UPDATE',       'Update Attendance Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_attendance',                'Delete Attendance',                'DELETE',       'Delete Attendance Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_department',                'Create Department',                'CREATE',       'Create Department Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_department',                  'Read Department',                  'READ',         'Read Department Record',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_department',                'Update Department',                'UPDATE',       'Update Department Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_department',                'Delete Department',                'DELETE',       'Delete Department Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_department_head',           'Create DepartmentHead',            'CREATE',       'Create DepartmentHead Record',             NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_department_head',             'Read DepartmentHead',              'READ',         'Read DepartmentHead Record',               NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_department_head',           'Update DepartmentHead',            'UPDATE',       'Update DepartmentHead Record',             NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_department_head',           'Delete DepartmentHead',            'DELETE',       'Delete DepartmentHead Record',             NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_document',                  'Create Document',                  'CREATE',       'Create Document Record',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_document',                    'Read Document',                    'READ',         'Read Document Record',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_document',                  'Update Document',                  'UPDATE',       'Update Document Record',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_document',                  'Delete Document',                  'DELETE',       'Delete Document Record',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_emergency_contact',         'Create EmergencyContact',          'CREATE',       'Create EmergencyContact Record',           NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_emergency_contact',           'Read EmergencyContact',            'READ',         'Read EmergencyContact Record',             NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_emergency_contact',         'Update EmergencyContact',          'UPDATE',       'Update EmergencyContact Record',           NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_emergency_contact',         'Delete EmergencyContact',          'DELETE',       'Delete EmergencyContact Record',           NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_employee',                  'Create Employee',                  'CREATE',       'Create Employee Record',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_employee',                    'Read Employee',                    'READ',         'Read Employee Record',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_employee',                  'Update Employee',                  'UPDATE',       'Update Employee Record',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_employee',                  'Delete Employee',                  'DELETE',       'Delete Employee Record',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_employee_update',           'Create EmployeeUpdate',            'CREATE',       'Create EmployeeUpdate Record',             NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_employee_update',             'Read EmployeeUpdate',              'READ',         'Read EmployeeUpdate Record',               NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_employee_update',           'Update EmployeeUpdate',            'UPDATE',       'Update EmployeeUpdate Record',             NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_employee_update',           'Delete EmployeeUpdate',            'DELETE',       'Delete EmployeeUpdate Record',             NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_event',                     'Create Event',                     'CREATE',       'Create Event Record',                      NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_event',                       'Read Event',                       'READ',         'Read Event Record',                        NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_event',                     'Update Event',                     'UPDATE',       'Update Event Record',                      NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_event',                     'Delete Event',                     'DELETE',       'Delete Event Record',                      NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_leave_request',             'Create LeaveRequest',              'CREATE',       'Create LeaveRequest Record',               NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_leave_request',               'Read LeaveRequest',                'READ',         'Read LeaveRequest Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_leave_request',             'Update LeaveRequest',              'UPDATE',       'Update LeaveRequest Record',               NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_leave_request',             'Delete LeaveRequest',              'DELETE',       'Delete LeaveRequest Record',               NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_notification',              'Create Notification',              'CREATE',       'Create Notification Record',               NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_notification',                'Read Notification',                'READ',         'Read Notification Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_notification',              'Update Notification',              'UPDATE',       'Update Notification Record',               NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_notification',              'Delete Notification',              'DELETE',       'Delete Notification Record',               NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_notification_template',     'Create NotificationTemplate',      'CREATE',       'Create NotificationTemplate Record',       NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_notification_template',       'Read NotificationTemplate',        'READ',         'Read NotificationTemplate Record',         NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_notification_template',     'Update NotificationTemplate',      'UPDATE',       'Update NotificationTemplate Record',       NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_notification_template',     'Delete NotificationTemplate',      'DELETE',       'Delete NotificationTemplate Record',       NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_option',                    'Create Option',                    'CREATE',       'Create Option Record',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_option',                      'Read Option',                      'READ',         'Read Option Record',                       NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_option',                    'Update Option',                    'UPDATE',       'Update Option Record',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_option',                    'Delete Option',                    'DELETE',       'Delete Option Record',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_option_type',               'Create OptionType',                'CREATE',       'Create OptionType Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_option_type',                 'Read OptionType',                  'READ',         'Read OptionType Record',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_option_type',               'Update OptionType',                'UPDATE',       'Update OptionType Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_option_type',               'Delete OptionType',                'DELETE',       'Delete OptionType Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_permission',                'Create Permission',                'CREATE',       'Create Permission Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_permission',                  'Read Permission',                  'READ',         'Read Permission Record',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_permission',                'Update Permission',                'UPDATE',       'Update Permission Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_permission',                'Delete Permission',                'DELETE',       'Delete Permission Record',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_role',                      'Create Role',                      'CREATE',       'Create Role Record',                       NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_role',                        'Read Role',                        'READ',         'Read Role Record',                         NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_role',                      'Update Role',                      'UPDATE',       'Update Role Record',                       NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_role',                      'Delete Role',                      'DELETE',       'Delete Role Record',                       NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('create_role_has_permission',       'Create RoleHasPermission',         'CREATE',       'Create RoleHasPermission Record',          NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('read_role_has_permission',         'Read RoleHasPermission',           'READ',         'Read RoleHasPermission Record',            NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('update_role_has_permission',       'Update RoleHasPermission',         'UPDATE',       'Update RoleHasPermission Record',          NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('delete_role_has_permission',       'Delete RoleHasPermission',         'DELETE',       'Delete RoleHasPermission Record',          NOW(),    '{"employee_id":"system", "name":"system"}');


INSERT INTO public.role_has_permission(role_has_permission_id, role_id, permission_id, date_created, created_by)
VALUES  ('user_read_announcement',                'user',         'read_announcement',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_create_announcement',             'admin',        'create_announcement',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_announcement',               'admin',        'read_announcement',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_announcement',             'admin',        'update_announcement',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_delete_announcement',             'admin',        'delete_announcement',                 NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_read_app_config',                  'user',         'read_app_config',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_update_app_config',                'user',         'update_app_config',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_create_app_config',               'admin',        'create_app_config',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_app_config',                 'admin',        'read_app_config',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_app_config',               'admin',        'update_app_config',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_delete_app_config',               'admin',        'delete_app_config',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_create_attendance',                'user',         'create_attendance',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_read_attendance',                  'user',         'read_attendance',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_create_attendance',               'admin',        'create_attendance',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_attendance',                 'admin',        'read_attendance',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_attendance',               'admin',        'update_attendance',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_delete_attendance',               'admin',        'delete_attendance',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_read_department',                  'user',         'read_department',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_create_department',               'admin',        'create_department',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_department',                 'admin',        'read_department',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_department',               'admin',        'update_department',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_delete_department',               'admin',        'delete_department',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_read_department_head',             'user',         'read_department_head',                NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_create_department_head',          'admin',        'create_department_head',              NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_department_head',            'admin',        'read_department_head',                NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_department_head',          'admin',        'update_department_head',              NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_delete_department_head',          'admin',        'delete_department_head',              NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_create_document',                  'user',         'create_document',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_read_document',                    'user',         'read_document',                       NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_create_document',                 'admin',        'create_document',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_document',                   'admin',        'read_document',                       NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_document',                 'admin',        'update_document',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_delete_document',                 'admin',        'delete_document',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_create_emergency_contact',         'user',         'create_emergency_contact',            NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_read_emergency_contact',           'user',         'read_emergency_contact',              NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_update_emergency_contact',         'user',         'update_emergency_contact',            NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_create_emergency_contact',        'admin',        'create_emergency_contact',            NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_emergency_contact',          'admin',        'read_emergency_contact',              NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_emergency_contact',        'admin',        'update_emergency_contact',            NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_delete_emergency_contact',        'admin',        'delete_emergency_contact',            NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_read_employee',                    'user',         'read_employee',                       NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_create_employee',                 'admin',        'create_employee',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_employee',                   'admin',        'read_employee',                       NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_employee',                 'admin',        'update_employee',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_delete_employee',                 'admin',        'delete_employee',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_create_employee_update',           'user',         'create_employee_update',              NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_read_employee_update',             'user',         'read_employee_update',                NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_update_employee_update',           'user',         'update_employee_update',              NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_create_employee_update',          'admin',        'create_employee_update',              NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_employee_update',            'admin',        'read_employee_update',                NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_employee_update',          'admin',        'update_employee_update',              NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_delete_employee_update',          'admin',        'delete_employee_update',              NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_read_event',                       'user',         'read_event',                          NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_create_event',                    'admin',        'create_event',                        NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_event',                      'admin',        'read_event',                          NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_event',                    'admin',        'update_event',                        NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_delete_event',                    'admin',        'delete_event',                        NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_create_leave_request',             'user',         'create_leave_request',                NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_read_leave_request',               'user',         'read_leave_request',                  NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_update_leave_request',             'user',         'update_leave_request',                NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_create_leave_request',            'admin',        'create_leave_request',                NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_leave_request',              'admin',        'read_leave_request',                  NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_leave_request',            'admin',        'update_leave_request',                NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_delete_leave_request',            'admin',        'delete_leave_request',                NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_read_permission',                  'user',         'read_permission',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_create_permission',               'admin',        'create_permission',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_permission',                 'admin',        'read_permission',                     NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_permission',               'admin',        'update_permission',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_delete_permission',               'admin',        'delete_permission',                   NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('user_read_role_has_permission',         'user',         'read_role_has_permission',            NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_create_role_has_permission',      'admin',        'create_role_has_permission',          NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_read_role_has_permission',        'admin',        'read_role_has_permission',            NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_update_role_has_permission',      'admin',        'update_role_has_permission',          NOW(),    '{"employee_id":"system", "name":"system"}'),
        ('admin_delete_role_has_permission',      'admin',        'delete_role_has_permission',          NOW(),    '{"employee_id":"system", "name":"system"}');
        