package com.minibytes.grid.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePostLikeRequest {
    private Long userId;
}
