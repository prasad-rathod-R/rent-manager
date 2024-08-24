package com.app.rent_manager.mappers;

import com.app.rent_manager.Entity.RoomMaster;
import com.app.rent_manager.ResponseBody.RoomDTO;

public class RoomMapper {
    public static RoomDTO toRoomDTO(RoomMaster roomMaster) {
        RoomDTO dto = new RoomDTO();
        dto.setRoomId(roomMaster.getRoomId());
        dto.setRoomName(roomMaster.getRoomName());
        dto.setRoomType(roomMaster.getRoomType());
        dto.setCurrentCapacity(roomMaster.getCurrentCapacity());
        dto.setMaxCapacity(roomMaster.getMaxCapacity());
        dto.setRoomRent(Double.valueOf(roomMaster.getRoomRent()));
        return dto;
    }
}