package top.andnux.chain.eos.impl;

import top.andnux.chain.core.AbstractNode;
import top.andnux.chain.core.Measure;

public class EosNode extends AbstractNode {

    @Override
    public String name() {
        return "EOS";
    }

    @Override
    public Measure getMeasure() {
        return new EosMeasure();
    }
}
