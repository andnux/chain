package top.andnux.chain.eos.bean.block;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import top.andnux.chain.eos.bean.Extensions;
import top.andnux.chain.eos.bean.block_header.SignedBlockHeader;
import top.andnux.chain.eos.bean.transaction.PackedTransaction;

/**
 * 
 * @author wangyan
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignedBlock<T extends TransactionReceipt<? extends PackedTransaction>> extends SignedBlockHeader {

	@JsonProperty("transactions")
	private List<T> transactions;

	@JsonProperty("block_extensions")
	private List<Extensions> blockExtensions;
	
	public List<T> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<T> transactions) {
		this.transactions = transactions;
	}

	public List<Extensions> getBlockExtensions() {
		return blockExtensions;
	}

	public void setBlockExtensions(List<Extensions> blockExtensions) {
		this.blockExtensions = blockExtensions;
	}

}
