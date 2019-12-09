package v.systems.transaction;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import v.systems.Account;
import v.systems.contract.Contract;
import v.systems.error.SerializationError;
import v.systems.type.Base58Field;
import v.systems.type.NetworkType;
import v.systems.type.SerializedWithSize;
import v.systems.type.TransactionType;
import v.systems.utils.Base58;

public class RegisterContractTransaction extends ProvenTransaction {
    public final String[] BYTE_SERIALIZED_FIELDS = {"type", "contract", "initData", "description", "fee", "feeScale", "timestamp"};
    @SerializedWithSize
    protected Contract contract;
    private String contractId;
    @SerializedWithSize
    @Base58Field
    protected String initData;
    @SerializedWithSize
    protected String description;

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getInitData() {
        return initData;
    }

    public void setInitData(String initData) {
        this.initData = initData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RegisterContractTransaction() {
        type = TransactionType.RegisterContract.getTypeId();
    }

    @Override
    public JsonElement toAPIRequestJson(String publicKey, String signature) {
        JsonObject json = super.toAPIRequestJson(publicKey, signature).getAsJsonObject();
        try {
            json.addProperty("contract", Base58.encode(contract.toBytes()));
            json.addProperty("initData", initData);
            json.addProperty("description",description);
        } catch (SerializationError ex) {
            throw new RuntimeException("Cannot generate JSON. " + ex.getMessage());
        }
        return json;
    }

    @Override
    public JsonElement toColdSignJson(String publicKey, NetworkType type) {
        JsonObject json = super.toColdSignJson(publicKey, type, 3).getAsJsonObject();
        try {
            json.addProperty("address",Account.getAddress(publicKey, type.toByte()));
            json.addProperty("contract", Base58.encode(contract.toBytes()));
            json.addProperty("contractInit", initData);
            json.addProperty("description",description);
        } catch (SerializationError ex) {
            throw new RuntimeException("Cannot generate JSON. " + ex.getMessage());
        }
        return json;
    }

    @Override
    protected String[] getByteSerializedFields() {
        return BYTE_SERIALIZED_FIELDS;
    }
}
