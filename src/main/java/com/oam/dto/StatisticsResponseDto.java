package com.oam.dto;

import java.util.List;

public record StatisticsResponseDto(List<String> labels, List<Integer> data) {
}
