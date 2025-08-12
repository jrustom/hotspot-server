package com.hotspot.dto.HotspotDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HotspotRequestDto {
	private final Double lng;
	private final Double lat;
}


