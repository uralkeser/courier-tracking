package com.migros_one.courier_tracking.courier.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class CourierDTO {

    @JsonProperty("time")
    private String time;

    @JsonProperty("courierId")
    private int courierId;

    @JsonProperty("lat")
    private double latitude;

    @JsonProperty("lng")
    private double longitude;

    @Override
    public String toString() {
        return "CourierDTO{" +
                "time=" + time +
                ", courierId=" + courierId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
