package com.oam.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Statistics {
    private List<String> labels;
    private List<Integer> data;
}
