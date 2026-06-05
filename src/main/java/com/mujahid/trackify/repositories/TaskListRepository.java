package com.mujahid.trackify.repositories;

import com.mujahid.trackify.domain.entities.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, UUID> {
    List<TaskList> findByUserId(UUID userId);

    Optional<TaskList> findByIdAndUserId(UUID taskListId, UUID userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM TaskList tl WHERE tl.id = :id AND tl.user.id = :userId")
    int deleteByIdAndUserId(@Param("id") UUID taskListId, @Param("userId") UUID userId);

}
