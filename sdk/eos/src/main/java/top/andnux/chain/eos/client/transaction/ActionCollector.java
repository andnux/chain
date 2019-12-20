package top.andnux.chain.eos.client.transaction;

import java.util.List;

import top.andnux.chain.eos.bean.action.Action;

public interface ActionCollector {
	public List<Action> collectActions();
}
