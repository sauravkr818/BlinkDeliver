package com.saurav.BlinkDeliver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RazorPayCreateOrderDto {

    private String razorpayOrderId;
    private String currency;
    private Integer amount;
    private String key;
}
