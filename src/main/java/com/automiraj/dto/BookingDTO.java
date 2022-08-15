package com.automiraj.dto;

import com.automiraj.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDTO {

    private String id;
    private SlotDTO slotDTO;
    private CustomerDTO customerDTO;
    private CommonStatus commonStatus;
}
