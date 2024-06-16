package org.example.springboot.elasticsearch.example.service.impl;

import org.example.springboot.elasticsearch.AuditElasticsearchContainer;
import org.example.springboot.elasticsearch.model.index.AuditRecord;
import org.example.springboot.elasticsearch.service.AuditService;
import org.example.springboot.elasticsearch.service.exception.DuplicateCorrelationIdException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DefaultAuditRecordServiceIT {

    @Autowired
    private AuditService auditService;

    @Autowired
    private ElasticsearchTemplate template;

    @Container
    private static final ElasticsearchContainer elasticsearchContainer = new AuditElasticsearchContainer();

    @BeforeAll
    static void setUp() {
        elasticsearchContainer.start();
    }

    @BeforeEach
    void testIsContainerRunning() {
        assertTrue(elasticsearchContainer.isRunning());
        recreateIndex();
    }

    @Test
    void testGetAuditByEntityId() throws DuplicateCorrelationIdException {
        auditService.create(createAudit("12 rules for life", "Jordan Peterson",
                                        OffsetDateTime.parse("2022-03-03'T'14:33:33+00:00"), "978-0345816023",
                                        "externalRefId1"));
        List<AuditRecord> result = auditService.getByEntityId("externalRefId1");
        assertFalse(result.isEmpty());
        AuditRecord createdAuditRecord = result.get(0);
        assertNotNull(createdAuditRecord);
        assertEquals("12 rules for life", createdAuditRecord.getMessage());
        assertEquals("Jordan Peterson", createdAuditRecord.getPayload());
        assertEquals(2018, createdAuditRecord.getCreatedAt());
        assertEquals("978-0345816023", createdAuditRecord.getCorrelationId());
    }


    @Test
    void testFindByPayload() throws DuplicateCorrelationIdException {
        auditService.create(createAudit("12 rules for life", "Jordan Peterson",
                                        OffsetDateTime.parse("2022-03-03'T'14:33:33+00:00"), "978-0345816023",
                                        "externalRefId1"));
        List<AuditRecord> auditRecords = auditService.getByPayload("Jordan Peterson");

        assertNotNull(auditRecords);
        assertEquals(2, auditRecords.size());
    }

    @Test
    void testFindByEntityIdAndPayload() throws DuplicateCorrelationIdException {
        auditService.create(createAudit("12 rules for life", "Jordan Peterson",
                                        OffsetDateTime.parse("2022-03-03'T'14:33:33+00:00"), "978-0345816023",
                                        "externalRefId1"));
        List<AuditRecord> auditRecords = auditService.findByEntityIdAndPayload("rules", "jordan");

        assertNotNull(auditRecords);
        assertEquals(2, auditRecords.size());
    }

    @Test
    void testCreateAudit() throws DuplicateCorrelationIdException {
        AuditRecord createdAuditRecord =
                auditService.create(createAudit("12 rules for life", "Jordan Peterson",
                                                OffsetDateTime.parse("2022-03-03'T'14:33:33+00:00"), "978-0345816023",
                                                "externalRefId1"));
        assertNotNull(createdAuditRecord);
        assertNotNull(createdAuditRecord.getCorrelationId());
        assertEquals("12 rules for life", createdAuditRecord.getMessage());
        assertEquals("Jordan Peterson", createdAuditRecord.getPayload());
        assertEquals(2018, createdAuditRecord.getCreatedAt());
        assertEquals("978-0345816023", createdAuditRecord.getExternalRefId());
    }

    /*@Test
    void testCreateErrorWithDuplicateISBNThrowsException() throws DuplicateIsbnException {
        ErrorRecordIndexModel createdErrorRecordIndexModel = errorService.create(createError("12 rules for life", "Jordan Peterson", 2018, "978-0345816023"));
        assertNotNull(createdErrorRecordIndexModel);
        assertThrows(DuplicateIsbnException.class, () -> {
            errorService.create(createError("Test title", "Test author", 2010, "978-0345816023"));
        });
    }

    @Test
    void testDeleteAuditById() throws DuplicateIsbnException {
        ErrorRecordIndexModel createdErrorRecordIndexModel = errorService.create(createError("12 rules for life", "Jordan Peterson", 2018, "978-0345816023"));

        assertNotNull(createdErrorRecordIndexModel);
        assertNotNull(createdErrorRecordIndexModel.getCorrelationId());

        errorService.deleteByCorrelationId(createdErrorRecordIndexModel.getCorrelationId());
        List<ErrorRecordIndexModel> errorRecordIndexModels = errorService.findByPayload("Jordan Peterson");

        assertTrue(errorRecordIndexModels.isEmpty());
    }

    @Test
    void testUpdateAudit() throws DuplicateIsbnException, ErrorNotFoundException {
        ErrorRecordIndexModel errorRecordIndexModelToUpdate = errorService.create(createError("12 rules for life", "Jordan Peterson", 2000, "978-0345816023"));

        assertNotNull(errorRecordIndexModelToUpdate);
        assertNotNull(errorRecordIndexModelToUpdate.getCorrelationId());

        errorRecordIndexModelToUpdate.setCreatedAt(2018);
        ErrorRecordIndexModel updatedErrorRecordIndexModel = errorService.update(errorRecordIndexModelToUpdate.getCorrelationId(), errorRecordIndexModelToUpdate);

        assertNotNull(updatedErrorRecordIndexModel);
        assertNotNull(updatedErrorRecordIndexModel.getCorrelationId());
        assertEquals("12 rules for life", updatedErrorRecordIndexModel.getMessage());
        assertEquals("Jordan Peterson", updatedErrorRecordIndexModel.getPayload());
        assertEquals(2018, updatedErrorRecordIndexModel.getCreatedAt());
        assertEquals("978-0345816023", updatedErrorRecordIndexModel.getCorrelationId());
    }

    @Test
    void testUpdateAuditThrowsExceptionIfCannotFindAudit() {
        ErrorRecordIndexModel updatedErrorRecordIndexModel = createError("12 rules for life", "Jordan Peterson", 2000, "978-0345816023");

        assertThrows(ErrorNotFoundException.class, () -> {
            errorService.update("1A2B3C", updatedErrorRecordIndexModel);
        });
    }*/

    private AuditRecord createAudit(String message, String payload, OffsetDateTime createdAt, String correlationId, String externalRefId) {
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setMessage(message);
        auditRecord.setPayload(payload);
        auditRecord.setCreatedAt(createdAt);
        auditRecord.setCorrelationId(correlationId);
        return auditRecord;
    }

    private void recreateIndex() {
        if (template.indexOps(AuditRecord.class).exists()) {
            template.indexOps(AuditRecord.class).delete();
            template.indexOps(AuditRecord.class).create();
        }
    }

    @AfterAll
    static void destroy() {
        elasticsearchContainer.stop();
    }
}
