package com.app.rent_manager.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private Long roomId;
    private String roomName;
    private String roomType;
    private Integer currentCapacity;
    private Integer maxCapacity;
    private Double roomRent;
}