package com.app.rent_manager.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.rent_manager.Entity.TenantMaster;

@Repository
public interface TenantRepository extends JpaRepository<TenantMaster, Long> {

	@Query("SELECT t FROM TenantMaster t WHERE t.roomId = :roomId")
	List<TenantMaster> findByRoomId(@Param("roomId") Long roomId);
	
    TenantMaster findByAdharNumber(String adharNumber);

	boolean existsByAdharNumber(String adharNumber);

}
