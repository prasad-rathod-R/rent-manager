package com.app.rent_manager.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.rent_manager.Entity.RoomMaster;

@Repository
public interface RoomMasterRepository extends JpaRepository<RoomMaster, Long> {

	Optional<RoomMaster> findByRoomName(String roomName);

	List<RoomMaster> findByRoomTypeContainingIgnoreCaseAndRoomRentBetween(String roomType, Integer minRent,
			Integer maxRent);

	@Query("SELECT r FROM RoomMaster r WHERE " + "(COALESCE(:roomType, NULL) IS NULL OR r.roomType = :roomType) AND "
			+ "(COALESCE(:minRent, NULL) IS NULL OR r.roomRent >= :minRent) AND "
			+ "(COALESCE(:maxRent, NULL) IS NULL OR r.roomRent <= :maxRent)")
	List<RoomMaster> searchRooms(@Param("roomType") String roomType, @Param("minRent") Integer minRent,
			@Param("maxRent") Integer maxRent);

	@Query("SELECT r FROM RoomMaster r WHERE r.currentCapacity < r.maxCapacity")
	List<RoomMaster> findAvailableRooms();
	
	@Query("SELECT r FROM RoomMaster r WHERE r.currentCapacity < r.maxCapacity AND r.addressDetails LIKE %:location%")
    List<RoomMaster> findAvailableRoomsByLocation(@Param("location") String location);
}
