package org.example.expert.domain.todo.service;

import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoQueryRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class todoserviceTest {
    @InjectMocks
    private TodoService todoService;
    @Mock
    private TodoQueryRepository todoQueryRepository;

    @BeforeAll
    public static void setUp() {
        User user1 = new User("email","password", UserRole.ROLE_USER);
        Todo todo1 = new Todo("title","contents","snowy",user1);

        User user2 = new User("email","password", UserRole.ROLE_USER);
        Todo todo2 = new Todo("title","contents","snowy",user2);

        User user3 = new User("email","password", UserRole.ROLE_USER);
        Todo todo3 = new Todo("title","contents","snowy",user3);

        User user4 = new User("email","password", UserRole.ROLE_USER);
        Todo todo4 = new Todo("title","contents","snowy",user4);

        User user5 = new User("email","password", UserRole.ROLE_USER);
        Todo todo5 = new Todo("title","contents","snowy",user5);
    }

    @Test
    public void searchTodosTest() {

    }
}
