package top.andnux.chain.eos.bean.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import top.andnux.chain.eos.bean.Extensions;
import top.andnux.chain.eos.bean.action.Action;
import top.andnux.chain.eos.client.pack.Pack;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction extends TransactionHeader {
	
	@Pack
	@JsonProperty("context_free_actions")
	private List<Action> contextFreeActions = new ArrayList<>();

	@Pack
	@JsonProperty("actions")
	private List<Action> actions = new ArrayList<>();

	@Pack
	@JsonProperty("transaction_extensions")
	private List<Extensions> transactionExtensions = new ArrayList<>();

	public List<Action> getContextFreeActions() {
		return contextFreeActions;
	}

	public void setContextFreeActions(List<Action> contextFreeActions) {
		this.contextFreeActions = contextFreeActions;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public List<Extensions> getTransactionExtensions() {
		return transactionExtensions;
	}

	public void setTransactionExtensions(List<Extensions> transactionExtensions) {
		this.transactionExtensions = transactionExtensions;
	}

}
