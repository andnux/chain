package top.andnux.chain.eos.api.request.push_transaction.action;


import top.andnux.chain.eos.client.pack.PackUtils;
import top.andnux.chain.eos.utils.ByteBuffer;
import top.andnux.chain.eos.utils.Hex;

public class BaseActionData {

	public String toString() {
		ByteBuffer bb = new ByteBuffer();
		PackUtils.packObj(this, bb);
		return Hex.bytesToHexString(bb.getBuffer());
	}
}
