package ru.nsu.usova.dipl.situation.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.usova.dipl.situation.Situation;

import java.util.List;
import java.util.Optional;

@Repository
public interface SituationRepository extends JpaRepository<Situation, Long> {
    @Query("select COALESCE(max(s.id), 0) from Situation s")
    Long getMaxId();

    @Query("select s.* from Situation s where s.id > :min_id - 1 AND s.id < :max_id")
    List<Situation> getAllByMinMaxId(@Param("min_id") Long minId, @Param("max_id") Long maxId);
}
