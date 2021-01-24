package ru.nsu.usova.dipl.situation;

import lombok.Data;
import org.springframework.context.annotation.ComponentScan;
import ru.nsu.usova.dipl.JavafxConfig;

import javax.persistence.*;

@Data
@Entity
@Table(name = "reasoning")
@ComponentScan(basePackageClasses = Situation.class)
public class SituationLink {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="premise_situation_id", nullable=false)
    protected Situation premiseSituation = new Situation();

    @ManyToOne
    @JoinColumn(name="result_situation_id", nullable=false)
    protected Situation resultSituation = new Situation();
}
