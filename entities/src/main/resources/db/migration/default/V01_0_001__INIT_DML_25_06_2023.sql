CREATE TYPE action AS ENUM ('CREATE', 'READ', 'UPDATE', 'DELETE');
CREATE TYPE request_status AS ENUM ('PENDING', 'APPROVED', 'REJECTED', 'CANCELED', 'IN_PROGRESS', 'COMPLETED');
CREATE TYPE employee_status AS ENUM ('ACTIVE', 'ON LEAVE', 'SUSPENDED', 'RESIGNED', 'TERMINATED', 'RETIRED');
CREATE TYPE notification_priority AS ENUM ('VERY HIGH', 'HIGH', 'NORMAL', 'LOW');
CREATE TYPE notification_status AS ENUM ('UNREAD', 'READ', 'DELETED');
CREATE TYPE degree AS ENUM ('WASSCE', 'A LEVELS', 'OND', 'HND', 'B.Sc/B.Arts/B.Eng', 'MSc/MBA', 'PhD');

CREATE TABLE public.role
(
    role_id            varchar(64)                 NOT NULL,
    role_name          varchar(64)                 NOT NULL,
    role_description   text,
    created_by         jsonb                       NOT NULL,
    last_updated_by    jsonb,
    date_created       timestamp with time zone    NOT NULL DEFAULT now(),
    date_updated       timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT role_pk PRIMARY KEY (role_id)
);

CREATE TABLE public.permission
(
    permission_id          varchar(64)                 NOT NULL,
    permission_name        varchar(64)                 NOT NULL,
    permission_action      ACTION                      NOT NULL,
    permission_description text,
    created_by             jsonb                       NOT NULL,
    last_updated_by        jsonb,
    date_created           timestamp with time zone    NOT NULL DEFAULT now(),
    date_updated           timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT permission_pk PRIMARY KEY (permission_id)
);

CREATE TABLE public.role_permission
(
    role_permission_id varchar(64)                 NOT NULL,
    role_id            varchar(64)                 NOT NULL,
    permission_id      varchar(64)                 NOT NULL,
    created_by         jsonb                       NOT NULL,
    last_updated_by    jsonb,
    date_created       timestamp with time zone    NOT NULL DEFAULT now(),
    date_updated       timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT role_permission_pk PRIMARY KEY (role_permission_id),
    CONSTRAINT role_id_fk FOREIGN KEY (role_id) REFERENCES public.role (role_id) ON UPDATE CASCADE,
    CONSTRAINT permission_id_fk FOREIGN KEY (permission_id) REFERENCES public.permission (permission_id) ON UPDATE CASCADE
);

CREATE TABLE public.department
(
    department_id      varchar(64)                 NOT NULL,
    department_name    varchar(64)                 NOT NULL,
    description        text,
    created_by         jsonb                       NOT NULL,
    last_updated_by    jsonb,
    date_created       timestamp with time zone    NOT NULL DEFAULT now(),
    date_updated       timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT department_pk PRIMARY KEY (department_id)
);

CREATE TABLE employee
(
    employee_id         varchar(64)                 NOT NULL,
    first_name          varchar(64)                 NOT NULL,
    last_name           varchar(64)                 NOT NULL,
    middle_name         varchar(64)                 NOT NULL,
    employee_role       varchar(64)                 NULL,
    phone_number        varchar(20)                 NULL,
    date_hired          date                        NOT NULL DEFAULT CURRENT_DATE,
    current_status      employee_status             NOT NULL DEFAULT 'ACTIVE',
    address             varchar(225)                NOT NULL,
    department_id       varchar(64),
    leave_days          integer                     NOT NULL DEFAULT 30,
    profile_picture_url text,
    highest_certification varchar(64),
    state_of_origin     varchar(64)                 NOT NULL,
    password            varchar(256)                NOT NULL,
    employee_email      varchar(60)                 NOT NULL,
    date_of_birth       date                        NOT NULL,
    login_attempts      integer                     NOT NULL DEFAULT 0,
    last_login          timestamp with time zone,
    created_by          jsonb                       NOT NULL,
    last_updated_by     jsonb,
    employee_active     boolean                     NOT NULL DEFAULT TRUE,
    date_created        timestamp with time zone    NOT NULL DEFAULT now(),
    date_updated        timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT employee_pk PRIMARY KEY (employee_id),
    CONSTRAINT employee_role_fk FOREIGN KEY (employee_role) REFERENCES public.role (role_id) ON UPDATE CASCADE,
    CONSTRAINT department_fk FOREIGN KEY (department_id) REFERENCES public.department (department_id) ON UPDATE CASCADE
);

CREATE TABLE public.department_head
(
    department_head_id   varchar(64)                NOT NULL,
    department_id        varchar(64)                NOT NULL,
    department_head      varchar(64)                NOT NULL,
    created_by           jsonb                      NOT NULL,
    last_updated_by      jsonb,
    date_created         timestamp with time zone   NOT NULL DEFAULT now(),
    date_updated         timestamp with time zone   NOT NULL DEFAULT now(),
    CONSTRAINT department_head_pk PRIMARY KEY (department_head_id),
    CONSTRAINT department_fk FOREIGN KEY (department_id) REFERENCES public.department (department_id) ON UPDATE CASCADE,
    CONSTRAINT department_head_fk FOREIGN KEY (department_head) REFERENCES public.employee (employee_id) ON UPDATE CASCADE
);

