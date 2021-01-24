package ru.nsu.usova.dipl.situation.view;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import ru.nsu.usova.dipl.situation.Situation;
import ru.nsu.usova.dipl.situation.scenario.SituationMining;

@Component
@Data
@ComponentScan(basePackageClasses = Situation.class)
public class MainViewController {
    @Autowired
    private SituationMining situationMining;

    public void searchForArguments() {
        System.out.println("FIND");
    }
}
