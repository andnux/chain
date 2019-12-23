package top.andnux.chain.tron;

import java.io.Serializable;

/**
 * created on 2019/12/23
 */
public class TronNode implements Serializable {

    private String fullNode;
    private String solidityNode;

    public TronNode() {

    }

    public TronNode(String fullNode, String solidityNode) {
        this.fullNode = fullNode;
        this.solidityNode = solidityNode;
    }

    public String getFullNode() {
        return fullNode;
    }

    public void setFullNode(String fullNode) {
        this.fullNode = fullNode;
    }

    public String getSolidityNode() {
        return solidityNode;
    }

    public void setSolidityNode(String solidityNode) {
        this.solidityNode = solidityNode;
    }
}
