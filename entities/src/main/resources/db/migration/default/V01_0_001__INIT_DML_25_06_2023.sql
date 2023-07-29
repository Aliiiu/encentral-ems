CREATE TYPE action AS ENUM ('CREATE', 'READ', 'UPDATE', 'DELETE');
CREATE TYPE employee_request_status AS ENUM ('PENDING', 'APPROVED', 'REJECTED', 'CANCELED', 'COMPLETED');
CREATE TYPE leave_request_status AS ENUM ('PENDING', 'APPROVED', 'REJECTED', 'CANCELED', 'IN_PROGRESS', 'COMPLETED');
CREATE TYPE employee_status AS ENUM ('ACTIVE', 'ON_LEAVE', 'SUSPENDED', 'RESIGNED', 'TERMINATED', 'RETIRED');
CREATE TYPE notification_priority AS ENUM ('LOW', 'NORMAL', 'HIGH', 'VERY_HIGH');
CREATE TYPE notification_status AS ENUM ('UNREAD', 'READ', 'DELETED');
CREATE TYPE event_status AS ENUM ('UPCOMING', 'ONGOING', 'ENDED');

CREATE TABLE public.option_type
(
    option_type_id       character varying(100)   NOT NULL,
    option_type          character varying(100)   NOT NULL,
    option_description   text                     NOT NULL,
    date_created         timestamp with time zone NOT NULL DEFAULT now(),
    date_modified        timestamp with time zone NOT NULL DEFAULT now(),
    created_by           json                     NOT NULL,
    modified_by          json,
    CONSTRAINT option_type_pk PRIMARY KEY (option_type_id)
);

CREATE TABLE public.option
(
    option_id            character varying(64)    NOT NULL,
    option_type_id       character varying(64)    NOT NULL,
    option_value         character varying(100)   UNIQUE NOT NULL,
    date_created         timestamp with time zone NOT NULL DEFAULT now(),
    date_modified        timestamp with time zone NOT NULL DEFAULT now(),
    created_by           json                     NOT NULL,
    modified_by          json,
    CONSTRAINT option_pk PRIMARY KEY (option_id),
    CONSTRAINT option_option_type_fk FOREIGN KEY (option_type_id) REFERENCES public.option_type (option_type_id) MATCH simple ON UPDATE cascade ON DELETE NO ACTION,
    CONSTRAINT option_option_value_uq UNIQUE (option_type_id, option_value)
);

CREATE TABLE public.role
(
    role_id            varchar(64)                 NOT NULL,
    role_name          varchar(64)                 NOT NULL,
    role_description   text,
    created_by         json                        NOT NULL,
    modified_by        json,
    date_created       timestamp with time zone    NOT NULL DEFAULT now(),
    date_modified      timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT role_pk PRIMARY KEY (role_id)
);

CREATE TABLE public.permission
(
    permission_id          varchar(64)                 NOT NULL,
    permission_name        varchar(64)                 NOT NULL,
    permission_action      action                      NOT NULL,
    permission_description text,
    created_by             json                        NOT NULL,
    modified_by            json,
    date_created           timestamp with time zone    NOT NULL DEFAULT now(),
    date_modified          timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT permission_pk PRIMARY KEY (permission_id)
);

CREATE TABLE public.role_has_permission
(
    role_has_permission_id varchar(64)                 NOT NULL,
    role_id                varchar(64)                 NOT NULL,
    permission_id          varchar(64)                 NOT NULL,
    created_by             json                        NOT NULL,
    date_created           timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT role_has_permission_pk PRIMARY KEY (role_has_permission_id),
    CONSTRAINT role_has_permission_role_fk FOREIGN KEY (role_id) REFERENCES public.role (role_id) ON UPDATE CASCADE,
    CONSTRAINT role_has_permission_permission_fk FOREIGN KEY (permission_id) REFERENCES public.permission (permission_id) ON UPDATE CASCADE
);

CREATE TABLE public.department
(
    department_id      varchar(64)                 NOT NULL,
    department_name    varchar(64)                 NOT NULL,
    working_hours      integer                     NOT NULL DEFAULT 8,
    description        text,
    created_by         json                       NOT NULL,
    modified_by        json,
    date_created       timestamp with time zone    NOT NULL DEFAULT now(),
    date_modified      timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT department_pk PRIMARY KEY (department_id)
);

