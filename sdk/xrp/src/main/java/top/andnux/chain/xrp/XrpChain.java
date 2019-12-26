package top.andnux.chain.xrp;

import top.andnux.chain.core.Chain;
import top.andnux.chain.core.Provider;

/**
 * created on 2019/12/25
 */
@Provider(XrpChainImpl.class)
public interface XrpChain extends Chain<XrpAccount,XrpTransferParams> {

}
