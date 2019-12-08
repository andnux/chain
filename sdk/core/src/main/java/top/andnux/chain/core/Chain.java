package top.andnux.chain.core;

public interface Chain<A extends Account, AC extends AccountManager<A>,
        T extends TransferParams> {

    Node nodeManager();

    AC accountManager();

    void transfer(T params, Callback callback);

    interface Callback {

        void onSuccess(String txId);

        void onError(Throwable e);
    }
}
