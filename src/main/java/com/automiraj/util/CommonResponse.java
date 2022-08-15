package com.automiraj.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CommonResponse {

    private boolean status = false;
    private List<String> errorMessages = new ArrayList<>();
    private List<Object> payload = null;

    public CommonResponse (boolean status, List<String> errorMessages){
        this.status=status;
        this.errorMessages=errorMessages;
    }

    public CommonResponse(boolean status){
        this.status=status;
    }
}
