package ru.nsu.usova.dipl.situation.db.repository;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.usova.dipl.JavafxConfig;
import ru.nsu.usova.dipl.situation.SituationLink;

import java.util.List;

@Repository
@ComponentScan(basePackageClasses = JavafxConfig.class)
public interface SituationLinkRepository extends CrudRepository<SituationLink, Long>, BatchRepository<SituationLink> {
    @Override
    @Query("select COALESCE(max(s.id), 0) from SituationLink s")
    Long getMaxId();

    @Override
    @Query("select s from SituationLink s where s.id > :min_id - 1 AND s.id < :max_id")
    List<SituationLink> getAllByMinMaxId(@Param("min_id") Integer minId, @Param("max_id") Long maxId);
}
