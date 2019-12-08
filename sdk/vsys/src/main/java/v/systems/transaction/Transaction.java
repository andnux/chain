package v.systems.transaction;

import java.io.Serializable;

public interface Transaction extends Serializable {

    String getId();

    void setId(String id);

    Byte getType();

    void setType(Byte type);

    Long getTimestamp();

    void setTimestamp(Long timestamp);

    Integer getHeight();

    void setHeight(Integer height);

    String getStatus();

    void setStatus(String status);

}
