package com.app.rent_manager.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.rent_manager.Entity.RoomMaster;
import com.app.rent_manager.Entity.TenantMaster;
import com.app.rent_manager.Repositories.RoomMasterRepository;
import com.app.rent_manager.Repositories.TenantRepository;
import com.app.rent_manager.RequestBody.TenantRequest;
import com.app.rent_manager.exceptions.InvalidRoomException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Service
public class TenantValidationService {

	@Autowired
	private TenantRepository tenantRepository;

	private Validator validator;

	public TenantValidationService() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	public Map<String, String> validateTenantRequest(TenantRequest tenantRequest) {
		Map<String, String> errors = new HashMap<>();

		if (tenantRequest == null) {
			errors.put("request", "Tenant request cannot be null.");
			return errors;
		}

		// Validate tenant request fields
		Set<ConstraintViolation<TenantRequest>> violations = validator.validate(tenantRequest);
		for (ConstraintViolation<TenantRequest> violation : violations) {
			errors.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		// Check for duplicate Adhar number
		if (tenantRepository.existsByAdharNumber(tenantRequest.getAdharNumber())) {
			errors.put("adharNumber", "A tenant with the same Adhar number already exists.");
		}

		if (!isValidRoomType(tenantRequest.getRoomType())) {
			errors.put("roomType", "Invalid room type. Must be one of: 1 RK, 1 BHK, 2 BHK, 3 BHK.");
		}

		return errors;
	}

	private boolean isValidRoomType(String roomType) {
		// Valid room types

		return roomType.equalsIgnoreCase("1 RK") || roomType.equalsIgnoreCase("1 BHK")
				|| roomType.equalsIgnoreCase("2 BHK") || roomType.equalsIgnoreCase("3 BHK");
	}
}
