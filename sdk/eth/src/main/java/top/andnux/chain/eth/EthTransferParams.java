package top.andnux.chain.eth;

import java.math.BigInteger;

import top.andnux.chain.core.TransferParams;

public class EthTransferParams extends TransferParams {

    private String contract;
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private BigInteger nonce;

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }
}
