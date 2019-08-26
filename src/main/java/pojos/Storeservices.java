package pojos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "createdAt",
        "updatedAt",
        "storeId",
        "serviceId"
})
public class Storeservices {

    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("storeId")
    private Integer storeId;
    @JsonProperty("serviceId")
    private Integer serviceId;

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("updatedAt")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updatedAt")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("storeId")
    public Integer getStoreId() {
        return storeId;
    }

    @JsonProperty("storeId")
    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    @JsonProperty("serviceId")
    public Integer getServiceId() {
        return serviceId;
    }

    @JsonProperty("serviceId")
    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

}
