package top.andnux.chain.eos.bean.block_header;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SignedBlockHeader extends BlockHeader {
	
	@JsonProperty("producer_signature")
	private String producerSignature;

	public String getProducerSignature() {
		return producerSignature;
	}

	public void setProducerSignature(String producerSignature) {
		this.producerSignature = producerSignature;
	}
	
}
