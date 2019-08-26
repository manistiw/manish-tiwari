import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojos.Category;
import pojos.Product;
import util.TestDataUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class ProductApiTests extends BaseTest {
    private static final String CREATE_PRODUCT="/products";
    private static final String GET_ALL_PRODUCTS="/products";
    private List<Integer> productIdsToBeDeleteForCleanUp=new ArrayList<>();


    @DataProvider(name="getProductRequestBody")
    public Object[][] getProductRequestBody(){
        Product product= TestDataUtil.convertJsonStrToObject(TestDataUtil.getTestData("valid_product_request_body"), Product.class);
        return new Object[][]{{product}};
    }

    @Test(description = "Validate creating product",dataProvider = "getProductRequestBody")
    public void validateCreationOfProduct(Product product){
        ValidatableResponse postResponse=restAssuredClient.doPost(CREATE_PRODUCT, TestDataUtil.getJSONObject(product));
        assertTrue(postResponse.extract().statusCode()== HttpStatus.SC_CREATED);
        int id=postResponse.extract().body().jsonPath().get("id");
        assertNotNull(id);
        productIdsToBeDeleteForCleanUp.add(id);
    }

    @DataProvider(name = "getTotalCountOfProduct")
    public Object[][] getTotalCountOfProduct(){
        ValidatableResponse getResponse=restAssuredClient.doGet(GET_ALL_PRODUCTS);
        int initialTotal=getResponse.extract().body().jsonPath().get("total");
        Product product= TestDataUtil.convertJsonStrToObject(TestDataUtil.getTestData("valid_product_request_body"), Product.class);
        ValidatableResponse createResponse=restAssuredClient.doPost(CREATE_PRODUCT, TestDataUtil.getJSONObject(product));
        return new Object[][]{{initialTotal+1}};
    }

    @Test(description = "Validate get all products",dataProvider = "getTotalCountOfProduct")
    public void validateGetAllProducts(int totalProducts){
        ValidatableResponse getResponse=restAssuredClient.doGet(GET_ALL_PRODUCTS);
        assertTrue(getResponse.extract().statusCode()==HttpStatus.SC_OK);
        int totalNumberOfProducts=getResponse.extract().body().jsonPath().get("total");
        assertTrue(totalProducts==totalNumberOfProducts);
    }
    @DataProvider(name="getQueryParametersToSet")
    public Object[][] getQueryParametersToSet(){
        Map<String,String> queryParams=new HashMap<>();
        queryParams.put("category.name","TVs");
        queryParams.put("price[$gt]","500");
        queryParams.put("price[$lt]","800");
        queryParams.put("shipping[$eq]","0");
        restAssuredClient.reqSpecs.setQueryParameters(queryParams);
        return new Object[][]{{queryParams}};
    }

    @Test(description = "Validate query product with price range",dataProvider = "getQueryParametersToSet")
    public void validatePriceFilterQuery(Map<String,String> queryParams){
        ValidatableResponse getResponse=restAssuredClient.doGet(GET_ALL_PRODUCTS);
        assertTrue(getResponse.extract().statusCode()==HttpStatus.SC_OK);
        List<Product> products=getResponse.extract().body().jsonPath().getList("data",Product.class);
        for(Product product:products){
            assertTrue(product.getPrice()<Float.parseFloat(queryParams.get("price[$lt]")));
            assertTrue(product.getPrice()>Float.parseFloat(queryParams.get("price[$gt]")));
            List<String> categoryNamesOfProduct=new ArrayList<>();
            for(Category category:product.getCategories()){
                String categoryName=category.getName();
                categoryNamesOfProduct.add(categoryName);
            }
            assertTrue(categoryNamesOfProduct.contains(queryParams.get("category.name")));
        }
    }

    @DataProvider(name="createProductWithHighestPrice")
    public Object[][] createProductWithHighestPrice(){
        Product product= TestDataUtil.convertJsonStrToObject(TestDataUtil.getTestData("valid_product_request_body"), Product.class);
        product.setPrice(99999.99);
        Product productWithMaxPrice=restAssuredClient.doPost(CREATE_PRODUCT, TestDataUtil.getJSONObject(product)).extract().body().as(Product.class);
        Map<String,String> queryParams=new HashMap<>();
        queryParams.put("$sort[price]","-1");
        restAssuredClient.reqSpecs.setQueryParameters(queryParams);
        productIdsToBeDeleteForCleanUp.add(productWithMaxPrice.getId());
        return new Object[][]{{productWithMaxPrice}};
    }

    @Test(description = "Validate Maximum price [99999.99] product appears first in price sorting",dataProvider ="createProductWithHighestPrice")
    public void validatePriceSortingInDecreasingOrder(Product productWithMaxPrice){
        ValidatableResponse getResponse=restAssuredClient.doGet(GET_ALL_PRODUCTS);
        assertTrue(getResponse.extract().statusCode()==HttpStatus.SC_OK);
        Product products=getResponse.extract().body().jsonPath().getObject("data[0]",Product.class);
        Double expectedPrice=productWithMaxPrice.getPrice();
        Double actualPrice=products.getPrice();
        assertEquals(expectedPrice,actualPrice);
    }

    @AfterClass(alwaysRun = true)
    public void cleanupProductCreatedForTestData(){
        for(Integer i:productIdsToBeDeleteForCleanUp){
            restAssuredClient.doDelete(GET_ALL_PRODUCTS+"/"+i.toString());
        }
    }


}
