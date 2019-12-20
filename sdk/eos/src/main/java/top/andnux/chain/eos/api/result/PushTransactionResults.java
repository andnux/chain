package top.andnux.chain.eos.api.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import top.andnux.chain.eos.bean.trace.TransactionTrace;

/**
 * 
 * @author wangyan
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushTransactionResults {

	@JsonProperty("transaction_id")
	private String transactionId;

	@JsonProperty("processed")
	private TransactionTrace processed;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public TransactionTrace getProcessed() {
		return processed;
	}

	public void setProcessed(TransactionTrace processed) {
		this.processed = processed;
	}

}
