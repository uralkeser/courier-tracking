package com.migros_one.courier_tracking.courier.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourierDTO {

    private Long id;
    private String fullName;
    private String identityNumber;
}
