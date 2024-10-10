package org.example.expert.domain.todo.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class searchTodosResponse {
    private final Long id;
    private final String title;
    private final Integer managersCount;
    private final Integer commentsCount;
    @QueryProjection
    public searchTodosResponse(Long id,String title,Integer managersCount,Integer commentsCount) {
        this.id = id;
        this.title = title;
        this.managersCount = managersCount;
        this.commentsCount = commentsCount;
    }
}
