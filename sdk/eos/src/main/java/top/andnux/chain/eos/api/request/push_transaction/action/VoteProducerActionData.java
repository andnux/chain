package top.andnux.chain.eos.api.request.push_transaction.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import top.andnux.chain.eos.client.pack.Pack;
import top.andnux.chain.eos.client.pack.PackType;

/**
 * 投票action使用的data
 * @author wuwei
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoteProducerActionData extends BaseActionData {
	@Pack(PackType.name)
	@JsonProperty("voter")
	private String voter;

	@Pack(PackType.name)
	@JsonProperty("proxy")
	private String proxy;

	@Pack(PackType.name)
	@JsonProperty("producers")
	private List<String> producers;

	public String getVoter() {
		return voter;
	}

	public void setVoter(String voter) {
		this.voter = voter;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public List<String> getProducers() {
		return producers;
	}

	public void setProducers(List<String> producers) {
		this.producers = producers;
	}
	
}
