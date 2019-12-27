package top.andnux.chain.eos.api.result.get_transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import top.andnux.chain.eos.bean.block.TransactionReceiptHeader;
import top.andnux.chain.eos.bean.transaction.PackedTransaction;


@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionReceiptWithTrxList<T extends PackedTransaction> extends TransactionReceiptHeader {

	@JsonProperty("trx")
	private List<T> trx;

	public List<T> getTrx() {
		return trx;
	}

	public void setTrx(List<T> trx) {
		this.trx = trx;
	}

}
