package com.hotspot.dto.AccountDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountUpdatePassDto {
    private final String oldPass;
    private final String newPass;

}
