package org.example.springboot.elasticsearch.model.index;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.InvariantFailureRecord;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "audit")
public class AuditRecord {

    @Id
    @JsonProperty
    private String correlationId;

    @JsonProperty
    private String userId;

    @JsonProperty
    private Boolean isError;

    @Field(type = FieldType.Date, format = DateFormat.date_time, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty
    private OffsetDateTime createdAt;

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

}
