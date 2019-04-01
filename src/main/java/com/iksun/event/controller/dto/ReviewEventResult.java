package com.iksun.event.controller.dto;

import com.iksun.event.domain.ReviewEvent;
import lombok.Data;

@Data
public class ReviewEventResult {
    String success;
    ReviewEvent reviewEvent;
}
