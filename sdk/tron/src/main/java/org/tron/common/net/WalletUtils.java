package top.andnux.chain.tron.net;

import org.tron.protos.Protocol;
import org.tron.common.crypto.Hash;
import org.tron.common.utils.Sha256Hash;

public class WalletUtils {

    public static byte[] generateContractAddress(Protocol.Transaction trx, byte[] ownerAddress) {
        // get tx hash
        byte[] txRawDataHash = Sha256Hash.of(trx.getRawData().toByteArray()).getBytes();

        // combine
        byte[] combined = new byte[txRawDataHash.length + ownerAddress.length];
        System.arraycopy(txRawDataHash, 0, combined, 0, txRawDataHash.length);
        System.arraycopy(ownerAddress, 0, combined, txRawDataHash.length, ownerAddress.length);

        return Hash.sha3omit12(combined);
    }
}
