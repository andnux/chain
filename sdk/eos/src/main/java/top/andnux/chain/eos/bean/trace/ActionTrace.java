package top.andnux.chain.eos.bean.trace;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionTrace extends BaseActionTrace {
	@JsonProperty("inline_traces")
	private List<ActionTrace> inlineTraces;

	public List<ActionTrace> getInlineTraces() {
		return inlineTraces;
	}

	public void setInlineTraces(List<ActionTrace> inlineTraces) {
		this.inlineTraces = inlineTraces;
	}

}
