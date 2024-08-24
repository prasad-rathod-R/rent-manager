package com.app.rent_manager.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.app.rent_manager.RequestBody.TenantRequest;

public interface ITenantService {
	
    public ResponseEntity<Map<String, Object>> addTenant(TenantRequest tenant,String usernamr);

    public ResponseEntity<Map<String, Object>> getTenantsByRoom(Long roomId);

}
