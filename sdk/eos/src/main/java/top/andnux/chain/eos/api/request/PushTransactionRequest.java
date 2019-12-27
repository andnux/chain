package top.andnux.chain.eos.api.request;

import top.andnux.chain.eos.bean.transaction.Transaction;


public class PushTransactionRequest {

	private String compression;

	private Transaction transaction;

	private String[] signatures;
	
	public PushTransactionRequest() {

	}

	public PushTransactionRequest(String compression, Transaction transaction, String[] signatures) {
		this.compression = compression;
		this.transaction = transaction;
		this.signatures = signatures;
	}

	public String getCompression() {
		return compression;
	}

	public void setCompression(String compression) {
		this.compression = compression;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public String[] getSignatures() {
		return signatures;
	}

	public void setSignatures(String[] signatures) {
		this.signatures = signatures;
	}

}
