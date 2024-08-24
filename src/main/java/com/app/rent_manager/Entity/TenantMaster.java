package com.app.rent_manager.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "TENANT_MASTER")
@Data
public class TenantMaster extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long TenantId;

	@Column(name = "TENANT_NAME")
	private String tenantName;

	@Column(name = "ROOM_ID")
	private Long roomId;

	@Column(name = "DEPOSIT_PAID")
	private Integer depositPaid;
	
	@Column(name = "TENANT_TYPE")
	private String tenentType;

	@Column(name = "RENT_PAID")
	private int rentPaid;

	@Column(name = "Address_Details")
	private String AddressDetails;
	
	@Column(name = "ROOM_TYPE")
	private String roomType;
	
	@Column(name = "STAY_FROM")
	private LocalDateTime stayFrom;

	@Column(name = "ADHAR")
	private String adharNumber;

	@Column(name = "FATHER_NAME")
	private String faterName;

	@Column(name = "MOTHER_NAME")
	private String motherName;
}
