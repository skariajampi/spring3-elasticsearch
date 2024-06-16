package org.example.springboot.elasticsearch.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.InvariantFailureRecord;
import org.example.springboot.elasticsearch.model.index.AuditRecord;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditDto {

    @JsonProperty
    private String correlationId;

    @JsonProperty
    private String userId;

    @JsonProperty
    private Boolean isError;

    @JsonProperty
    private String message;

    @JsonProperty
    private String componentType;

    @JsonProperty
    private String componentInstance;

    @JsonProperty
    private String entityId;

    @JsonProperty
    private String entityType;

    @JsonProperty
    private String exceptionStackTrace;

    @JsonProperty
    private String payload;

    @JsonProperty
    private String externalRefId;

    @JsonProperty
    private List<InvariantFailureRecord> invariantFailures;

    public static AuditRecord transform(AuditDto auditDto) {
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setCorrelationId(auditDto.getCorrelationId());
        auditRecord.setPayload(auditDto.getPayload());
        auditRecord.setMessage(auditDto.getMessage());
        auditRecord.setExternalRefId(auditDto.getExternalRefId());
        auditRecord.setInvariantFailures(auditDto.getInvariantFailures());
        return auditRecord;
    }

    }