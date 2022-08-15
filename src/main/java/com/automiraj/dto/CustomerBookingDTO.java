package com.automiraj.dto;

import com.automiraj.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerBookingDTO {

    private String id;
    private CustomerDTO customerDTO;
    private BookingDTO bookingDTO;
    private CommonStatus commonStatus;
}
