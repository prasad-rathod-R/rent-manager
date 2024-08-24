package com.app.rent_manager.RequestBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PgRquest {

	private Long roomId;
	private int capacity;
	private int floorNo;
	private Double deposit;
	private String roomName;
	private int roomRent;
	private String addressDetails;
	private String roomType;
	private int maxCapacity;
	private String ownerType;
}
