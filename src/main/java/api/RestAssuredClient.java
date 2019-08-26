package api;

import io.restassured.response.ValidatableResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.allure.annotations.Step;

import static io.restassured.RestAssured.given;

@Component
public class RestAssuredClient {
    @Autowired
    public RestAssuredRequestSpec reqSpecs;


    @Step("Doing post call with given path: {0}")
    public ValidatableResponse doGet(String path){
        return given().
                spec(reqSpecs.getSpecification()).log().body().
                when().
                get(path).
                then().log().body();
    }

    @Step("Doing post call with given path: {0} and JSON BODY:{1}")
    public ValidatableResponse doPost(String url, JSONObject requestBody) {
        return given().
                spec(reqSpecs.getSpecification()).log().all().
                body(requestBody.toString()).
                post(url).
                then().log().body();
    }
    @Step("Doing delete call with given path: {0}")
    public ValidatableResponse doDelete(String path) {
        return given().
                spec(reqSpecs.getSpecification()).log().all().
                when().
                delete(path).then().log().body();
    }
}
