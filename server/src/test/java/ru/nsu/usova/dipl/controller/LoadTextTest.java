package ru.nsu.usova.dipl.controller;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
import ru.nsu.usova.dipl.controllers.model.LoadTextInfo;
import ru.nsu.usova.dipl.controllers.model.ReasononingRequest;
import ru.nsu.usova.dipl.situation.model.Situation;
import ru.nsu.usova.dipl.situation.model.SituationLink;
import ru.nsu.usova.dipl.situation.repository.SituationLinkRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class LoadTextTest {
    private final static String text = "Бурляева подчеркнула, что мандарины содержат витамин С, витамины группы В, витамин А, каротин, кальций, магний и фосфор. Бурляева посоветовала есть их с белыми прожилками, так как в них содержится клетчатка.\n" +
            "\n" +
            "Однако не стоит есть мандарины на голодный желудок, а людям с обострением болезней желудочно-кишечного тракта, а также с аллергическими реакциями, они и вовсе противопоказаны. Также врач рассказала, что аллергическая реакция возникает на масла в кожице мандарина. Поэтому, если человеку предложить уже очищенный мандарин, части аллергических реакций можно будет избежать.\n" +
            "Также врач рассказала, что аллергический ответ возникает на масла в кожице мандарина. Поэтому, если человеку предложить уже очищенный мандарин, части аллергических реакций можно будет избежать.\n" +
            "\n" +
            "Ранее российские врач-диетолог Инна Заикина предупредила о скрытой опасности чеснока. Диетолог отметила, что чеснок в большом количестве нельзя употреблять людям с обострением язвенной болезни желудка, панкреатита, желчнокаменной болезни.\n" +
            "\n" +
            "«Четыре мандарина», — ответила Дзгоева на вопрос, сколько мандаринов можно съедать в день.\n" +
            "\n" +
            "В свою очередь врач-терапевт, диетолог, заведующая консультативно-диагностическим центром «Здоровое и спортивное питание» ФИЦ питания и биотехнологии, кандидат медицинских наук Екатерина Бурляева отметила, что мандарины имеют небольшую калорийность. По ее словам, их можно рекомендовать людям, следящим за фигурой.\n";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SituationLinkRepository situationLinkRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void loadText() throws Exception {
        situationLinkRepository.deleteAll();
        int situationLinksPreLoadSize = situationLinkRepository.getAllByIdIsGreaterThan(0L).size();

        String allArgsStr = mockMvc.perform(
                MockMvcRequestBuilders.get("/argument/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        TreeNode treeNode = objectMapper.readTree(allArgsStr);

        Assert.assertEquals(0, treeNode.size());
        Assert.assertEquals(0, situationLinksPreLoadSize);

        String res = mockMvc.perform(
                MockMvcRequestBuilders.post("/text/load")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ReasononingRequest(text))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        LoadTextInfo loadTextInfo = objectMapper.readValue(res, LoadTextInfo.class);

        Assert.assertEquals(3, (int) loadTextInfo.getExtractedArguments());

        List<SituationLink> situationLinksAfterLoad = situationLinkRepository.getAllByIdIsGreaterThan(0L);

        Assert.assertEquals(situationLinksAfterLoad.size() - situationLinksPreLoadSize, (int) loadTextInfo.getExtractedArguments());

        allArgsStr = mockMvc.perform(
                MockMvcRequestBuilders.get("/argument/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        treeNode = objectMapper.readTree(allArgsStr);

        Assert.assertEquals(treeNode.size(), 3);

        TreeNode argument = treeNode.get(0);
        Assert.assertEquals(argument.get("premise").toString(), "\"в них содержится клетчатка\"");
        Assert.assertEquals(argument.get("result").toString(), "\"бурляева посоветовала есть их с белыми прожилками\"");

        argument = treeNode.get(1);
        Assert.assertEquals(argument.get("premise").toString(), "\" Также врач рассказала, что аллергическая реакция возникает на масла в кожице мандарина\"");
        Assert.assertEquals(argument.get("result").toString(), "\"если человеку предложить уже очищенный мандарин, части аллергических реакций можно будет избежать\"");

        argument = treeNode.get(2);
        Assert.assertEquals(argument.get("premise").toString(), "\"Также врач рассказала, что аллергический ответ возникает на масла в кожице мандарина\"");
        Assert.assertEquals(argument.get("result").toString(), "\"если человеку предложить уже очищенный мандарин, части аллергических реакций можно будет избежать\"");
    }
}
