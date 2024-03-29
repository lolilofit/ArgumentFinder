package ru.nsu.usova.dipl.javafx.situation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.usova.dipl.javafx.situation.model.Situation;

import java.util.List;

@Repository
public interface SituationRepository extends JpaRepository<Situation, Long>, BatchRepository<Situation> {
    @Override
    @Query("select COALESCE(max(s.id), 0) from Situation s")
    Long getMaxId();

    @Override
    @Query("select s from Situation s where s.id > :min_id - 1 AND s.id < :max_id")
    List<Situation> getAllByMinMaxId(@Param("min_id") Integer minId, @Param("max_id") Long maxId);
}
