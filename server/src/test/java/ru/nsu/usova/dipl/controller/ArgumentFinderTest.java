package ru.nsu.usova.dipl.controller;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.util.Assert;
import ru.nsu.usova.dipl.Main;
import ru.nsu.usova.dipl.controllers.model.ReasononingRequest;

import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class ArgumentFinderTest {
    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void findArgumentationForEqualInclude() throws Exception {
        String res = mockMvc.perform(
                MockMvcRequestBuilders.post("/argument/statement")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new ReasononingRequest("Бурляева посоветовала есть их с белыми прожилками, врачи совертуют овощи."))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        TreeNode treeNode = mapper.readTree(res);

        boolean flag = false;
        for (int i = 0; i < treeNode.size(); i++) {
            TreeNode t = treeNode.get(i);
            TreeNode l = t.get("link").get("result");
            if (t.get("metric").toString().equals("0.8333334")
                    && l.toString().equals("\"бурляева посоветовала есть их с белыми прожилками\"")
                    && t.get("structuralRelationType").toString().equals("\"Одно включает второе\"")
                    && t.get("samePartRelationType").toString().equals("\"Точное равенство\"")) {
                flag = true;
                break;
            }
        }
        Assert.isTrue(flag);
    }

    @Test
    public void findArgumentationForSameEquals() throws Exception {
        String res = mockMvc.perform(
                MockMvcRequestBuilders.post("/argument/statement")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new ReasononingRequest("Бурляева посоветовала есть их с белыми прожилками."))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        TreeNode treeNode = mapper.readTree(res);

        boolean flag = false;
        for (int i = 0; i < treeNode.size(); i++) {
            TreeNode t = treeNode.get(i);
            TreeNode l = t.get("link").get("result");
            if (t.get("metric").toString().equals("1.0")
                    && l.toString().equals("\"бурляева посоветовала есть их с белыми прожилками\"")
                    && t.get("structuralRelationType").toString().equals("\"Пересекаются полностью\"")
                    && t.get("samePartRelationType").toString().equals("\"Точное равенство\"")) {
                flag = true;
                break;
            }
        }
        Assert.isTrue(flag);
    }

    @Test
    public void findArgumentationForGeneralizationInclude() throws Exception {
        String res = mockMvc.perform(
                MockMvcRequestBuilders.post("/argument/statement")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new ReasononingRequest("Бурляева посоветовала есть их с бесцветными прожилками, врачи совертуют овощи."))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        TreeNode treeNode = mapper.readTree(res);

        boolean flag = false;
        for (int i = 0; i < treeNode.size(); i++) {
            TreeNode t = treeNode.get(i);
            TreeNode l = t.get("link").get("result");
            if (t.get("metric").toString().equals("0.8333334")
                    && l.toString().equals("\"бурляева посоветовала есть их с белыми прожилками\"")
                    && t.get("structuralRelationType").toString().equals("\"Одно включает второе\"")
                    && t.get("samePartRelationType").toString().equals("\"Обобщение\"")) {
                flag = true;
                break;
            }
        }
        Assert.isTrue(flag);
    }

    @Test
    public void findArgumentationForGeneralizationSame() throws Exception {
        String res = mockMvc.perform(
                MockMvcRequestBuilders.post("/argument/statement")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new ReasononingRequest("Бурляева посоветовала есть их с бесцветными прожилками."))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        TreeNode treeNode = mapper.readTree(res);

        boolean flag = false;
        for (int i = 0; i < treeNode.size(); i++) {
            TreeNode t = treeNode.get(i);
            TreeNode l = t.get("link").get("result");
            if (t.get("metric").toString().equals("1.0")
                    && l.toString().equals("\"бурляева посоветовала есть их с белыми прожилками\"")
                    && t.get("structuralRelationType").toString().equals("\"Пересекаются полностью\"")
                    && t.get("samePartRelationType").toString().equals("\"Обобщение\"")) {
                flag = true;
                break;
            }
        }
        Assert.isTrue(flag);
    }

    @Test
    public void findArgumentationForEqualsIntersection() throws Exception {
        String res = mockMvc.perform(
                MockMvcRequestBuilders.post("/argument/statement")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new ReasononingRequest("Бурляева посоветовала есть их с прожилками, врачи советуют фрукты."))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        TreeNode treeNode = mapper.readTree(res);

        boolean flag = false;
        for (int i = 0; i < treeNode.size(); i++) {
            TreeNode t = treeNode.get(i);
            TreeNode l = t.get("link").get("result");
            if (t.get("metric").toString().equals("0.5")
                    && l.toString().equals("\"бурляева посоветовала есть их с белыми прожилками\"")
                    && t.get("structuralRelationType").toString().equals("\"Пересечение\"")
                    && t.get("samePartRelationType").toString().equals("\"Точное равенство\"")) {
                flag = true;
                break;
            }
        }
        Assert.isTrue(flag);
    }

    @Test
    public void findArgumentationForSimilarIntersection() throws Exception {
        String res = mockMvc.perform(
                MockMvcRequestBuilders.post("/argument/statement")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new ReasononingRequest("Части аллергических ответов можно будет избежать, врачи советуют фрукты."))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        TreeNode treeNode = mapper.readTree(res);

        boolean flag = false;
        for (int i = 0; i < treeNode.size(); i++) {
            TreeNode t = treeNode.get(i);
            TreeNode l = t.get("link").get("result");
            if (t.get("metric").toString().equals("0.675")
                    && l.toString().equals("\"если человеку предложить уже очищенный мандарин, части аллергических реакций можно будет избежать\"")
                    && t.get("structuralRelationType").toString().equals("\"Пересечение\"")
                    && t.get("samePartRelationType").toString().equals("\"Cхожие (синонимичные)\"")) {
                flag = true;
                break;
            }
        }
        Assert.isTrue(flag);
    }

    @Test
    public void findArgumentationForSimilarSame() throws Exception {
        String res = mockMvc.perform(
                MockMvcRequestBuilders.post("/argument/statement")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new ReasononingRequest("Если человеку предложить уже очищенный мандарин, части аллергических ответов можно будет избежать."))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        TreeNode treeNode = mapper.readTree(res);

        boolean flag = false;
        for (int i = 0; i < treeNode.size(); i++) {
            TreeNode t = treeNode.get(i);
            TreeNode l = t.get("link").get("result");
            if (t.get("metric").toString().equals("1.0")
                    && l.toString().equals("\"если человеку предложить уже очищенный мандарин, части аллергических реакций можно будет избежать\"")
                    && t.get("structuralRelationType").toString().equals("\"Пересекаются полностью\"")
                    && t.get("samePartRelationType").toString().equals("\"Cхожие (синонимичные)\"")) {
                flag = true;
                break;
            }
        }
        Assert.isTrue(flag);
    }

    @Test
    public void findArgumentationForSimilarInclude() throws Exception {
        String res = mockMvc.perform(
                MockMvcRequestBuilders.post("/argument/statement")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new ReasononingRequest("Части аллергических ответов можно будет избежать."))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        TreeNode treeNode = mapper.readTree(res);

        boolean flag = false;
        for (int i = 0; i < treeNode.size(); i++) {
            TreeNode t = treeNode.get(i);
            TreeNode l = t.get("link").get("result");
            if (t.get("metric").toString().equals("0.8")
                    && l.toString().equals("\"если человеку предложить уже очищенный мандарин, части аллергических реакций можно будет избежать\"")
                    && t.get("structuralRelationType").toString().equals("\"Одно включает второе\"")
                    && t.get("samePartRelationType").toString().equals("\"Cхожие (синонимичные)\"")) {
                flag = true;
                break;
            }
        }
        Assert.isTrue(flag);
    }

    @Test
    public void findArgumentationForGeneralizationIntersection() throws Exception {
        String res = mockMvc.perform(
                MockMvcRequestBuilders.post("/argument/statement")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new ReasononingRequest("От части аллергических ответов можно будет спастись, врачи советуют фрукты."))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        TreeNode treeNode = mapper.readTree(res);

        boolean flag = false;
        for (int i = 0; i < treeNode.size(); i++) {
            TreeNode t = treeNode.get(i);
            TreeNode l = t.get("link").get("result");
            if (t.get("metric").toString().equals("0.675")
                    && l.toString().equals("\"если человеку предложить уже очищенный мандарин, части аллергических реакций можно будет избежать\"")
                    && t.get("structuralRelationType").toString().equals("\"Пересечение\"")
                    && t.get("samePartRelationType").toString().equals("\"Обобщение\"")) {
                flag = true;
                break;
            }
        }
        Assert.isTrue(flag);
    }

    @Test
    public void findArgumentationForEqualsInclude() throws Exception {
        String res = mockMvc.perform(
                MockMvcRequestBuilders.post("/argument/statement")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new ReasononingRequest("Если человеку предложить уже очищенный мандарин, части аллергических реакций можно будет избежать, врачи советуют фрукты."))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8);

        TreeNode treeNode = mapper.readTree(res);

        boolean flag = false;
        for (int i = 0; i < treeNode.size(); i++) {
            TreeNode t = treeNode.get(i);
            TreeNode l = t.get("link").get("result");
            if (t.get("metric").toString().equals("0.9166666")
                    && l.toString().equals("\"если человеку предложить уже очищенный мандарин, части аллергических реакций можно будет избежать\"")
                    && t.get("structuralRelationType").toString().equals("\"Одно включает второе\"")
                    && t.get("samePartRelationType").toString().equals("\"Точное равенство\"")) {
                flag = true;
                break;
            }
        }
        Assert.isTrue(flag);
    }
}
