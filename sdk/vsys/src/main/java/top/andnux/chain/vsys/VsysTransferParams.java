package top.andnux.chain.vsys;

import top.andnux.chain.core.TransferParams;

public class VsysTransferParams extends TransferParams {

    private long fee = (long) Math.pow(10,7);

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }
}
