package org.example.expert.domain.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class searchTodosRequestDto {
    private String title;
    private LocalDateTime createdAt;
    private String nickname;

}
