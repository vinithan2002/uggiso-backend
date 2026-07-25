package com.uggiso.uggiso_backend.dto.request;

import com.uggiso.uggiso_backend.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotNull(message = "User Id is required")
    private Long userId;

    @NotNull(message = "Address Id is required")
    private Long addressId;

    @NotNull(message = "Payment Method is required")
    private PaymentMethod paymentMethod;

    private String couponCode;

}