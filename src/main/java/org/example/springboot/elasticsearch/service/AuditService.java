package org.example.springboot.elasticsearch.service;

import org.example.springboot.elasticsearch.model.index.AuditRecord;
import org.example.springboot.elasticsearch.service.exception.AuditNotFoundException;
import org.example.springboot.elasticsearch.service.exception.DuplicateCorrelationIdException;

import java.util.List;

public interface AuditService {

    List<AuditRecord> getByEntityId(String entityId);

    List<AuditRecord> getByPayload(String payload);

    AuditRecord create(AuditRecord auditRecord) throws DuplicateCorrelationIdException;

    void deleteByCorrelationId(String correlationId);

    AuditRecord update(String id, AuditRecord auditRecord) throws AuditNotFoundException;

    List<AuditRecord> findByEntityIdAndPayload(String entityId, String payload);
}
