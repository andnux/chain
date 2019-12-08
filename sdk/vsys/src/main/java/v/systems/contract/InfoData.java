package v.systems.contract;

import org.bitcoinj.core.Base58;

import java.io.Serializable;

import v.systems.type.DataType;

public class InfoData  implements Serializable {
    private String data;
    private String type;
    private String name;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataEntry toDataEntry() {
        return DataEntry.builder()
                .data(Base58.decode(data))
                .type(DataType.parse(type))
                .build();
    }
}
