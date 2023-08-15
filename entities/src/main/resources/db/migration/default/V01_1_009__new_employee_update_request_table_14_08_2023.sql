-- author: WARITH
-- description: Recreating the schema for employee_update_request relation to conform with the UI design
-- date: 14/08/2013


DROP TABLE public.employee_update_request;

CREATE TABLE public.employee_update_request
(
    employee_update_request_id      varchar(64)                     NOT NULL,
    employee_id                     varchar(64)                     NOT NULL,
    approver_id                     varchar(64),
    approval_status                 employee_request_status         NOT NULL,
    remarks                         text,
    date_created                    timestamp with time zone        NOT NULL DEFAULT now(),
    date_modified                   timestamp with time zone,

    first_name                      varchar(64)                     NOT NULL,
    last_name                       varchar(64)                     NOT NULL,
    employee_gender                 varchar(64)                     NOT NULL,
    phone_number                    varchar(20)                     NOT NULL,
    address                         varchar(225)                    NOT NULL,
    highest_certification           varchar(64),
    state_of_origin                 varchar(64),
    country_of_origin               varchar(64)                     NOT NULL,
    employee_marital_status         varchar(64),
    date_of_birth                   date                            NOT NULL,

    emergency_contact_full_name     varchar(128)                    NOT NULL,
    emergency_contact_relationship  varchar(64)                     NOT NULL,
    emergency_contact_phone_number     varchar(20)                     NOT NULL,

    CONSTRAINT employee_update_request_pk PRIMARY KEY (employee_update_request_id),
    CONSTRAINT employee_update_request_employee_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE,
    CONSTRAINT employee_update_request_approver_fk FOREIGN KEY (approver_id) REFERENCES public.employee (employee_id) ON UPDATE CASCADE,

    CONSTRAINT employee_highest_certification_fk FOREIGN KEY (highest_certification) REFERENCES public.option (option_id) ON UPDATE CASCADE,
    CONSTRAINT employee_state_of_origin_fk FOREIGN KEY (state_of_origin) REFERENCES public.option (option_id) ON UPDATE CASCADE,
    CONSTRAINT employee_country_of_origin_fk FOREIGN KEY (country_of_origin) REFERENCES public.option (option_id) ON UPDATE CASCADE,
    CONSTRAINT employee_employee_gender_fk FOREIGN KEY (employee_gender) REFERENCES public.option (option_id) ON UPDATE CASCADE,
    CONSTRAINT employee_employee_marital_status_fk FOREIGN KEY (employee_marital_status) REFERENCES public.option (option_id) ON UPDATE CASCADE
);

