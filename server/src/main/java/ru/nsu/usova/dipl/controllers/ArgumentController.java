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
import ru.nsu.usova.dipl.controllers.model.CompareRequest;
import ru.nsu.usova.dipl.controllers.model.LoadTextInfo;
import ru.nsu.usova.dipl.controllers.model.ReasononingRequest;
import ru.nsu.usova.dipl.scenario.ArgumentExtractorService;
import ru.nsu.usova.dipl.scenario.SituationMining;
import ru.nsu.usova.dipl.scenario.model.LinkMetric;
import ru.nsu.usova.dipl.situation.model.Situation;
import ru.nsu.usova.dipl.situation.model.SituationLink;
import ru.nsu.usova.dipl.situation.model.metric.SituationMetric;
import ru.nsu.usova.dipl.situation.repository.SituationLinkRepository;
import ru.nsu.usova.dipl.situation.service.SituationCompareService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private final SituationCompareService situationCompareService;

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
    @RequestMapping(path = "/situation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Situation> getSituation(@RequestBody String text) {
        return Arrays.stream(text.split("\\.")).map(t -> {
            try {
                return argumentExtractorService.getSituationFromStatement(t);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @RequestMapping(path = "/situation/compare", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SituationMetric compareSituations(@RequestBody CompareRequest compareRequest) throws IOException, InterruptedException {
        if(compareRequest.getFirstPhrase().split("\\.").length != 1 || compareRequest.getSecondPhrase().split("\\.").length != 1)
            throw new RuntimeException("Каждый текст должен представлять собой только одно предложение");

        Situation s1 = argumentExtractorService.getSituationFromStatement(compareRequest.getFirstPhrase());
        Situation s2 = argumentExtractorService.getSituationFromStatement(compareRequest.getSecondPhrase());

        return situationCompareService.compare(s1, s2);
    }
}
