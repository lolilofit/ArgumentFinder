package ru.nsu.usova.dipl.javafx.scenario.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.usova.dipl.javafx.situation.model.SituationLink;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkMetric {
    private Float metric;
    private String samePartRelationType;
    private String structuralRelationType;
    private SituationLink link;
}
