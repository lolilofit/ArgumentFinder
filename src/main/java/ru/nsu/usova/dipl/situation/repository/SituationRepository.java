package ru.nsu.usova.dipl.situation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.usova.dipl.situation.Situation;

@Repository
public interface SituationRepository extends JpaRepository<Situation, Long> {
}
