package com.uggiso.uggiso_backend.dto.response;

import com.uggiso.uggiso_backend.entity.AddressType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {

    private Long id;

    private String houseNo;

    private String street;

    private String landmark;

    private String city;

    private String state;

    private String pincode;

    private AddressType addressType;

    private Boolean defaultAddress;

    private Long userId;

    private String userName;
}