CREATE TABLE employee
(
    employee_id         varchar(64)                 NOT NULL,
    first_name          varchar(64)                 NOT NULL,
    last_name           varchar(64)                 NOT NULL,
    employee_gender     varchar(64)                 NOT NULL,
    role_id             varchar(64)                 NOT NULL,
    phone_number        varchar(20)                 NOT NULL,
    date_hired          date                        NOT NULL,
    current_status      employee_status             NOT NULL,
    address             varchar(225)                NOT NULL,
    job_title           varchar(60)                 NOT NULL,
    department_id       varchar(64),
    profile_picture_url text,
    highest_certification varchar(64),
    state_of_origin     varchar(64),
    country_of_origin   varchar(64)                 NOT NULL,
    employee_marital_status varchar(64),
    password            varchar(256)                NOT NULL,
    employee_email      varchar(60)                 NOT NULL,
    date_of_birth       date                        NOT NULL,
    leave_days          integer                     NOT NULL DEFAULT 30,
    login_attempts      integer                     NOT NULL DEFAULT 0,
    last_login          timestamp with time zone,
    created_by          json                        NOT NULL,
    modified_by         json,
    employee_active     boolean                     NOT NULL DEFAULT TRUE,
    date_created        timestamp with time zone    NOT NULL DEFAULT now(),
    date_modified       timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT employee_pk PRIMARY KEY (employee_id),
    CONSTRAINT employee_role_fk FOREIGN KEY (role_id) REFERENCES public.role (role_id) ON UPDATE CASCADE,
    CONSTRAINT employee_department_fk FOREIGN KEY (department_id) REFERENCES public.department (department_id) ON UPDATE CASCADE,
    CONSTRAINT employee_highest_certification_fk FOREIGN KEY (highest_certification) REFERENCES public.option (option_id) ON UPDATE CASCADE,
    CONSTRAINT employee_state_of_origin_fk FOREIGN KEY (state_of_origin) REFERENCES public.option (option_id) ON UPDATE CASCADE,
    CONSTRAINT employee_country_of_origin_fk FOREIGN KEY (country_of_origin) REFERENCES public.option (option_id) ON UPDATE CASCADE,
    CONSTRAINT employee_employee_gender_fk FOREIGN KEY (employee_gender) REFERENCES public.option (option_id) ON UPDATE CASCADE,
    CONSTRAINT employee_employee_marital_status_fk FOREIGN KEY (employee_marital_status) REFERENCES public.option (option_id) ON UPDATE CASCADE
);

CREATE TABLE public.department_head
(
    department_head_id   varchar(64)                NOT NULL,
    department_id        varchar(64)                NOT NULL,
    employee_id          varchar(64)                NOT NULL,
    created_by           json                       NOT NULL,
    modified_by          json,
    date_created         timestamp with time zone   NOT NULL DEFAULT now(),
    date_modified        timestamp with time zone   NOT NULL DEFAULT now(),
    CONSTRAINT department_head_pk PRIMARY KEY (department_head_id),
    CONSTRAINT department_head_department_fk FOREIGN KEY (department_id) REFERENCES public.department (department_id) ON UPDATE CASCADE,
    CONSTRAINT department_head_employee_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE
);

CREATE TABLE public.emergency_contact
(
    emergency_contact_id varchar(64)                 NOT NULL,
    employee_id          varchar(64)                 NOT NULL,
    first_name           varchar(64)                 NOT NULL,
    last_name            varchar(64)                 NOT NULL,
    contact_gender       varchar(64),
    phone_number         varchar(20)                 NOT NULL,
    relationship         varchar(64)                 NOT NULL,
    address              varchar(64)                 NOT NULL,
    email                varchar(64)                 NOT NULL,
    created_by           json                        NOT NULL,
    modified_by          json,
    date_created         timestamp with time zone    NOT NULL DEFAULT now(),
    date_modified        timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT emergency_contact_pk PRIMARY KEY (emergency_contact_id),
    CONSTRAINT emergency_contact_employee_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE,
    CONSTRAINT emergency_contact_gender_fk FOREIGN KEY (contact_gender) REFERENCES public.option (option_id) ON UPDATE CASCADE
);

