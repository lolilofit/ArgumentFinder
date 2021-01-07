package ru.nsu.usova.dipl.situation.ontology;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.usova.dipl.situation.ontology.model.OntologyRelated;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WordNetUtils {
    private static HttpClient client = HttpClient.newHttpClient();

    private static ObjectMapper objectMapper = new ObjectMapper();

    /*
    public static String decompressBody(HttpResponse<InputStream> response) throws IOException {
        GZIPInputStream inputStream = new GZIPInputStream(response.body());

        StringBuilder stringBuilder = new StringBuilder();

        int ch = -1;
        while ((ch = inputStream.read()) != -1)
            stringBuilder.append((char)ch);

        return stringBuilder.toString();
    }
    */

    public static OntologyRelated ontologyRelated(String word) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest getVersionBuilder = HttpRequest.newBuilder(new URI("http://127.0.0.1:8000/synsets?word=" + word)).header("accept", "application/json").GET().build();
        HttpResponse<String> response = client.send(getVersionBuilder, HttpResponse.BodyHandlers.ofString());

        OntologyRelated ontologyRelated = null;
        try {
            ontologyRelated = objectMapper.readValue(response.body(), OntologyRelated.class);
        } catch (Exception e) {
            System.out.println("Can't deserialize json response (Ontology)");
        }
        return ontologyRelated;
    }
}
