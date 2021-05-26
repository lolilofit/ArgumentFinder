package ru.nsu.usova.dipl.javafx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Ignore;
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
import ru.nsu.usova.dipl.javafx.Main;

import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@Ignore
public class SituationControllerTest {
    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void convertToSituation() throws Exception {
        String phrase = "Врач рекомендует фрукты с белыми прожилками.";
        String res = mockMvc.perform(
                MockMvcRequestBuilders.post("/situation")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(phrase))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        Assert.assertEquals(res, "[{\"questionsList\":[],\"childSituations\":[{\"questionsList\":[{\"questionKey\":\"Основное действие\",\"questionValue\":\"рекомендовать\"},{\"questionKey\":\"objectRole\",\"questionValue\":\"врач\"},{\"questionKey\":\"для чего\",\"questionValue\":\"для чего рекомендовать\"},{\"questionKey\":\"в кого\",\"questionValue\":\"фрукт\"},{\"questionKey\":\"к чему\",\"questionValue\":\"к чему рекомендовать\"},{\"questionKey\":\"на что\",\"questionValue\":\"фрукт\"}],\"childSituations\":[]},{\"questionsList\":[{\"questionKey\":\"Основное действие\",\"questionValue\":\"являться\"},{\"questionKey\":\"objectRole\",\"questionValue\":\"прожилка\"},{\"questionKey\":\"каким\",\"questionValue\":\"белый\"}],\"childSituations\":[]}]}]");

    }
}
