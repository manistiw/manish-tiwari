import api.RestAssuredClient;
import config.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;

@ContextConfiguration(classes = {ApplicationContext.class})
public class BaseTest extends AbstractTestNGSpringContextTests {
    @Autowired
    RestAssuredClient restAssuredClient;

    @AfterMethod
    public void resetRestSpecification(){
        restAssuredClient.reqSpecs.resetSpecification();
    }

    @AfterSuite(alwaysRun = true)
    public void doAfterSuite(){
        System.out.println("After Suite");
    }
}
