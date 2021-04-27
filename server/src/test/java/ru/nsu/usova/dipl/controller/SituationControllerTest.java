package ru.nsu.usova.dipl.controller;

import com.fasterxml.jackson.databind.JsonNode;
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
import ru.nsu.usova.dipl.situation.model.Situation;

import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class SituationControllerTest {
    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void convertToSituation() throws Exception {
        String phrase = "Врач рекомендует фрукты с белыми прожилками.";
        String res = mockMvc.perform(
                MockMvcRequestBuilders.get("/situation")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(phrase))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assert.assertEquals(res, "[{\"questionsList\":[],\"childSituations\":[{\"questionsList\":[{\"key\":\"Основное действие\",\"value\":\"рекомендовать\"},{\"key\":\"objectRole\",\"value\":\"врач\"},{\"key\":\"для чего\",\"value\":\"для чего рекомендовать\"},{\"key\":\"в кого\",\"value\":\"фрукт\"},{\"key\":\"к чему\",\"value\":\"к чему рекомендовать\"},{\"key\":\"на что\",\"value\":\"фрукт\"}],\"childSituations\":[]},{\"questionsList\":[{\"key\":\"Основное действие\",\"value\":\"являться\"},{\"key\":\"objectRole\",\"value\":\"прожилка\"},{\"key\":\"каким\",\"value\":\"белый\"}],\"childSituations\":[]}]}]");

        String s = "{\"questionsList\":[],\"childSituations\":[{\"questionsList\":[{\"key\":\"Основное действие\",\"value\":\"рекомендовать\"},{\"key\":\"objectRole\",\"value\":\"врач\"},{\"key\":\"для чего\",\"value\":\"для чего рекомендовать\"},{\"key\":\"в кого\",\"value\":\"фрукт\"},{\"key\":\"к чему\",\"value\":\"к чему рекомендовать\"},{\"key\":\"на что\",\"value\":\"фрукт\"}],\"childSituations\":[]},{\"questionsList\":[{\"key\":\"Основное действие\",\"value\":\"являться\"},{\"key\":\"objectRole\",\"value\":\"прожилка\"},{\"key\":\"каким\",\"value\":\"белый\"}],\"childSituations\":[]}]}";
    }
}
