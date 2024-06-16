package org.example.springboot.elasticsearch.service.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import org.example.springboot.elasticsearch.model.index.AuditRecord;
import org.example.springboot.elasticsearch.repository.AuditRepository;
import org.example.springboot.elasticsearch.service.AuditService;
import org.example.springboot.elasticsearch.service.exception.AuditNotFoundException;
import org.example.springboot.elasticsearch.service.exception.DuplicateCorrelationIdException;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.List;

import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.match;

@Service
public class DefaultAuditService implements AuditService {

    private final AuditRepository auditRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;

    public DefaultAuditService(AuditRepository auditRepository, ElasticsearchTemplate elasticsearchTemplate) {
        this.auditRepository = auditRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public List<AuditRecord> getByEntityId(String entityId) {
        return auditRepository.findByEntityId(entityId);
    }

    @Override
    public boolean getByCorrelationId(String correlationId) {
        return auditRepository.existsById(correlationId);
    }

    @Override
    public List<AuditRecord> getByPayload(String payload) {
        return auditRepository.findByPayloadLike(payload);
    }

    @Override
    public AuditRecord create(AuditRecord auditRecord) throws DuplicateCorrelationIdException {
        if (!getByCorrelationId(auditRecord.getCorrelationId())) {
            return auditRepository.save(auditRecord);
        }
        throw new DuplicateCorrelationIdException(String.format("The provided Correlation ID: %s already exists. " +
                                                                        "Use update instead!", auditRecord.getCorrelationId()));
    }

    @Override
    public void deleteByCorrelationId(String correlationId) {
        auditRepository.deleteById(correlationId);
    }

    @Override
    public AuditRecord update(String id, AuditRecord auditRecord) throws AuditNotFoundException {
        AuditRecord oldAuditRecord = auditRepository.findById(id)
            .orElseThrow(() -> new AuditNotFoundException("There is not error associated with the given correlation id"));
        oldAuditRecord.setCorrelationId(auditRecord.getCorrelationId());
        oldAuditRecord.setMessage(auditRecord.getMessage());
        oldAuditRecord.setCreatedAt(auditRecord.getCreatedAt());
        oldAuditRecord.setPayload(auditRecord.getPayload());
        return auditRepository.save(oldAuditRecord);
    }

    @Override
    public List<AuditRecord> findByEntityIdAndPayload(String entityId, String payload) {
        var criteria = QueryBuilders.bool(builder -> builder.must(
                match(queryAuthor -> queryAuthor.field("payload").query(payload)),
                match(queryTitle -> queryTitle.field("entityId").query(entityId))
                                                                 ));

        return elasticsearchTemplate.search(NativeQuery.builder().withQuery(criteria).build(), AuditRecord.class)
                .stream().map(SearchHit::getContent).toList();
    }

}