CREATE TABLE public.emergency_contact
(
    emergency_contact_id varchar(64)                 NOT NULL,
    employee_id          varchar(64)                 NOT NULL,
    first_name           varchar(64)                 NOT NULL,
    last_name            varchar(64)                 NOT NULL,
    phone_number         varchar(20)                 NOT NULL,
    relationship         varchar(64)                 NOT NULL,
    address              varchar(64)                 NOT NULL,
    email                varchar(64)                 NOT NULL,
    created_by           jsonb                       NOT NULL,
    last_updated_by      jsonb,
    date_created         timestamp with time zone    NOT NULL DEFAULT now(),
    date_updated         timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT emergency_contact_pk PRIMARY KEY (emergency_contact_id),
    CONSTRAINT employee_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE
);

CREATE TABLE public.attendance
(
    attendance_id     varchar(64)                 NOT NULL,
    employee_id       varchar(64)                 NOT NULL,
    attendance_date   date                        NOT NULL DEFAULT CURRENT_DATE,
    check_in_time     time                        NOT NULL DEFAULT CURRENT_TIME,
    check_out_time    time,
    CONSTRAINT attendance_pk PRIMARY KEY (attendance_id),
    CONSTRAINT employee_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE
);

CREATE TABLE public.leave_request
(
    leave_request_id   varchar(64)                 NOT NULL,
    employee_id        varchar(64)                 NOT NULL,
    approver_id        varchar(64),
    approval_status    request_status              NOT NULL DEFAULT 'PENDING',
    start_date         date                        NOT NULL,
    duration           integer                     NOT NULL,
    reason             text,
    remarks            text,
    date_created       timestamp with time zone    NOT NULL DEFAULT now(),
    date_updated       timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT leave_request_pk PRIMARY KEY (leave_request_id),
    CONSTRAINT employee_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE,
    CONSTRAINT approver_fk FOREIGN KEY (approver_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE
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
    date_read          timestamp with time zone,
    created_by         jsonb                       NOT NULL,
    last_updated_by    jsonb,
    date_created       timestamp with time zone    NOT NULL DEFAULT now(),
    date_updated       timestamp with time zone    NOT NULL DEFAULT now(),
    CONSTRAINT notification_pk PRIMARY KEY (notification_id),
    CONSTRAINT sender_fk FOREIGN KEY (sender_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE,
    CONSTRAINT receiver_fk FOREIGN KEY (receiver_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE
);

CREATE TABLE public.documents
(
    employee_id         character varying(64)      NOT NULL,
    document_id         character varying(64)      NOT NULL,
    document_name       character varying(512)     NOT NULL,
    document_description character varying(512),
    document_upload_path text                     NOT NULL,
    document_type       character varying(64)      NOT NULL,
    date_created        timestamp with time zone   NOT NULL,
    date_updated        timestamp with time zone   NOT NULL DEFAULT now(),
    created_by          jsonb                    NOT NULL,
    last_updated_by     jsonb,
    CONSTRAINT documents_pk PRIMARY KEY (document_id),
    CONSTRAINT employee_id_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) MATCH simple ON UPDATE cascade ON DELETE NO ACTION
);

CREATE TABLE public.configuration_option
(
    configuration_option_id character varying(64)  NOT NULL,
    configuration_key       character varying(60) UNIQUE NOT NULL,
    configuration_value     character varying(100) NOT NULL,
    date_created            timestamp with time zone NOT NULL DEFAULT now(),
    date_updated            timestamp with time zone NOT NULL DEFAULT now(),
    created_by              jsonb                   NOT NULL,
    last_updated_by         jsonb,
    CONSTRAINT configuration_option_pk PRIMARY KEY (configuration_option_id)
);

CREATE TABLE public.option_type
(
    option_type_id       character varying(100)   NOT NULL,
    option_type          character varying(100)   NOT NULL,
    option_description   text                     NOT NULL,
    date_created         timestamp with time zone NOT NULL DEFAULT now(),
    date_updated         timestamp with time zone NOT NULL DEFAULT now(),
    created_by           jsonb                   NOT NULL,
    last_updated_by      jsonb,
    CONSTRAINT option_type_pk PRIMARY KEY (option_type_id)
);

CREATE TABLE public.option
(
    option_id            character varying(64)    NOT NULL,
    option_type_id       character varying(64)    NOT NULL,
    option_value         character varying(100)   UNIQUE NOT NULL,
    date_created         timestamp with time zone NOT NULL DEFAULT now(),
    date_updated         timestamp with time zone NOT NULL DEFAULT now(),
    created_by           jsonb                   NOT NULL,
    last_updated_by      jsonb,
    CONSTRAINT option_pk PRIMARY KEY (option_id),
    CONSTRAINT option_type_id_fk FOREIGN KEY (option_type_id) REFERENCES public.option_type (option_type_id) MATCH simple ON UPDATE cascade ON DELETE NO ACTION
);

CREATE TABLE public.event
(
    event_id             character varying(64)    NOT NULL,
    event_title          character varying(64)    NOT NULL,
    event_description    character varying(100)   NOT NULL,
    event_type           character varying(100)   NOT NULL,
    start_date           date                     NOT NULL,
    end_date             date                     NOT NULL,
    date_created         timestamp with time zone NOT NULL DEFAULT now(),
    date_updated         timestamp with time zone NOT NULL DEFAULT now(),
    created_by           jsonb                   NOT NULL,
    last_updated_by      jsonb,
    CONSTRAINT event_pk PRIMARY KEY (event_id)
);
