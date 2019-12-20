package top.andnux.chain.eos;


import top.andnux.chain.core.TransferParams;

public class EosTransferParams extends TransferParams {

    private String contract = "eosio.token";
    private String memo = "";

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