CREATE TABLE public.attendance
(
    attendance_id     varchar(64)                 NOT NULL,
    employee_id       varchar(64)                 NOT NULL,
    attendance_date   date                        NOT NULL DEFAULT CURRENT_DATE,
    check_in_time     time                        NOT NULL DEFAULT CURRENT_TIME,
    check_out_time    time,
    CONSTRAINT attendance_pk PRIMARY KEY (attendance_id),
    CONSTRAINT attendance_employee_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE
);

CREATE TABLE public.leave_request
(
    leave_request_id   varchar(64)                 NOT NULL,
    employee_id        varchar(64)                 NOT NULL,
    approver_id        varchar(64),
    approval_status    leave_request_status        NOT NULL DEFAULT 'PENDING',
    leave_type         varchar(64)                 NOT NULL,
    start_date         date                        NOT NULL,
    duration           integer                     NOT NULL,
    reason             text,
    remarks            text,
    date_created       timestamp with time zone    NOT NULL DEFAULT now(),
    date_modified      timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT leave_request_pk PRIMARY KEY (leave_request_id),
    CONSTRAINT leave_request_employee_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE,
    CONSTRAINT leave_request_approver_fk FOREIGN KEY (approver_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE,
    CONSTRAINT leave_request_leave_type_fk FOREIGN KEY (leave_type) REFERENCES public.option (option_id) ON UPDATE CASCADE
);

CREATE TABLE public.employee_update_request
(
    employee_update_request_id   varchar(64)                 NOT NULL,
    employee_id                  varchar(64)                 NOT NULL,
    approver_id                  varchar(64),
    update_field_name            varchar(64)                 NOT NULL,
    old_value                    text,
    update_new_value             text                        NOT NULL,
    approval_status              employee_request_status                 NOT NULL,
    reason                       text,
    remarks                      text,
    date_created                 timestamp with time zone     NOT NULL DEFAULT now(),
    date_modified                timestamp with time zone     NOT NULL DEFAULT now(),
    CONSTRAINT employee_update_request_pk PRIMARY KEY (employee_update_request_id),
    CONSTRAINT employee_update_request_employee_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE,
    CONSTRAINT employee_update_request_approver_fk FOREIGN KEY (approver_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE
);

CREATE TABLE public.notification_template
(
    notification_template_id       varchar(64)                 NOT NULL,
    notification_template_name     varchar(64) UNIQUE          NOT NULL,
    notification_template_content  text                        NOT NULL,
    notification_description       text,
    created_by                     json                        NOT NULL,
    modified_by                    json,
    date_created                   timestamp with time zone    NOT NULL DEFAULT now(),
    date_modified                  timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT notification_template_pk PRIMARY KEY (notification_template_id)
);

CREATE TABLE public.notification
(
    notification_id    varchar(64)                 NOT NULL,
    notification_title varchar(64)                 NOT NULL,
    notification_message text,
    sender_id          varchar(64)                 NOT NULL,
    receiver_id        varchar(64)                 NOT NULL,
    priority           notification_priority       NOT NULL DEFAULT 'NORMAL',
    delivery_status    notification_status         NOT NULL DEFAULT 'UNREAD',
    notification_template varchar(64)              NOT NULL,
    date_read          timestamp with time zone,
    created_by         json                        NOT NULL,
    modified_by        json,
    date_created       timestamp with time zone    NOT NULL DEFAULT now(),
    date_modified      timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT notification_pk PRIMARY KEY (notification_id),
    CONSTRAINT notification_sender_fk FOREIGN KEY (sender_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE,
    CONSTRAINT notification_receiver_fk FOREIGN KEY (receiver_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE,
    CONSTRAINT notification_notification_template_fk FOREIGN KEY (notification_template) REFERENCES public.notification_template (notification_template_id) ON UPDATE CASCADE
);

CREATE TABLE public.document
(
    document_id             character varying(64)      NOT NULL,
    document_name           character varying(512)     NOT NULL,
    document_description    character varying(512),
    document_upload_path    text                       NOT NULL,
    date_created            timestamp with time zone   NOT NULL,
    date_modified           timestamp with time zone   NOT NULL DEFAULT now(),
    created_by              json                       NOT NULL,
    modified_by             json,
    CONSTRAINT document_pk PRIMARY KEY (document_id)
);

CREATE TABLE public.employee_has_document
(
    employee_has_document_id   character varying(64)      NOT NULL,
    employee_id                character varying(64)      NOT NULL,
    document_id                character varying(64)      NOT NULL,
    document_type              character varying(64)      NOT NULL,
    date_created               timestamp with time zone   NOT NULL,
    created_by                 json                       NOT NULL,
    CONSTRAINT employee_has_document_pk PRIMARY KEY (document_id),
    CONSTRAINT employee_has_document_employee_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) MATCH simple ON UPDATE cascade ON DELETE NO ACTION,
    CONSTRAINT employee_has_document_document_fk FOREIGN KEY (document_id) REFERENCES public.document (document_id) ON UPDATE CASCADE,
    CONSTRAINT employee_has_document_document_type_fk FOREIGN KEY (document_type) REFERENCES public.option (option_id) ON UPDATE CASCADE
);

CREATE TABLE public.app_config
(
    app_config_id           character varying(64)            NOT NULL,
    configuration_key       character varying(60) UNIQUE     NOT NULL,
    configuration_value     character varying(100)           NOT NULL,
    date_created            timestamp with time zone         NOT NULL DEFAULT now(),
    date_modified           timestamp with time zone         NOT NULL DEFAULT now(),
    created_by              json                             NOT NULL,
    modified_by             json,
    CONSTRAINT app_config_pk PRIMARY KEY (app_config_id),
    CONSTRAINT app_config_configuration_key_uq UNIQUE (configuration_key)
);

CREATE TABLE public.event
(
    event_id             character varying(64)    NOT NULL,
    event_title          character varying(64)    NOT NULL,
    event_description    character varying(100)   NOT NULL,
    event_type           character varying(100)   NOT NULL,
    event_status         event_status             NOT NULL,
    start_date           date                     NOT NULL,
    end_date             date                     NOT NULL,
    date_created         timestamp with time zone NOT NULL DEFAULT now(),
    date_modified        timestamp with time zone NOT NULL DEFAULT now(),
    created_by           json                     NOT NULL,
    modified_by          json,
    CONSTRAINT event_pk PRIMARY KEY (event_id),
    CONSTRAINT event_event_type_fk FOREIGN KEY (event_type) REFERENCES public.option (option_id) ON UPDATE CASCADE
);

CREATE TABLE public.audit_log
(
    audit_log_id            character varying(64)            NOT NULL,
    employee_id             character varying(64)            NOT NULL,
    table_name              character varying(60)            NOT NULL,
    row_id                  character varying(64)            NOT NULL,
    field_name              character varying(60)            NOT NULL,
    initial_value           text,
    new_value               text                             NOT NULL,
    action_type             action                           NOT NULL,
    date_modified           timestamp with time zone         NOT NULL DEFAULT now(),
    CONSTRAINT audit_log_pk PRIMARY KEY (audit_log_id),
    CONSTRAINT audit_log_employee_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE
);

CREATE TABLE public.announcement
(
    announcement_id    varchar(64)                 NOT NULL,
    announcement_title varchar(60)                 NOT NULL,
    announcement_message text,
    sender_id          varchar(64)                 NOT NULL,
    priority           notification_priority       NOT NULL DEFAULT 'NORMAL',
    delivery_date      date,
    created_by         json                        NOT NULL,
    modified_by        json,
    date_created       timestamp with time zone    NOT NULL DEFAULT now(),
    date_modified      timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT announcement_pk PRIMARY KEY (announcement_id),
    CONSTRAINT announcement_sender_fk FOREIGN KEY (sender_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE
);

CREATE TABLE public.announcement_recipient
(
    announcement_recipient_id     varchar(64)                 NOT NULL,
    announcement_id               varchar(64)                 NOT NULL,
    employee_id                   varchar(64)                 NOT NULL,
    delivery_status               notification_status         NOT NULL DEFAULT 'UNREAD',
    date_read                     timestamp with time zone,
    date_created                  timestamp with time zone    NOT NULL DEFAULT now(),
    date_modified                 timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT announcement_recipient_pk PRIMARY KEY (announcement_recipient_id),
    CONSTRAINT announcement_recipient_announcement_fk FOREIGN KEY (announcement_id) REFERENCES public.announcement (announcement_id) ON UPDATE CASCADE,
    CONSTRAINT announcement_recipient_employee_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE
);