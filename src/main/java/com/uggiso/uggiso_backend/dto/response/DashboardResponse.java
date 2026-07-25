package com.uggiso.uggiso_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private Long totalOrders;

    private Long pendingOrders;

    private Long completedOrders;

    private BigDecimal totalRevenue;

}