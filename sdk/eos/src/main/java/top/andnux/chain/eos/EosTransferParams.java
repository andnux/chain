package top.andnux.chain.eos;


import top.andnux.chain.core.TransferParams;

public class EosTransferParams extends TransferParams {

    private String actor;
    private String permission;
    private String memo = "";
    private String account = "eosio.token"; //合约帐号
    private String name = "transfer";//调的方法

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
