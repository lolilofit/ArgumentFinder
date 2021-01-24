package ru.nsu.usova.dipl.view.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
@Data
public class MainViewController {
    //private final SituationMining situationMining;

    public void searchForArguments() {
        System.out.println("FIND");
    }
}
