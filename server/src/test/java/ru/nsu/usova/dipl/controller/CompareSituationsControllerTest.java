package ru.nsu.usova.dipl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.nsu.usova.dipl.Main;
import ru.nsu.usova.dipl.controllers.model.CompareRequest;
import ru.nsu.usova.dipl.situation.model.metric.SamePartRelationType;
import ru.nsu.usova.dipl.situation.model.metric.SituationMetric;
import ru.nsu.usova.dipl.situation.model.metric.StructuralRelationType;

import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class CompareSituationsControllerTest {
    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void compareSituationsEquals() throws Exception {
        String phrase = "Врач рекомендует фрукты с белыми прожилками.";
        String phraseSimilar = "Врач рекомендует фрукты с белыми прожилками.";

        String res = mockMvc.perform(
                MockMvcRequestBuilders.get("/situation/compare")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CompareRequest(phrase, phraseSimilar))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        SituationMetric metric = mapper.readValue(res, SituationMetric.class);

        Assert.assertTrue(metric.getDistance() == 1.0);
        Assert.assertEquals(metric.getSamePartRelationType(), SamePartRelationType.EQUAL);
        Assert.assertEquals(metric.getStructuralRelationType(), StructuralRelationType.SAME);
    }

    @Test
    public void compareSituationsSimilar() throws Exception {
        String phrase = "Врач рекомендует фрукты с белыми прожилками.";
        String phraseSimilar = "Доктор рекомендует фрукты с белыми прожилками.";

        String res = mockMvc.perform(
                MockMvcRequestBuilders.get("/situation/compare")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CompareRequest(phrase, phraseSimilar))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        SituationMetric metric = mapper.readValue(res, SituationMetric.class);

        Assert.assertTrue(metric.getDistance() == 1.0);
        Assert.assertEquals(metric.getSamePartRelationType(), SamePartRelationType.SIMILAR);
        Assert.assertEquals(metric.getStructuralRelationType(), StructuralRelationType.SAME);
    }

    @Test
    public void compareSituationsGeneral() throws Exception {
        String phrase = "Врач рекомендует фрукты с белыми прожилками.";
        String phraseSimilar = "Эксперт рекомендует фрукты с бесцветными прожилками.";

        String res = mockMvc.perform(
                MockMvcRequestBuilders.get("/situation/compare")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CompareRequest(phrase, phraseSimilar))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        SituationMetric metric = mapper.readValue(res, SituationMetric.class);

        Assert.assertTrue(metric.getDistance() == 1.0f);
        Assert.assertEquals(metric.getSamePartRelationType(), SamePartRelationType.GENERALIZATION);
        Assert.assertEquals(metric.getStructuralRelationType(), StructuralRelationType.SAME);
    }

    @Test
    public void compareSituationsInclude() throws Exception {
        String phrase = "Врач рекомендует фрукты с белыми прожилками, особенно спелые мандарины.";
        String phraseSimilar = "Врач рекомендует фрукты с белыми прожилками.";

        String res = mockMvc.perform(
                MockMvcRequestBuilders.get("/situation/compare")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CompareRequest(phrase, phraseSimilar))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        SituationMetric metric = mapper.readValue(res, SituationMetric.class);

        Assert.assertTrue(metric.getDistance() == 0.8333334f);
        Assert.assertEquals(metric.getSamePartRelationType(), SamePartRelationType.EQUAL);
        Assert.assertEquals(metric.getStructuralRelationType(), StructuralRelationType.INCLUDE);
    }

    @Test
    public void compareSituationsIncludeSimilar() throws Exception {
        String phrase = "Врач рекомендует фрукты с белыми прожилками, особенно спелые мандарины.";
        String phraseSimilar = "Доктор рекомендует фрукты с белыми прожилками.";

        String res = mockMvc.perform(
                MockMvcRequestBuilders.get("/situation/compare")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CompareRequest(phrase, phraseSimilar))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        SituationMetric metric = mapper.readValue(res, SituationMetric.class);

        Assert.assertTrue(metric.getDistance() == 0.8333334f);
        Assert.assertEquals(metric.getSamePartRelationType(), SamePartRelationType.SIMILAR);
        Assert.assertEquals(metric.getStructuralRelationType(), StructuralRelationType.INCLUDE);
    }

    @Test
    public void compareSituationsIncludeGeneralization() throws Exception {
        String phrase = "Врач рекомендует фрукты с белыми прожилками, особенно спелые мандарины.";
        String phraseSimilar = "Эксперт рекомендует фрукты с белыми прожилками.";

        String res = mockMvc.perform(
                MockMvcRequestBuilders.get("/situation/compare")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CompareRequest(phrase, phraseSimilar))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        SituationMetric metric = mapper.readValue(res, SituationMetric.class);

        Assert.assertTrue(metric.getDistance() == 0.8333334f);
        Assert.assertEquals(metric.getSamePartRelationType(), SamePartRelationType.GENERALIZATION);
        Assert.assertEquals(metric.getStructuralRelationType(), StructuralRelationType.INCLUDE);
    }

    @Test
    public void compareSituationsIntersectionSimilar() throws Exception {
        String phrase = "Доктор рекомендует фрукты с белыми прожилками, особенно спелые мандарины.";
        String phraseSimilar = "Врач рекомендует фрукты с белыми прожилками, врач любит фрукты.";

        String res = mockMvc.perform(
                MockMvcRequestBuilders.get("/situation/compare")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CompareRequest(phrase, phraseSimilar))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        SituationMetric metric = mapper.readValue(res, SituationMetric.class);

        Assert.assertTrue(metric.getDistance() == 0.6666667f);
        Assert.assertEquals(metric.getSamePartRelationType(), SamePartRelationType.SIMILAR);
        Assert.assertEquals(metric.getStructuralRelationType(), StructuralRelationType.INTERSECTION);
    }

    @Test
    public void compareSituationsIntersectionGeneralization() throws Exception {
        String phrase = "Эксперт рекомендует фрукты с белыми прожилками, особенно спелые мандарины.";
        String phraseSimilar = "Врач рекомендует фрукты с белыми прожилками, врач любит фрукты.";

        String res = mockMvc.perform(
                MockMvcRequestBuilders.get("/situation/compare")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CompareRequest(phrase, phraseSimilar))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        SituationMetric metric = mapper.readValue(res, SituationMetric.class);

        Assert.assertTrue(metric.getDistance() == 0.6666667f);
        Assert.assertEquals(metric.getSamePartRelationType(), SamePartRelationType.GENERALIZATION);
        Assert.assertEquals(metric.getStructuralRelationType(), StructuralRelationType.INTERSECTION);
    }

    @Test
    public void compareSituationsIntersectionEquals() throws Exception {
        String phrase = "Врач рекомендует фрукты с белыми прожилками, особенно спелые мандарины.";
        String phraseSimilar = "Врач рекомендует фрукты с белыми прожилками, врач любит фрукты.";

        String res = mockMvc.perform(
                MockMvcRequestBuilders.get("/situation/compare")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new CompareRequest(phrase, phraseSimilar))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        SituationMetric metric = mapper.readValue(res, SituationMetric.class);

        Assert.assertTrue(metric.getDistance() == 0.6666667f);
        Assert.assertEquals(metric.getSamePartRelationType(), SamePartRelationType.EQUAL);
        Assert.assertEquals(metric.getStructuralRelationType(), StructuralRelationType.INTERSECTION);
    }
}
