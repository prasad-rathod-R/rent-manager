package com.app.rent_manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.rent_manager.Entity.RoomMaster;
import com.app.rent_manager.RequestBody.PgRquest;
import com.app.rent_manager.RequestBody.RoomRequest;
import com.app.rent_manager.RequestBody.TenantRequest;
import com.app.rent_manager.ResponseBody.ResponseHandler;
import com.app.rent_manager.ResponseBody.RoomDTO;
import com.app.rent_manager.service.ITenantService;
import com.app.rent_manager.service.RoomService;

@RestController
@RequestMapping("/api/v1")
public class RentMasterController {

	@Autowired
	RoomService service;

	@Autowired
	ITenantService tenantService;

	/**
	 * API to add a new room.
	 * 
	 * @param room     The details of the room to be added.
	 * @param username The username of the person adding the room.
	 * @return ResponseEntity with a success message and the room details.
	 */
	@PostMapping("/addRoom")
	public ResponseEntity<Map<String, Object>> addRoom(@RequestBody RoomRequest room, @RequestHeader String username) {
		service.addRoom(room, username);
		return ResponseHandler.generateResponse("Data Added Successfully", HttpStatus.OK, room);
	}

	/**
	 * API to add a new tenant.
	 * 
	 * @param tenant   The details of the tenant to be added.
	 * @param username The username of the person adding the tenant.
	 * @return ResponseEntity with a success message and tenant details.
	 */
	@PostMapping("/addTenant")
	public ResponseEntity<Map<String, Object>> addTenant(@RequestBody TenantRequest tenant,
			@RequestHeader String username) {
		return tenantService.addTenant(tenant, username);
	}

	/**
	 * API to add a new sharing room for PG (Paying Guest) accommodation.
	 * 
	 * @param pg       The details of the PG room to be added.
	 * @param username The username of the person adding the PG room.
	 * @return ResponseEntity with a success message and PG room details.
	 */
	@PostMapping("/addSharingRoomForPg")
	public ResponseEntity<Map<String, Object>> addPgSharingRoom(@RequestBody PgRquest pg,
			@RequestHeader String username) {
		service.addPGSharingRoom(pg, username);
		return ResponseHandler.generateResponse("Data Added Successfully", HttpStatus.OK, pg);
	}

	/**
	 * API to update details of an existing room.
	 * 
	 * @param id          The ID of the room to be updated.
	 * @param roomRequest The updated details of the room.
	 * @param userName    The username of the person updating the room.
	 * @return ResponseEntity with status and message of the update operation.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateRoom(@PathVariable Long id, @RequestBody RoomRequest roomRequest,
			@RequestHeader String userName) {
		try {
			service.updateRoom(roomRequest, userName);
			return ResponseEntity.ok(Map.of("status", "success", "message", "Room updated successfully"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	/**
	 * API to retrieve details of a room by its ID.
	 * 
	 * @param id       The ID of the room to retrieve.
	 * @param userName The username of the person requesting the room details.
	 * @return ResponseEntity with the room details or an error message.
	 */
	@GetMapping("/getRoomById/{id}")
	public ResponseEntity<Map<String, Object>> getRoomById(@PathVariable Long id, @RequestHeader String userName) {
		try {
			RoomMaster room = service.getRoomById(id, userName);
			return ResponseEntity.ok(Map.of("status", "success", "data", room));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	/**
	 * API to retrieve a dashboard view of all rooms.
	 * 
	 * @param username The username of the person requesting the room dashboard.
	 * @return ResponseEntity with a list of all rooms or an error message.
	 */
	@GetMapping("/viewAllRooms")
	public ResponseEntity<Map<String, Object>> getAllRooms(@RequestHeader String username) {
		try {
			return service.viewAllRoomDashboard(username);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
		}
	}

	/**
	 * API to check the availability of a room by its ID.
	 * 
	 * @param roomId The ID of the room to check availability.
	 * @return ResponseEntity with the availability status of the room.
	 */
	@GetMapping("/checkAvailability/{roomId}")
	public ResponseEntity<?> checkRoomAvailability(@PathVariable Long roomId) {
		return service.checkRoomAvailability(roomId);
	}

	/**
	 * API to retrieve the list of tenants staying in a specific room.
	 * 
	 * @param roomId The ID of the room to retrieve tenant details.
	 * @return ResponseEntity with a list of tenants or an error message.
	 */
	@GetMapping("/tenant/room/{roomId}")
	public ResponseEntity<Map<String, Object>> getTenantsByRoom(@PathVariable Long roomId) {
		return tenantService.getTenantsByRoom(roomId);
	}

	/**
	 * API to retrieve a list of available rooms.
	 * 
	 * @return ResponseEntity with a list of available rooms or a message if no
	 *         rooms are available.
	 */
	@GetMapping("/available")
	public ResponseEntity<?> getAvailableRooms() {
		List<RoomDTO> availableRooms = service.getAvailableRooms();
		if (availableRooms.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No available rooms found.");
		}
		return ResponseEntity.ok(availableRooms);
	}

	/**
	 * API to search for rooms based on room type and rent range.
	 * 
	 * @param roomType The type of the room (e.g., 1 RK, 1 BHK).
	 * @param minRent  The minimum rent value.
	 * @param maxRent  The maximum rent value.
	 * @return ResponseEntity with a list of rooms matching the criteria or a
	 *         message if no results are found.
	 */
	@GetMapping("/search")
	public ResponseEntity<?> searchRooms(@RequestParam(value = "roomType", required = false) String roomType,
			@RequestParam(value = "minRent", required = false) Integer minRent,
			@RequestParam(value = "maxRent", required = false) Integer maxRent) {
		List<RoomDTO> rooms = service.searchRooms(roomType, minRent, maxRent);
		if (rooms.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No results found for the search criteria.");
		}
		return ResponseEntity.ok(rooms);
	}

	/**
	 * API to search for available rooms based on location.
	 * 
	 * @param location The location to search for available rooms.
	 * @return ResponseEntity with a list of available rooms for the specified
	 *         location or a message if no rooms are found.
	 */
	@GetMapping("/available/searchByLocation")
	public ResponseEntity<?> searchAvailableRoomsByLocation(@RequestParam String location) {
		List<RoomDTO> availableRooms = service.getAvailableRoomsByLocation(location);
		if (availableRooms.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("No available rooms found for the specified location.");
		}
		return ResponseEntity.ok(availableRooms);
	}

	/**
	 * API to retrieve all deposits along with the room ID and room name.
	 * 
	 * @return ResponseEntity with a list of deposits or a message if no deposits
	 *         are found.
	 */
	@GetMapping("/owner/getAllDeposits")
	public ResponseEntity<Map<String, Object>> getAllDeposits() {
		try {
			List<Map<String, Object>> deposits = service.getAllDeposits();
			if (deposits.isEmpty()) {
				return ResponseHandler.generateResponse("No deposits found.", HttpStatus.NOT_FOUND, null);
			}
			return ResponseHandler.generateResponse("Deposits fetched successfully.", HttpStatus.OK, deposits);
		} catch (Exception e) {
			return ResponseHandler.generateResponse("Error fetching deposits.", HttpStatus.INTERNAL_SERVER_ERROR,
					e.getMessage());
		}
	}

}
