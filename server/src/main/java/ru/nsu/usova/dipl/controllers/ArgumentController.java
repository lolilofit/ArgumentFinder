package ru.nsu.usova.dipl.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.nsu.usova.dipl.controllers.model.LoadTextInfo;
import ru.nsu.usova.dipl.controllers.model.ReasononingRequest;
import ru.nsu.usova.dipl.scenario.ArgumentExtractorService;
import ru.nsu.usova.dipl.scenario.SituationMining;
import ru.nsu.usova.dipl.scenario.model.LinkMetric;
import ru.nsu.usova.dipl.situation.model.Situation;
import ru.nsu.usova.dipl.situation.model.SituationLink;
import ru.nsu.usova.dipl.situation.repository.SituationLinkRepository;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/argument")
@RequiredArgsConstructor
@Data
@Slf4j
public class ArgumentController {
    private final SituationLinkRepository situationLinkRepository;

    private final ArgumentExtractorService argumentExtractorService;

    private final SituationMining situationMining;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(path = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<SituationLink> getAllReasoning() {
        log.info("get all arguments");
        return situationLinkRepository.getAllByIdIsGreaterThan(0L);
    }

    @RequestMapping(path = "/statement", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<LinkMetric> getReasoningByStatement(@RequestBody ReasononingRequest request) {
        log.info(String.format("get arguments by statement {%s}", request.getStatement()));
        try {
            Situation s = argumentExtractorService.getSituationFromStatement(request.getStatement());
            return argumentExtractorService.findArgumentation(s, 0.01F);
        } catch (IOException | InterruptedException e) {
            return List.of();
        }
    }

    @RequestMapping(path = "/load", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public LoadTextInfo loadText(@RequestBody ReasononingRequest request) {
        log.info("load text");
        return situationMining.extractSituationsByText(request.getStatement());
    }
}
