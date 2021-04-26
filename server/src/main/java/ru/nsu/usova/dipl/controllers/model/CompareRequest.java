package ru.nsu.usova.dipl.controllers.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompareRequest {
    String firstPhrase;
    String secondPhrase;
}
