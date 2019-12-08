package top.andnux.chain.eth.impl;

import top.andnux.chain.core.AbstractNode;
import top.andnux.chain.core.Measure;

public class EthNode extends AbstractNode {

    @Override
    public String name() {
        return "ETH";
    }

    @Override
    public Measure getMeasure() {
        return null;
    }
}
