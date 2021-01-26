package ru.nsu.usova.dipl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonDeserialize(keyUsing = SituationLinkDeserializer.class)
public class SituationLink {
    private Long id;

    private Situation premiseSituation;

    private Situation resultSituation;

    private String premise;

    private String result;
}
