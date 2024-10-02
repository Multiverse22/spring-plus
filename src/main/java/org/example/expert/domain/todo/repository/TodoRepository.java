package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {


    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query("SELECT t from Todo t LEFT JOIN FETCH t.user u " +
            "WHERE (:weather is null or t.weather = :weather)" +
            "and (:startDate is null or t.modifiedAt>=:startDate)" +
            "and (:endDate is null or t.modifiedAt<=:endDate)" +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByWeatherAndStartDateAndEndDateOrderByModifiedAtDesc(Pageable pageable,
                                                                           @Param("startDate") LocalDateTime startDate,
                                                                           @Param("endDate") LocalDateTime endDate,
                                                                           @Param("weather") String weather);
    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
}
