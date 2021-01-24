package ru.nsu.usova.dipl.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.nsu.usova.dipl.controllers.model.ReasononingRequest;
import ru.nsu.usova.dipl.scenario.ArgumentExtractorService;
import ru.nsu.usova.dipl.situation.Situation;
import ru.nsu.usova.dipl.situation.SituationLink;
import ru.nsu.usova.dipl.situation.db.repository.SituationLinkRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/argument")
@RequiredArgsConstructor
@Data
@Slf4j
public class ArgumentController {
    private final SituationLinkRepository situationLinkRepository;

    private final ArgumentExtractorService argumentExtractorService;

    @RequestMapping(path = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SituationLink> getAllReasoning() {
        log.info("get all arguments");
        return situationLinkRepository.getAllByIdIsGreaterThan(0L);
    }

    @RequestMapping(path = "/statement", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Pair<Float, SituationLink>> getReasoningByStatement(@RequestBody ReasononingRequest request) {
        log.info(String.format("get arguments by statement {%s}", request.getStatement()));
        try {
            Situation s = argumentExtractorService.getSituationFromStatement(request.getStatement());
            s.print();
            return argumentExtractorService.findArgumentation(s, 0.01F);
        } catch (IOException | InterruptedException e) {
            return List.of();
        }
    }
}
