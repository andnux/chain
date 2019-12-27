package top.andnux.chain.eos.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import top.andnux.chain.eos.api.request.AbiJsonToBinRequest;
import top.andnux.chain.eos.api.request.GetAccountRequest;
import top.andnux.chain.eos.api.request.GetAccountsRequest;
import top.andnux.chain.eos.api.request.GetBlockRequest;
import top.andnux.chain.eos.api.request.GetCurrencyBalanceRequest;
import top.andnux.chain.eos.api.request.GetTransactionRequest;
import top.andnux.chain.eos.api.request.PushTransactionRequest;
import top.andnux.chain.eos.api.result.AbiJsonToBinResults;
import top.andnux.chain.eos.api.result.GetAccountResults;
import top.andnux.chain.eos.api.result.GetAccountsResults;
import top.andnux.chain.eos.api.result.GetBlockResult;
import top.andnux.chain.eos.api.result.GetInfoResults;
import top.andnux.chain.eos.api.result.GetTransactionResult;
import top.andnux.chain.eos.api.result.PushTransactionResults;

public interface RpcService {

	@GET("/v1/chain/get_info")
	Call<GetInfoResults> getChainInfo();

	@POST("/v1/chain/get_block")
	Call<GetBlockResult> getBlock(@Body GetBlockRequest requestFields);

	@POST("/v1/chain/get_account")
	Call<GetAccountResults> getAccount(@Body GetAccountRequest requestFields);

	@POST("/v1/chain/push_transaction")
	Call<PushTransactionResults> pushTransaction(@Body PushTransactionRequest request);

	@POST("/v1/chain/abi_json_to_bin")
    Call<AbiJsonToBinResults> abiJsonToBin(@Body AbiJsonToBinRequest abiJsonToBinRequest);

	@POST("/v1/chain/get_currency_balance")
	Call<List<String>> getCurrencyBalance(@Body GetCurrencyBalanceRequest request);

	@POST("/v1/history/get_transaction")
	Call<GetTransactionResult> getTransaction(@Body GetTransactionRequest request);

	@POST("/v1/history/get_key_accounts")
	Call<GetAccountsResults> getAccounts(@Body GetAccountsRequest request);

}
