package org.example.springboot.elasticsearch.repository;

import org.example.springboot.elasticsearch.model.index.AuditRecord;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditRepository extends ElasticsearchRepository<AuditRecord, String> {

    List<AuditRecord> findByEntityId(String entityId);

    List<AuditRecord> findByPayloadLike(String payload);
}