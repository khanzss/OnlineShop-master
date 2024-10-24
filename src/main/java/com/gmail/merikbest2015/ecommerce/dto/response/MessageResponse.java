package com.gmail.merikbest2015.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String response;
    private String message;

    public MessageResponse(String response, String message) {
        this.response = response;
        this.message = message;
    }
}
