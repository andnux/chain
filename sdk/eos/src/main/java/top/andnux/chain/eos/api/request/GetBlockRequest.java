package top.andnux.chain.eos.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetBlockRequest {
	
	@JsonProperty("block_num_or_id")
	private String blockNumOrId;

	public String getBlockNumOrId() {
		return blockNumOrId;
	}

	public void setBlockNumOrId(String blockNumOrId) {
		this.blockNumOrId = blockNumOrId;
	}
	
}
