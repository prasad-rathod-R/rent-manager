package com.app.rent_manager.Entity;



import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {


    private String CreatedBy;
    private LocalDateTime createdDate;
    private String updatedBy;
    private LocalDateTime UpdatedDate;

}
