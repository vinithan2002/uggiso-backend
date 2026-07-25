package com.uggiso.uggiso_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentOrderResponse {

    private String customerName;

    private String orderNumber;

    private String status;

    private BigDecimal amount;

}