import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojos.Category;
import util.TestDataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class CategoryAPITests extends BaseTest {
    private static final String CREATE_CATEGORY="/categories";
    private static final String GET_CATEGORY="/categories";
    private List<String> categoryIdsToBeDeleteForCleanUp=new ArrayList<>();

    @DataProvider(name="getValidCategoryPostBody")
    public Object[][] getValidCategoryPostBody(){
        Category category= TestDataUtil.convertJsonStrToObject(TestDataUtil.getTestData("valid_category_request_body"), Category.class);
        return new Object[][]{{category}};
    }

    @Test(description = "validate creation of Category for given valid category json body",dataProvider = "getValidCategoryPostBody")
    public void validateCreationOfCategory(Category category){
        ValidatableResponse response=restAssuredClient.doPost(CREATE_CATEGORY, TestDataUtil.getJSONObject(category));
        String id=response.extract().body().jsonPath().get("id");
        assertTrue(response.extract().statusCode()== HttpStatus.SC_CREATED);
        assertNotNull(id);
        categoryIdsToBeDeleteForCleanUp.add(id);
    }
    @DataProvider(name="getValidCategoryIdToGet")
    public Object[][] getValidCategoryIdToGet(){
        Category category= TestDataUtil.convertJsonStrToObject(TestDataUtil.getTestData("valid_category_request_body"), Category.class);
        ValidatableResponse response=restAssuredClient.doPost(CREATE_CATEGORY, TestDataUtil.getJSONObject(category));
        String id=response.extract().body().jsonPath().get("id");
        categoryIdsToBeDeleteForCleanUp.add(id);
        return new Object[][]{{id}};
    }


    @Test(description = "validate get category for given Id",dataProvider = "getValidCategoryIdToGet")
    public void validateGetCategory(String categoryId){
        ValidatableResponse response=restAssuredClient.doGet(CREATE_CATEGORY+"/"+categoryId);
        String id=response.extract().body().jsonPath().get("id");
        assertTrue(response.extract().statusCode()== HttpStatus.SC_OK);
        assertEquals(id,categoryId);
    }
    @DataProvider(name="getValidCategoryIdToDelete")
    public Object[][] getValidCategoryIdToDelete(){
        Category category= TestDataUtil.convertJsonStrToObject(TestDataUtil.getTestData("valid_category_request_body"), Category.class);
        ValidatableResponse response=restAssuredClient.doPost(CREATE_CATEGORY, TestDataUtil.getJSONObject(category));
        String id=response.extract().body().jsonPath().get("id");
        return new Object[][]{{id}};
    }

    @Test(description = "validate delete category for given Id",dataProvider ="getValidCategoryIdToDelete")
    public void validateDeleteCategory(String categoryId){
        ValidatableResponse response=restAssuredClient.doDelete(CREATE_CATEGORY+"/"+categoryId);
        String id=response.extract().body().jsonPath().get("id");
        assertTrue(response.extract().statusCode()== HttpStatus.SC_OK);
        assertEquals(id,categoryId);
    }

    @DataProvider(name="getQueryParametersToSet")
    public Object[][] getQueryParametersToSet(){
        Map<String,String> queryParams=new HashMap<>();
        queryParams.put("$select[]","name");
        restAssuredClient.reqSpecs.setQueryParameters(queryParams);
        return new Object[][]{{queryParams}};
    }

    @Test(description = "validate get category with query parameters as name",dataProvider = "getQueryParametersToSet")
    public void validateQueryParam(Map<String,String> queryParam){
        ValidatableResponse getResponse=restAssuredClient.doGet(GET_CATEGORY);
        assertTrue(getResponse.extract().statusCode()==HttpStatus.SC_OK);
        List<Category> categories=getResponse.extract().body().jsonPath().getList("data",Category.class);
        for(Category category:categories){
            assertNotNull(category.getName());
        }
    }

    @DataProvider(name="getLikeQueryParamsToSet")
    public Object[][] getLikeQueryParamsToSet(){
        Map<String,String> queryParams=new HashMap<>();
        queryParams.put("name[$like]","*TV*");
        restAssuredClient.reqSpecs.setQueryParameters(queryParams);
        return new Object[][]{{queryParams}};
    }

    @Test(description = "validate get category with TV in category Name",dataProvider = "getLikeQueryParamsToSet")
    public void validateLikeQueryParam(Map<String,String> queryParam){
        ValidatableResponse getResponse=restAssuredClient.doGet(GET_CATEGORY);
        assertTrue(getResponse.extract().statusCode()==HttpStatus.SC_OK);
        int limit=getResponse.extract().body().jsonPath().get("limit");
        for(int i=0;i<limit;i++) {
            String category = getResponse.extract().body().jsonPath().get(String.format("data[%s].name", i));
            assertTrue(category.contains(queryParam.get("name[$like]").replaceAll("\\*","")));
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanupCategoryCreatedForTestData(){
        for(String id:categoryIdsToBeDeleteForCleanUp){
            restAssuredClient.doDelete(GET_CATEGORY+"/"+id);
        }
    }







}
