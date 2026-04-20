package com.minibytes.grid.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCommentRequest {
    private Long authorId;
    private String authorType;
    private String content;
    private int depthLevel;
}
