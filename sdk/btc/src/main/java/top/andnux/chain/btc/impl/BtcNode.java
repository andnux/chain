package top.andnux.chain.btc.impl;

import top.andnux.chain.core.AbstractNode;
import top.andnux.chain.core.Measure;

public class BtcNode extends AbstractNode {
    @Override
    public String name() {
        return "BTC";
    }

    @Override
    public Measure getMeasure() {
        return new BtcMeasure();
    }
}
