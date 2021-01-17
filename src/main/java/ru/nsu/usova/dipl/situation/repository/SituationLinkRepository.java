package ru.nsu.usova.dipl.situation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.usova.dipl.situation.SituationLink;

@Repository
public interface SituationLinkRepository extends CrudRepository<SituationLink, Long> {
}
