package com.app.rent_manager.RequestBody;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class TenantRequest {

	@NotBlank(message = "Tenant name is required.")
	String tenantName;
	// allocated room
	@NotNull(message = "Room ID is required.")
	Long roomId;

	@NotNull(message = "Deposit paid is required.")
	int depositPaid;

	@NotNull(message = "Rent paid is required.")
	int rentPaid;

	@NotBlank(message = "Adhar number is required.")
	@Pattern(regexp = "\\d{12}", message = "Adhar number must be 12 digits.")
	String adharNumber;

	@NotBlank(message = "Tenant address is required.")
	String tenantAddress;

	@NotBlank(message = "RoomType is required.")
	String roomType;

	@NotBlank(message = "Father's name is required.")
	String fatherName;

	@NotBlank(message = "Mother's name is required.")
	String motherName;

	@NotBlank(message = "Tenant type is required.")
	String tenantType;

}
