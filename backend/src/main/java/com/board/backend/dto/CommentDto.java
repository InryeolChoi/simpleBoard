package com.board.backend.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private String authorUsername;
    private LocalDateTime createdAt;
}
