package com.app.rent_manager.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.rent_manager.Entity.RoomMaster;
import com.app.rent_manager.Entity.TenantMaster;
import com.app.rent_manager.Repositories.RoomMasterRepository;
import com.app.rent_manager.Repositories.TenantRepository;
import com.app.rent_manager.RequestBody.TenantRequest;
import com.app.rent_manager.ResponseBody.ResponseHandler;
import com.app.rent_manager.exceptions.InvalidRoomException;
import com.app.rent_manager.service.ITenantService;

@Service
public class TenantServiceImpl implements ITenantService {

	private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

	@Autowired
	TenantRepository tenantRepository;

	@Autowired
	RoomMasterRepository roomMasterRepository;

	@Autowired
	private TenantValidationService tenantValidationService;

	@Override
	public ResponseEntity<Map<String, Object>> addTenant(TenantRequest tenantRequest,String username ) {
		logger.info("Add Tenant Initiated:");
		tenantValidationService.validateTenantRequest(tenantRequest );
		
		
        Map<String, String> errors = tenantValidationService.validateTenantRequest(tenantRequest);
	        if (!errors.isEmpty()) {
	            return ResponseHandler.generateResponse("Validation failed", HttpStatus.BAD_REQUEST, errors);
	        }
		
		RoomMaster rooms = roomMasterRepository.findById(tenantRequest.getRoomId()).get();

		Integer currentCapacity = validateRoomCapacity(tenantRequest, rooms);

		TenantMaster tenantadd = new TenantMaster();

		Long roomId = rooms.getRoomId();
		tenantadd.setRoomId(roomId);
		tenantadd.setTenantName(tenantRequest.getTenantName());
		tenantadd.setRentPaid(tenantRequest.getRentPaid());
		tenantadd.setDepositPaid(tenantRequest.getDepositPaid());
		tenantadd.setAddressDetails(tenantRequest.getTenantAddress());
		tenantadd.setAdharNumber(tenantRequest.getAdharNumber());
		tenantadd.setRoomType(tenantRequest.getRoomType());
		tenantadd.setTenentType(tenantRequest.getTenantType());
		tenantadd.setCreatedBy(username);
		tenantadd.setCreatedDate(LocalDateTime.now());
		tenantadd.setUpdatedDate(LocalDateTime.now());
		tenantadd.setUpdatedBy(username);
		tenantadd.setFaterName(tenantRequest.getFatherName());
		tenantadd.setStayFrom(tenantadd.getCreatedDate());
		tenantadd.setMotherName(tenantRequest.getMotherName());

		tenantRepository.save(tenantadd);

		rooms.setCurrentCapacity(currentCapacity + 1);
		roomMasterRepository.save(rooms);

		return ResponseHandler.generateResponse("Member added successfully.", HttpStatus.OK, tenantadd);

	}
	
	@Override
	public ResponseEntity<Map<String, Object>> getTenantsByRoom(Long roomId) {
	    List<TenantMaster> tenants = tenantRepository.findByRoomId(roomId);

	    if (tenants.isEmpty()) {
	        return ResponseHandler.generateResponse("No tenants found for room ID: " + roomId, HttpStatus.NOT_FOUND, null);
	    }

	    List<Map<String, Object>> tenantList = tenants.stream()
	            .map(tenant -> {
	                Map<String, Object> tenantMap = new HashMap<>();
	                tenantMap.put("tenantId", tenant.getTenantId());
	                tenantMap.put("tenantName", tenant.getTenantName());
	                tenantMap.put("addressDetails", tenant.getAddressDetails());
	                tenantMap.put("adharNumber", tenant.getAdharNumber());
	                tenantMap.put("tenantType", tenant.getTenentType());
	                tenantMap.put("stayFrom", tenant.getStayFrom());
	                return tenantMap;
	            })
	            .collect(Collectors.toList());

	    return ResponseHandler.generateResponse("Tenants fetched successfully.", HttpStatus.OK, tenantList);
	}
	
	private Integer validateRoomCapacity(TenantRequest tenant, RoomMaster rooms) {
		Integer maxCapacity = rooms.getMaxCapacity();
		Integer currentCapacity = rooms.getCurrentCapacity();

		List<TenantMaster> existingTenants = tenantRepository.findByRoomId(tenant.getRoomId());

		Integer tenantCount = existingTenants.size();

		int remainingCapacity = maxCapacity - tenantCount;

		logger.info("Max Capacity: {}", maxCapacity);
		logger.info("Current Capacity: {}", currentCapacity);
		logger.info("Existing Tenants Count: {}", tenantCount);
		logger.info("Remaining Capacity: {}", remainingCapacity);

		if (remainingCapacity <= 0) {
			throw new InvalidRoomException("Room Capacity Limit Exceeded for room type: " + rooms.getRoomType());
		}
		return currentCapacity;
	}
}
