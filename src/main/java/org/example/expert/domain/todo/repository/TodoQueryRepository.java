package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
@AllArgsConstructor
public class TodoQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QTodo todo=QTodo.todo;
    private final QUser user=QUser.user;

    public List<TodoResponse> findAll(LocalDateTime startDate, LocalDateTime endDate, String weather,Pageable pageable) {
        return queryFactory.select(
                Projections.constructor(
                        TodoResponse.class,
                        todo.id,
                        todo.title,
                        todo.contents,
                        todo.weather,
                        Projections.constructor(UserResponse.class,user.id,user.email),
                        todo.createdAt,
                        todo.modifiedAt
                )
                )
                .from(todo).
                leftJoin(todo.user,user)
                .where(
                    startDate(startDate),
                        endDate(endDate),
                        weather(weather)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression startDate(LocalDateTime startDate) {
        return startDate !=null ? todo.modifiedAt.goe(startDate) : null;
    }
    private BooleanExpression endDate(LocalDateTime endDate) {
        return endDate !=null ? todo.modifiedAt.loe(endDate) : null;
    }
    private BooleanExpression weather(String weather) {
        return weather !=null ? todo.weather.eq(weather) : null;
    }
}
