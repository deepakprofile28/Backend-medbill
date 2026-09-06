package com.medbill.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medbill.entity.AccessPermission;
import com.medbill.service.AccessPermissionService;

@RestController
@RequestMapping("/api/access")
@CrossOrigin(origins = "http://localhost:4200")
public class AccessPermissionController {

    private final AccessPermissionService accessPermissionService;

    public AccessPermissionController(AccessPermissionService accessPermissionService) {
        this.accessPermissionService = accessPermissionService;
    }

    @GetMapping
    public ResponseEntity<List<AccessPermission>> getAll(
            @RequestParam(name = "companyId", required = false) Long companyId,
            @RequestParam(name = "activeOnly", required = false, defaultValue = "false") boolean activeOnly) {

        List<AccessPermission> list = activeOnly
                ? accessPermissionService.getActive(companyId)
                : accessPermissionService.getAll(companyId);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessPermission> getById(@PathVariable Long id) {
        AccessPermission item = accessPermissionService.getById(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<AccessPermission> create(
            @RequestBody AccessPermission accessPermission,
            @RequestParam(name = "companyId", required = false) Long companyId) {

        AccessPermission created = accessPermissionService.create(accessPermission, companyId);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccessPermission> update(
            @PathVariable Long id,
            @RequestBody AccessPermission accessPermission) {

        AccessPermission updated = accessPermissionService.update(id, accessPermission);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accessPermissionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
