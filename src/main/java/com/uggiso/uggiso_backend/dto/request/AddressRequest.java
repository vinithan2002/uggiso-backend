package com.uggiso.uggiso_backend.dto.request;

import com.uggiso.uggiso_backend.entity.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    @NotBlank(message = "House No is required")
    private String houseNo;

    @NotBlank(message = "Street is required")
    private String street;

    private String landmark;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pincode is required")
    private String pincode;

    @NotNull(message = "Address Type is required")
    private AddressType addressType;

    private Boolean defaultAddress;

    @NotNull(message = "User Id is required")
    private Long userId;
}