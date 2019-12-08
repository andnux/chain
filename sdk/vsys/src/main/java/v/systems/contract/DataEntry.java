package v.systems.contract;

import org.bitcoinj.core.Base58;

import java.util.List;

import v.systems.error.SerializationError;
import v.systems.serialization.BytesSerializable;
import v.systems.type.DataType;
import v.systems.utils.BytesHelper;

public class DataEntry implements BytesSerializable {
    public final int KEY_LENGTH = 32;
    public final int ADDRESS_LENGTH = 26;
    private DataType type;
    private byte[] data;

    @Override
    public byte[] toBytes() throws SerializationError {
        if (data == null) {
            throw new SerializationError("No data in DataEntry");
        }
        if (!verifyLength()) {
            throw new SerializationError("Invalid length of DataEntry");
        }
        byte[] header = {type.getTypeId()};
        if (type == DataType.ShortText) {
            byte[] len = BytesHelper.toBytes((short) data.length);
            header = BytesHelper.concat(header, len);
        }
        return BytesHelper.concat(header, data);
    }

    @Override
    public List<Byte> toByteList() throws SerializationError {
        return BytesHelper.toList(this.toBytes());
    }

    public boolean verifyLength() {
        if (data == null) {
            return false;
        }
        switch (type) {
            case PublicKey:
                return data.length == KEY_LENGTH;
            case Address:
            case ContractAccount:
                return data.length == ADDRESS_LENGTH;
            case Amount:
                return data.length == Long.BYTES;
            case Int32:
                return data.length == Integer.BYTES;
            case ShortText:
                return true;
            default:
                return false;
        }
    }

    public static DataEntry builder() {
        return new DataEntry();
    }

    public DataEntry type(DataType type) {
        this.type = type;
        return this;
    }

    public DataEntry data(byte[] data) {
        this.data = data;
        return this;
    }

    public DataEntry build() {
        return this;
    }

    public boolean isAccountType() {
        return type == DataType.Address || type == DataType.ContractAccount;
    }

    public static DataEntry publicKey(String publicKey) {
        return DataEntry.builder()
                .type(DataType.PublicKey)
                .data(Base58.decode(publicKey))
                .build();
    }

    public static DataEntry address(String address) {
        return DataEntry.builder()
                .type(DataType.Address)
                .data(Base58.decode(address))
                .build();
    }

    public static DataEntry amount(Long amount) {
        return DataEntry.builder()
                .type(DataType.Amount)
                .data(BytesHelper.toBytes(amount))
                .build();
    }

    public static DataEntry int32(Integer int32) {
        return DataEntry.builder()
                .type(DataType.Int32)
                .data(BytesHelper.toBytes(int32))
                .build();
    }

    public static DataEntry shortText(String text) {
        byte[] data = BytesHelper.toBytes(text);
        byte[] len = BytesHelper.toBytes((short) data.length);
        return DataEntry.builder()
                .type(DataType.ShortText)
                .data(BytesHelper.concat(len, data))
                .build();
    }

    public static DataEntry contractAccount(String contractAccount) {
        return DataEntry.builder()
                .type(DataType.ContractAccount)
                .data(Base58.decode(contractAccount))
                .build();
    }
}
