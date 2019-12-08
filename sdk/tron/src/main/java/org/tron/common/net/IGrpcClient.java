package top.andnux.chain.tron.net;


public enum IGrpcClient {

    THIS;

    private GrpcClient rpcCli;
    private NodeProvider mProvider;
    private String fullNode;
    private String solidityNode;

    public interface NodeProvider {

        String getFullNode();

        String getSolidityNode();
    }

    public NodeProvider getProvider() {
        return mProvider;
    }

    public void setProvider(NodeProvider provider) {
        mProvider = provider;
    }

    public GrpcClient getCli() {
        if (mProvider == null) {
            throw new IllegalArgumentException("please call setProvider(NodeProvider) ");
        }
        if (!mProvider.getFullNode().equalsIgnoreCase(fullNode) ||
                mProvider.getSolidityNode().equalsIgnoreCase(solidityNode)) {
            fullNode = mProvider.getFullNode();
            solidityNode = mProvider.getSolidityNode();
            initGRpc(fullNode, solidityNode);
        }
        if (rpcCli == null) initGRpc(fullNode, solidityNode);
        if (!rpcCli.canUseSolidityNode()) rpcCli.connetSolidityNode(solidityNode);
        if (!rpcCli.canUseFullNode()) rpcCli.connetFullNode(fullNode);
        return rpcCli;
    }

    public void initGRpc(String fullNode, String solidityNode) {
        if (rpcCli != null) {
            try {
                rpcCli.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        rpcCli = new GrpcClient();
        rpcCli.connetSolidityNode(solidityNode);
        rpcCli.connetFullNode(fullNode);
    }
}
