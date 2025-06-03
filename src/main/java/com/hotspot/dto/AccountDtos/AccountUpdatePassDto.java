package com.hotspot.dto.AccountDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountUpdatePassDto {
    @NotBlank(message = "The old password must not be blank")
    private final String oldPass;
    @NotBlank(message = "The new password must noe be blank")
    private final String newPass;

}
