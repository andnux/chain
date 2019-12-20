package top.andnux.chain.eos.bean.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import top.andnux.chain.eos.client.pack.Pack;
import top.andnux.chain.eos.client.pack.PackType;

/**
 * 
 * @author wangyan
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionNotice extends Action {
	@Pack(PackType.name)
	@JsonProperty("receiver")
	private String receiver;

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}
