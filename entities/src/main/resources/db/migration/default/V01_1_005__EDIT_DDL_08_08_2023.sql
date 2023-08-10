ALTER TABLE employee_has_document
DROP CONSTRAINT employee_has_document_pk;

UPDATE employee_has_document
SET employee_has_document_id = document_id;

ALTER TABLE employee_has_document
ADD PRIMARY KEY (employee_has_document_id);
