package ru.nsu.usova.dipl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SituationMetric {
    private float distance;
    private String samePartRelationType;
    private String structuralRelationType;
}
