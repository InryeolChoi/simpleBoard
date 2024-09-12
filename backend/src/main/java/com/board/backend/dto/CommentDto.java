package com.board.backend.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private String authorUsername;
    private LocalDateTime createdAt;
}
