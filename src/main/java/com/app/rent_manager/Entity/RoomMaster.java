package com.app.rent_manager.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ROOM_MASTER")
@Data
public class RoomMaster extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long RoomId;

	@Column(name = "ROOM_NAME")
	private String roomName;
	
	@Column(name = "OWNER_TYPE")
    private String ownerType; // 'houseOwner' or 'pgOwner'

	@Column(name = "ROOM_RENT")
	private int roomRent;

	@Column(name = "CURRENT_CAPACITY")
	private Integer currentCapacity;

	@Column(name = "MAXCAPACITY")
	private int maxCapacity;

	@Column(name = "Address_Details")
	private String addressDetails;

	@Column(name = "Deposit")
	private Double Deposit;

	@Column(name = "Capacity")
	private int Capacity;

	@Column(name = "Room_Type")
	private String roomType;

	@Column(name = "Floor_No")
	private int FloorNo;

	@Column(name = "TenantId")
	private Long tenentID;
	
}
