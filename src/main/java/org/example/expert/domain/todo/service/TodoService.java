package org.example.expert.domain.todo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.request.getTodosRequestDto;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoQueryRepository;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;
    private final TodoQueryRepository todoQueryRepository;
    @Transactional
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail())
        );
    }

    public List<TodoResponse> getTodos(int page, int size, getTodosRequestDto requestDto) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<TodoResponse> todos = todosCheck(pageable,requestDto);

        return todos;
    }
    public List<TodoResponse> todosCheck(Pageable pageable,getTodosRequestDto requestDto) {
        /*
        기간의 시작과 끝이 둘다 있을때
        기간시작일이 끝보다 느릴수 없고
        기간끝나는일이 시작일보다 빠를 수는 없다.
        */
        if(requestDto.getEndDate()!=null&&requestDto.getStartDate()!=null) {
            if(requestDto.getStartDate().isAfter(requestDto.getEndDate())) {
                throw new InvalidRequestException("Start date cannot be after end date");
            }
            if(requestDto.getEndDate().isBefore(requestDto.getStartDate())) {
                throw new InvalidRequestException("End date cannot be before start date");
            }
            if(requestDto.getStartDate().equals(requestDto.getEndDate())) {
                throw new InvalidRequestException("start date cannot equal end date");
            }
        }
        return todoQueryRepository.findAll(
                requestDto.getStartDate()
                ,requestDto.getEndDate()
        , requestDto.getWeather(), pageable);
    }

    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }
}
