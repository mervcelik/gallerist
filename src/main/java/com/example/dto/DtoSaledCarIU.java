package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoSaledCarIU {

    @NotNull
    private Long galleristId;

    @NotNull
    private Long carId;

    @NotNull
    private Long customerId;
}
