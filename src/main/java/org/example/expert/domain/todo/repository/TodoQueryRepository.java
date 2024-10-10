package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.searchTodosResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@AllArgsConstructor
public class TodoQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QTodo todo = QTodo.todo;
    private final QUser user = QUser.user;
    private final QComment comments = QComment.comment;
    private final QManager managers = QManager.manager;

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
    /*
    - 새로운 API로 만들어주세요.
    - 검색 조건은 다음과 같아요.
    - 검색 키워드로 일정의 제목을 검색할 수 있어요.
        - 제목은 부분적으로 일치해도 검색이 가능해요.
    - 일정의 생성일 범위로 검색할 수 있어요.
        - 일정을 생성일 최신순으로 정렬해주세요.
    - 담당자의 닉네임으로도 검색이 가능해요.
        - 닉네임은 부분적으로 일치해도 검색이 가능해요.
    - 다음의 내용을 포함해서 검색 결과를 반환해주세요.
    - 일정에 대한 모든 정보가 아닌, 제목만 넣어주세요.
    - 해당 일정의 담당자 수를 넣어주세요.
    - 해당 일정의 총 댓글 개수를 넣어주세요.
    - 검색 결과는 페이징 처리되어 반환되도록 합니다.
     */
    public List<searchTodosResponse> findSearchTodo(String title, LocalDateTime createdAt, String nickname, Pageable pageable) {
        return queryFactory.select(
                Projections.constructor(
                        searchTodosResponse.class,
                        todo.id,
                        todo.title,
                        todo.managers.size(),
                        todo.comments.size()
                )).
                from(todo)
                .leftJoin(todo.comments,comments)
                .leftJoin(todo.managers,managers)
                .where(
                        title(title)
                        ,createdAt(createdAt)
                        ,nickname(nickname)
                ).groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
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
    private BooleanExpression title(String title) {
        return title !=null ? todo.title.contains(title) : null;
    }
    private BooleanExpression createdAt(LocalDateTime createdAt) {
        return createdAt !=null ? todo.createdAt.goe(createdAt) : null;
    }
    private BooleanExpression nickname(String nickname) {
        return nickname !=null ? managers.user.nickname.contains(nickname) : null;
    }
}
