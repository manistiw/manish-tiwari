import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojos.Services;
import pojos.Store;
import util.TestDataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class StoreApiTests extends BaseTest {
    private static final String CREATE_STORE ="/stores";
    private static final String GET_STORE ="/stores";
    private List<Integer> storeIdsToBeDeleteForCleanUp=new ArrayList<>();

    @DataProvider(name="getValidStorePostBody")
    public Object[][] getValidStorePostBody(){
        Store store= TestDataUtil.convertJsonStrToObject(TestDataUtil.getTestData("valid_store_request_body"), Store.class);
        return new Object[][]{{store}};
    }

    @Test(description = "validate creation of store for given valid category json body",dataProvider = "getValidStorePostBody")
    public void validateCreationOfStore(Store store){
        ValidatableResponse response=restAssuredClient.doPost(CREATE_STORE, TestDataUtil.getJSONObject(store));
        Integer id=response.extract().body().jsonPath().get("id");
        assertTrue(response.extract().statusCode()== HttpStatus.SC_CREATED);
        assertNotNull(id);
        storeIdsToBeDeleteForCleanUp.add(id);
    }

    @DataProvider(name="getQueryParametersToSet")
    public Object[][] getQueryParametersToSet(){
        Map<String,String> queryParams=new HashMap<>();
        queryParams.put("state","MN");
        restAssuredClient.reqSpecs.setQueryParameters(queryParams);
        return new Object[][]{{queryParams}};
    }
    @Test(description = "Validate Get with Query Param to find store in Given City",dataProvider = "getQueryParametersToSet")
    public void validateQueryParamForStore(Map<String,String> queryParam){
        ValidatableResponse getResponse=restAssuredClient.doGet(GET_STORE);
        assertTrue(getResponse.extract().statusCode()==HttpStatus.SC_OK);
        List<Store> stores=getResponse.extract().body().jsonPath().getList("data",Store.class);
        for(Store store:stores){
            assertEquals(store.getState(),queryParam.get("state"));
        }
    }
    @DataProvider(name="setQueryParametersToSet")
    public Object[][] setQueryParametersToSet(){
        Map<String,String> queryParams=new HashMap<>();
        queryParams.put("service.name","Apple Shop");
        restAssuredClient.reqSpecs.setQueryParameters(queryParams);
        return new Object[][]{{queryParams}};
    }
    @Test(description = "Validate Get with Query Param to find store that sell apple products",dataProvider = "setQueryParametersToSet")
    public void validateQueryParamForStoreThatSellAppleProduct(Map<String,String> queryParam){
        ValidatableResponse getResponse=restAssuredClient.doGet(GET_STORE);
        assertTrue(getResponse.extract().statusCode()==HttpStatus.SC_OK);
        List<Store> stores=getResponse.extract().body().jsonPath().getList("data",Store.class);
        for(Store store:stores){
            List<String> serviceName=new ArrayList<>();
            for(Services services:store.getServices())
            serviceName.add(services.getName());
            assertTrue(serviceName.contains(queryParam.get("service.name")));
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanupStoreCreatedForTestData(){
        for(Integer id:storeIdsToBeDeleteForCleanUp){
            restAssuredClient.doDelete(GET_STORE+"/"+id);
        }
    }


}
