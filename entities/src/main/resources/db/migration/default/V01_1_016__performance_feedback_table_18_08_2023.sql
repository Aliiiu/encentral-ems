

CREATE TABLE public.performance_evaluation
(
    performance_evaluation_id       varchar(64)                     NOT NULL,
    attendance_accuracy             double                          NOT NULL,
    leave_performance               double                          NOT NULL,
    evaluation_start_date           date                            NOT NULL,
    evaluation_end_date             date                            NOT NULL,
    employee_id                     varchar(64)                     NOT NULL,
    evaluator_id                    varchar(64)                     NOT NULL,
    date_created                    timestamp with time zone        NOT NULL DEFAULT now(),

    CONSTRAINT performance_evaluation_pk PRIMARY KEY (performance_evaluation_id),
    CONSTRAINT performance_evaluation_employee_fk FOREIGN KEY (employee_id) REFERENCES public.employee (employee_id) ON DELETE CASCADE,
    CONSTRAINT performance_evaluation_evaluator_fk FOREIGN KEY (evaluator_id) REFERENCES public.employee (employee_id) ON DELETE SET NULL
);
