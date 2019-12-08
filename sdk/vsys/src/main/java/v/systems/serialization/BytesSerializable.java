package v.systems.serialization;

import java.io.Serializable;
import java.util.List;

import v.systems.error.SerializationError;

public interface BytesSerializable extends Serializable {
    byte[] toBytes() throws SerializationError;
    List<Byte> toByteList() throws SerializationError;
}
