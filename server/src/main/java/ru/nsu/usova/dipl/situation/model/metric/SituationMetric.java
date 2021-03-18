package ru.nsu.usova.dipl.situation.model.metric;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SituationMetric {
    private float distance;
    private SamePartRelationType samePartRelationType;
    private StructuralRelationType structuralRelationType;
}
