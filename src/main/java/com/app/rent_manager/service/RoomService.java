package com.app.rent_manager.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.app.rent_manager.Entity.RoomMaster;
import com.app.rent_manager.RequestBody.PgRquest;
import com.app.rent_manager.RequestBody.RoomRequest;
import com.app.rent_manager.ResponseBody.RoomDTO;

public interface RoomService {

	public void addRoom(RoomRequest room, String usernamr);

	public void addPGSharingRoom(PgRquest room, String usernamr);

	public void updateRoom(RoomRequest room, String usernamr);

	public ResponseEntity<Map<String, Object>> viewAllRoomDashboard(String usernamr);

	public RoomMaster getRoomById(Long id, String usernamr);
	
    ResponseEntity<Map<String, Object>> checkRoomAvailability(Long roomId);

    List<RoomDTO> searchRooms(String roomType, Integer minRent, Integer maxRent);

	List<RoomDTO> getAvailableRooms();

	List<RoomDTO> getAvailableRoomsByLocation(String location);

	List<Map<String, Object>> getAllDeposits();

}
