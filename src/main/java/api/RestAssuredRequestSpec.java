package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Map;

@Component
public class RestAssuredRequestSpec {
    private final Logger log = LoggerFactory.getLogger(RestAssuredRequestSpec.class);

    private RestRequestSpecBuilder reqSpecBuilder;

    @Autowired
    public RestAssuredRequestSpec(RestRequestSpecBuilder reqSpecBuilder) {
        this.reqSpecBuilder = reqSpecBuilder;
    }


    public RequestSpecification getSpecification(){
        return reqSpecBuilder.getReqBuilder().build();
    }


    public RestRequestSpecBuilder resetSpecification(){
        this.reqSpecBuilder.setReqBuilder(new RequestSpecBuilder());
        return reqSpecBuilder;
    }
    @Step("Setting Query Parameters before calling endpoint: {0}")
    public RestRequestSpecBuilder setQueryParameters(Map<String,String> queryParams){
        this.reqSpecBuilder.setQueryParams(queryParams);
        return reqSpecBuilder;
    }
}
