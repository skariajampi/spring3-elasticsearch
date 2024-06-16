package org.example.springboot.elasticsearch.controller.exception.handler;

import jakarta.validation.Valid;
import org.example.springboot.elasticsearch.model.index.AuditRecord;
import org.example.springboot.elasticsearch.model.dto.AuditDto;
import org.example.springboot.elasticsearch.service.AuditService;
import org.example.springboot.elasticsearch.service.exception.AuditNotFoundException;
import org.example.springboot.elasticsearch.service.exception.DuplicateCorrelationIdException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }


    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public AuditRecord createAudit(@Valid @RequestBody AuditDto Audit) throws DuplicateCorrelationIdException {
        return auditService.create(AuditDto.transform(Audit));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{entityId}")
    public List<AuditRecord> getAuditByEntityId(@PathVariable String entityId) throws AuditNotFoundException {
        return auditService.getByEntityId(entityId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/query")
    public List<AuditRecord> getByPayload(@RequestParam(value = "payload") String payload) {
        return auditService.getByPayload(payload);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}")
    public AuditRecord updateAudit(@PathVariable String id, @RequestBody AuditDto auditDto) throws AuditNotFoundException {
        return auditService.update(id, AuditDto.transform(auditDto));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void deleteAudit(@PathVariable String id) {
        auditService.deleteByCorrelationId(id);
    }


}
