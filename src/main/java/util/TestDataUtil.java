package util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.annotations.Step;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class TestDataUtil {
    private static final Logger log = LoggerFactory.getLogger(TestDataUtil.class);
    private static Map<String, Object> data = new HashMap<String, Object>();

    static {
        URL dirURL = TestDataUtil.class.getResource("/mockData");
        File[] files = null;
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            try {
                files = new File(dirURL.toURI()).listFiles();
                for (File file : files) {
                    try {
                        Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                        JsonFactory factory = new JsonFactory();
                        ObjectMapper mapper = new ObjectMapper(factory);
                        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
                        };
                        Map<String, Object> localJsonData = mapper.readValue(reader, typeRef);
                        data.putAll(localJsonData);
                    } catch (IOException e) {

                    }
                }
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block

            }
        }
    }


    @Step("getting json testData request body with given name: {0}")
    public static String getTestData(String property){
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(data.get(property));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Step("getting JSONObject from given pojo: {0}")
    public static JSONObject getJSONObject(Object objDto) {
        String json = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Map<String, Object> objectMap = objectMapper.convertValue(objDto, Map.class);
            return new JSONObject(objectMap);
        } catch (JSONException e) {
            log.info(json);
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Step("getting Pojo in :{1} from given json String: {0}")
    public static <T> T convertJsonStrToObject(@Nonnull String jsonString, Class<T> clazz) {
        try {
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            T obj = mapper.readValue(jsonString.replace("%s", RandomStringUtils
                    .random(20, true, true)), clazz);
            return obj;
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }
}
