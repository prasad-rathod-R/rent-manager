package com.app.rent_manager.serviceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
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
import com.app.rent_manager.Repositories.RoomMasterRepository;
import com.app.rent_manager.RequestBody.PgRquest;
import com.app.rent_manager.RequestBody.RoomRequest;
import com.app.rent_manager.ResponseBody.ResponseHandler;
import com.app.rent_manager.ResponseBody.RoomDTO;
import com.app.rent_manager.exceptions.InvalidRoomException;
import com.app.rent_manager.mappers.RoomMapper;
import com.app.rent_manager.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

	private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

	private static final List<String> HOUSE_OWNER_ROOM_TYPES = Arrays.asList("1 RK", "1 BHK", "2 BHK", "3 BHK");
	private static final List<String> PG_OWNER_ROOM_TYPES = Arrays.asList("1 SHARING", "2 SHARING", "3 SHARING",
			"4 SHARING", "5 SHARING");

	@Autowired
	RoomMasterRepository roomMasterRepository;

	@Override
	public void addRoom(RoomRequest roomRequest, String username) {
		logger.info("Add Room Iniated:");
		validateRoomType(roomRequest.getRoomType(), roomRequest.getOwnerType());

		RoomMaster existingRoom = roomMasterRepository.findByRoomName(roomRequest.getRoomName())
				.orElseGet(() -> createNewRoom(roomRequest, username));

		// updateRoomCapacity(existingRoom, roomRequest.getCapacity());

		roomMasterRepository.save(existingRoom);
	}

	@Override
	public void addPGSharingRoom(PgRquest pgRequest, String username) {
		logger.info("Add Room Iniated:");
		validateRoomType(pgRequest.getRoomType(), pgRequest.getOwnerType());

		RoomMaster existingRoom = roomMasterRepository.findByRoomName(pgRequest.getRoomName())
				.orElseGet(() -> addPgSharingRoom(pgRequest, username));

		// updateRoomCapacity(existingRoom, roomRequest.getCapacity());

		roomMasterRepository.save(existingRoom);
	}

	/*
	 * public void applyYearlyRentIncrease() {
	 * 
	 * RoomMaster room = new RoomMaster(); int currentYear = Year.now().getValue();
	 * int yearsPassed = currentYear - room.getLastRentUpdateYear();
	 * 
	 * if (yearsPassed > 0) { int annualIncrease = 500; int newRent =
	 * room.getRoomRent() + (yearsPassed * annualIncrease);
	 * room.setRoomRent(newRent); room.setLastRentUpdateYear(currentYear); } }
	 */

	private RoomMaster createNewRoom(RoomRequest roomRequest, String username) {
		RoomMaster room = new RoomMaster();
		room.setRoomName(roomRequest.getRoomName());
		room.setAddressDetails(roomRequest.getAddressDetails());

		int deposit = roomRequest.getRoomRent() * 3;
		room.setDeposit(Double.valueOf(deposit));
		room.setFloorNo(roomRequest.getFloorNo());
		room.setCreatedBy(username);
		room.setRoomType(roomRequest.getRoomType());
		room.setMaxCapacity(getMaxCapacity(roomRequest.getRoomType(), roomRequest.getOwnerType()));
		room.setOwnerType(roomRequest.getOwnerType());
		room.setRoomRent(roomRequest.getRoomRent());
		room.setCreatedDate(LocalDateTime.now());
		room.setCurrentCapacity(0);
		return room;
	}

	private RoomMaster addPgSharingRoom(PgRquest pgRquest, String username) {
		RoomMaster pg = new RoomMaster();
		pg.setRoomName(pgRquest.getRoomName());
		pg.setAddressDetails(pgRquest.getAddressDetails());

		int deposit = pgRquest.getRoomRent() * 3;
		pg.setDeposit(Double.valueOf(deposit));
		pg.setFloorNo(pgRquest.getFloorNo());
		pg.setCreatedBy(username);
		pg.setRoomType(pgRquest.getRoomType());
		pg.setMaxCapacity(getMaxCapacity(pgRquest.getRoomType(), pgRquest.getOwnerType()));
		pg.setOwnerType(pgRquest.getOwnerType());
		pg.setRoomRent(pgRquest.getRoomRent());
		pg.setCreatedDate(LocalDateTime.now());
		pg.setCurrentCapacity(0);
		return pg;
	}

	@Override
	public ResponseEntity<Map<String, Object>> viewAllRoomDashboard(String usernamr) {

		List<RoomMaster> alldata = roomMasterRepository.findAll();

		return ResponseHandler.generateResponse("Data Fetched Sucessfully", HttpStatus.OK, alldata);

	}

	@Override
	public RoomMaster getRoomById(Long id, String userName) {
		return roomMasterRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
	}

	private Integer getMaxCapacity(String roomType, String ownerType) {
		if ("houseOwner".equalsIgnoreCase(ownerType)) {
			return getHouseOwnerMaxCapacity(roomType);
		} else if ("pgOwner".equalsIgnoreCase(ownerType)) {
			return getPgOwnerMaxCapacity(roomType);
		} else {
			throw new InvalidRoomException("Invalid owner type: " + ownerType);
		}
	}

	private Integer getHouseOwnerMaxCapacity(String roomType) {
		switch (roomType.toUpperCase()) {
		case "1 RK":
			return 2;
		case "1 BHK":
			return 3;
		case "2 BHK":
			return 4;
		case "3 BHK":
			return 5;
		default:
			throw new InvalidRoomException("Invalid room type for house owner: " + roomType);
		}
	}

	private Integer getPgOwnerMaxCapacity(String roomType) {
		switch (roomType.toUpperCase()) {
		case "1 SHARING":
			return 1;
		case "2 SHARING":
			return 2;
		case "3 SHARING":
			return 3;
		case "4 SHARING":
			return 4;
		case "5 SHARING":
			return 5;
		default:
			throw new InvalidRoomException("Invalid room type for PG owner: " + roomType);
		}
	}

	private void validateRoomType(String roomType, String ownerType) {
		if (!isValidRoomType(roomType, ownerType)) {
			throw new InvalidRoomException(
					"Invalid room type: " + roomType + ". Check valid types for owner type: " + ownerType);
		}
	}

	private boolean isValidRoomType(String roomType, String ownerType) {
		if ("houseOwner".equalsIgnoreCase(ownerType)) {
			return HOUSE_OWNER_ROOM_TYPES.contains(roomType.toUpperCase());
		} else if ("pgOwner".equalsIgnoreCase(ownerType)) {
			return PG_OWNER_ROOM_TYPES.contains(roomType.toUpperCase());
		} else {
			return false;
		}
	}

	@Override
	public void updateRoom(RoomRequest roomRequest, String usernamr) {

		RoomMaster existingRoom = roomMasterRepository.findById(roomRequest.getRoomId())
				.orElseThrow(() -> new RuntimeException("Room not found with id: " + roomRequest.getRoomId()));

		validateRoomType(roomRequest.getRoomType(), roomRequest.getOwnerType());

		existingRoom.setRoomName(roomRequest.getRoomName());
		existingRoom.setAddressDetails(roomRequest.getAddressDetails());
		existingRoom.setDeposit(roomRequest.getDeposit());
		existingRoom.setFloorNo(roomRequest.getFloorNo());
		existingRoom.setRoomType(roomRequest.getRoomType());
		existingRoom.setUpdatedDate(LocalDateTime.now());

		roomMasterRepository.save(existingRoom);
	}

	@Override
	public ResponseEntity<Map<String, Object>> checkRoomAvailability(Long roomId) {
		logger.info("Check Room Availability Initiated:");

		RoomMaster room = roomMasterRepository.findById(roomId)
				.orElseThrow(() -> new InvalidRoomException("Room not found with id: " + roomId));

		int currentCapacity = room.getCurrentCapacity();
		int maxCapacity = room.getMaxCapacity();
		int remainingCapacity = maxCapacity - currentCapacity;

		Map<String, Object> response = new HashMap<>();
		response.put("roomId", roomId);
		response.put("roomType", room.getRoomType());
		response.put("currentCapacity", currentCapacity);
		response.put("maxCapacity", maxCapacity);
		response.put("remainingCapacity", remainingCapacity);

		if (remainingCapacity > 0) {
			response.put("availability", "Available");
		} else {
			response.put("availability", "Not Available");
		}

		return ResponseHandler.generateResponse("Room availability checked successfully.", HttpStatus.OK, response);
	}
	
	
	@Override
    public List<RoomDTO> getAvailableRooms() {
        List<RoomMaster> availableRooms = roomMasterRepository.findAvailableRooms();
        return availableRooms.stream()
                             .map(RoomMapper::toRoomDTO)
                             .collect(Collectors.toList());
    }
	
	@Override
    public List<RoomDTO> getAvailableRoomsByLocation(String location) {
        List<RoomMaster> availableRooms = roomMasterRepository.findAvailableRoomsByLocation(location);
        return availableRooms.stream()
                             .map(RoomMapper::toRoomDTO)
                             .collect(Collectors.toList());
    }
	
	
	@Override
    public List<RoomDTO> searchRooms(String roomType, Integer minRent, Integer maxRent) {
        List<RoomMaster> rooms = roomMasterRepository.searchRooms(roomType, minRent, maxRent);
        return rooms.stream()
                    .map(RoomMapper::toRoomDTO)
                    .collect(Collectors.toList());
    }
	
	@Override
	public List<Map<String, Object>> getAllDeposits() {
	    List<RoomMaster> rooms = roomMasterRepository.findAll();
	    
	    // Use a proper Map type declaration
	    return rooms.stream()
	            .map(room -> {
	                Map<String, Object> depositMap = new HashMap<>();
	                depositMap.put("roomId", room.getRoomId());
	                depositMap.put("roomName", room.getRoomName());
	                depositMap.put("deposit", room.getDeposit());
	                return depositMap;
	            })
	            .collect(Collectors.toList());
	}

}
