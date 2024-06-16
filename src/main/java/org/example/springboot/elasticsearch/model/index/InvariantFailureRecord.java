package org.example.springboot.elasticsearch.model.index;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@Document(indexName = "errors")
public class InvariantFailureRecord {
    private String invariantId;
    private String invariantName;
    private String externalDescription;
    private String internalDescription;
}
