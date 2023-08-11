package com.esl.internship.staffsync.document.management.model;

import com.esl.internship.staffsync.document.management.dto.DocumentDTO;
import com.esl.internship.staffsync.entities.JpaDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocumentMapper {
    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    JpaDocument mapDocument(Document model);

    Document mapDocument(JpaDocument entity);

    JpaDocument mapDocumentDTO(DocumentDTO modelDto);

    DocumentDTO mapDocumentDTO(JpaDocument entity);

}
