package top.andnux.chain.eos.client.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import top.andnux.chain.eos.EosClient;
import top.andnux.chain.eos.api.request.push_transaction.action.BuyrambytesActionData;
import top.andnux.chain.eos.api.request.push_transaction.action.DelegatebwActionData;
import top.andnux.chain.eos.api.request.push_transaction.action.NewaccountActionData;
import top.andnux.chain.eos.api.request.push_transaction.action.TransferActionData;
import top.andnux.chain.eos.api.request.push_transaction.action.VoteProducerActionData;
import top.andnux.chain.eos.api.result.GetBlockResult;
import top.andnux.chain.eos.api.result.GetInfoResults;
import top.andnux.chain.eos.bean.action.Action;
import top.andnux.chain.eos.bean.transaction.Transaction;
import top.andnux.chain.eos.client.exception.ApiException;
import top.andnux.chain.eos.client.pack.AssetQuantity;
import top.andnux.chain.eos.crypto.EccTool;

/**
 * 交易构造器
 * 
 * @author wuwei
 *
 */
public class TransactionBuilder {

	private EosClient eos4j;

	private TransactionBuilder(EosClient eos4j) {
		this.eos4j = eos4j;
	}

	public static TransactionBuilder newInstance(EosClient eos4j) {
		return new TransactionBuilder(eos4j);
	}

	/**
	 * 组装原始转账交易
	 * 
	 * @param pk
	 * @param creator
	 * @param newAccount
	 * @param owner
	 * @param active
	 * @param buyRam
	 * @param stakeNetQuantity
	 * @param stakeCpuQuantity
	 * @param transfer
	 * @return
	 * @throws IOException
	 */
	public SignedTransactionToPush buildNewAccountRawTx(String pk, String creator, String newAccount, String owner,
			String active, Long buyRam, String stakeNetQuantity, String stakeCpuQuantity, Long transfer)
			throws IOException {
		return buildRawTx(pk, () -> {
			// actions
			List<Action> actions = new ArrayList<>();
			// newaccount
			NewaccountActionData createMap = new NewaccountActionData();
			createMap.setCreator(creator);
			createMap.setName(newAccount);
			createMap.setOwner(owner);
			createMap.setActive(active);
			Action createAction = new Action(creator, "eosio", "newaccount", createMap);
			actions.add(createAction);
			// buyrambytes
			BuyrambytesActionData buyMap = new BuyrambytesActionData();
			buyMap.setPayer(creator);
			buyMap.setReceiver(newAccount);
			buyMap.setBytes(buyRam);
			Action buyAction = new Action(creator, "eosio", "buyrambytes", buyMap);
			actions.add(buyAction);
			// delegatebw
			if (stakeNetQuantity != null && stakeCpuQuantity != null && transfer != null) {
				DelegatebwActionData delMap = new DelegatebwActionData();
				delMap.setFrom(creator);
				delMap.setReceiver(newAccount);
				delMap.setStakeNetQuantity(AssetQuantity.parse(stakeNetQuantity, "eosio.token", "4"));
				delMap.setStakeCpuQuantity(AssetQuantity.parse(stakeCpuQuantity, "eosio.token", "4"));
				delMap.setTransfer(transfer);
				Action delAction = new Action(creator, "eosio", "delegatebw", delMap);
				actions.add(delAction);
			}
			return actions;
		});
	}

	/**
	 * 组装原始交易
	 * 
	 * @param pk
	 * @param actionCollector
	 * @return
	 * @throws ApiException
	 * @throws IOException
	 */
	public SignedTransactionToPush buildRawTx(String pk, ActionCollector actionCollector)
			throws ApiException, IOException {
		// get chain info
		GetInfoResults info = eos4j.getChainInfo();
		// get block info
		GetBlockResult block = eos4j.getBlock(info.getHeadBlockNum().toString());
		// tx
		Transaction tx = new Transaction();
		tx.setExpiration(new Date(info.getHeadBlockTime().getTime() + 60000));
		tx.setRefBlockNum(block.getBlockNum());
		tx.setRefBlockPrefix(block.getRefBlockPrefix());
		tx.setNetUsageWords(0l);
		tx.setMaxCpuUsageMs(0l);
		tx.setDelaySec(0l);
		// add actions
		List<Action> actions = actionCollector.collectActions();
		tx.setActions(actions);
		// 签名 TODO
		String sign = EccTool.signTransaction(pk, new TransactionToSign(info.getChainId(), tx));
		// 计算txId
		String txId = eos4j.calcTransactionId(tx);
		// reset action data
		for (Action action : actions) {
			action.setData(action.getData().toString());
		}
		return new SignedTransactionToPush(txId, "none", tx, new String[] { sign});
	}

	/**
	 * 组装转账原始交易
	 * 
	 * @param pk
	 * @param contractAccount
	 * @param from
	 * @param to
	 * @param quantity
	 * @param memo
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws Exception
	 */
	public SignedTransactionToPush buildTransferRawTx(String pk, String contractAccount, String from, String to,
			String quantity, String memo) throws ApiException, IOException {
		return buildRawTx(pk, () -> {
			List<Action> actions = new ArrayList<>();
			// data
			TransferActionData dataMap = new TransferActionData();
			dataMap.setFrom(from);
			dataMap.setTo(to);
			dataMap.setQuantity(AssetQuantity.parse(quantity, contractAccount, "4"));
			dataMap.setMemo(memo);
			// action
			Action action = new Action(from, contractAccount, "transfer", dataMap);
			actions.add(action);
			return actions;
		});
	}

	/**
	 * 组装投票原始交易
	 * 
	 * @param pk
	 * @param voter
	 * @param proxy
	 * @param producers
	 * @return
	 * @throws ApiException
	 * @throws IOException
	 */
	public SignedTransactionToPush buildVoteProducerRawTx(String pk, String voter, String proxy, List<String> producers)
			throws ApiException, IOException {
		return buildRawTx(pk, () -> {
			Arrays.sort(producers.toArray(new String[producers.size()]), (h1, h2) -> h1.compareTo(h2));
			List<Action> actions = new ArrayList<>();
			// data
			VoteProducerActionData data = new VoteProducerActionData();
			data.setVoter(voter);
			data.setProxy(proxy);
			data.setProducers(producers);
			// action
			Action action = new Action(voter, "eosio", "voteproducer", data);
			actions.add(action);
			return actions;
		});
	}
}
