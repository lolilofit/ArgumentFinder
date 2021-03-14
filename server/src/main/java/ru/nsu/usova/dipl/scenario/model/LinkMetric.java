package ru.nsu.usova.dipl.scenario.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.usova.dipl.situation.model.SituationLink;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkMetric {
    private Float metric;
    private SituationLink link;
}
