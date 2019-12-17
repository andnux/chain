package org.tron.common.wallet;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;

import org.tron.common.bip32.Bip32ECKeyPair;
import org.tron.common.crypto.ECKey;
import org.tron.common.crypto.Hash;
import org.tron.common.crypto.MnemonicUtils;
import org.tron.common.utils.Utils;
import org.tron.protos.Protocol;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.tron.common.utils.StringUtil;

public class TronWallet {

    private ECKey mECKey;
    public String address = "";
    private byte[] publicKey;
    private String mnemonic;
    private String mKeyStore;

    private TronWallet() {

    }

    public byte[] getPublicKey() {
        return mECKey != null ? mECKey.getPubKey() : publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] getPrivateKey() {
        return mECKey != null ? mECKey.getPrivKeyBytes() : null;
    }

    public static TronWallet generateWallet() {
        TronWallet wallet = new TronWallet();
        byte[] initialEntropy = new byte[16];
        Utils.getRandom().nextBytes(initialEntropy);
        wallet.mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(wallet.mnemonic, null);
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair bip44Keypair = generateBip44KeyPair(masterKeypair, false);
        wallet.mECKey = ECKey.fromPrivate(bip44Keypair.getPrivateKeyBytes33());
        return wallet;
    }

    public static TronWallet generateKeyForPrivateKey(String privateKey) {
        TronWallet wallet = new TronWallet();
        if (privateKey != null && !privateKey.isEmpty()) {
            ECKey tempKey = null;
            try {
                BigInteger priK = new BigInteger(privateKey, 16);
                tempKey = ECKey.fromPrivate(priK);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            wallet.mECKey = tempKey;
        } else {
            wallet.mECKey = null;
        }
        return wallet;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public static TronWallet generateKeyForKeyStore(String keyStore, String password) throws Exception {
        TronWallet wallet = new TronWallet();
        wallet.mKeyStore = keyStore;
        if (keyStore != null && !keyStore.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            WalletFile walletFile = objectMapper.readValue(keyStore, WalletFile.class);
            ECKeyPair keyPair = Wallet.decrypt(password, walletFile);
            BigInteger privateKey = keyPair.getPrivateKey();
            wallet.mECKey = ECKey.fromPrivate(privateKey);
        } else {
            wallet.mECKey = null;
        }
        return wallet;
    }


    public static TronWallet generateKeyForMnemonic(String mnemonic) {
        TronWallet wallet = new TronWallet();
        wallet.mnemonic = mnemonic;
        if (mnemonic != null && !mnemonic.isEmpty()) {
            ECKey tempKey = null;
            try {
                byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
                Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
                Bip32ECKeyPair bip44Keypair = generateBip44KeyPair(masterKeypair, false);
                tempKey = ECKey.fromPrivate(bip44Keypair.getPrivateKeyBytes33());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            wallet.mECKey = tempKey;
        } else {
            wallet.mECKey = null;
        }
        return wallet;
    }


    public String getKeyStore(String password, File file) throws Exception {
        String walletFile = WalletUtils.generateWalletFile(password,
                ECKeyPair.create(getPrivateKey()), file, false);
        return readFileString(new File(file, walletFile).getAbsolutePath());
    }

    private String readFileString(String fileName) {
        StringBuilder builder = new StringBuilder();
        try (FileReader reader = new FileReader(fileName);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public String getAddress() {
        if (mECKey != null) {
            return StringUtil.encode58Check(mECKey.getAddress());
        } else if (publicKey != null) {
            return StringUtil.encode58Check(ECKey.fromPublicOnly(publicKey).getAddress());
        } else {
            return address;
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ECKey getECKey() {
        return mECKey;
    }

    public static Protocol.Transaction sign(Protocol.Transaction transaction, ECKey myKey) {
        Protocol.Transaction.Builder transactionBuilderSigned = transaction.toBuilder();
        byte[] hash = Hash.sha256(transaction.getRawData().toByteArray());
        List<Protocol.Transaction.Contract> listContract = transaction.getRawData().getContractList();
        for (int i = 0; i < listContract.size(); i++) {
            ECKey.ECDSASignature signature = myKey.sign(hash);
            ByteString bsSign = ByteString.copyFrom(signature.toByteArray());
            transactionBuilderSigned.addSignature(bsSign);
        }
        return transactionBuilderSigned.build();
    }

    public Protocol.Transaction sign(Protocol.Transaction transaction) {
        return TronWallet.sign(transaction, getECKey());
    }

    private static Bip32ECKeyPair generateBip44KeyPair(Bip32ECKeyPair master, boolean testNet) {
        if (testNet) {
            final int[] path = {44 | Bip32ECKeyPair.HARDENED_BIT, Bip32ECKeyPair.HARDENED_BIT, Bip32ECKeyPair.HARDENED_BIT, 0};
            return Bip32ECKeyPair.deriveKeyPair(master, path);
        } else {
            final int[] path = {44 | Bip32ECKeyPair.HARDENED_BIT, 195 | Bip32ECKeyPair.HARDENED_BIT, Bip32ECKeyPair.HARDENED_BIT, 0, 0};
            return Bip32ECKeyPair.deriveKeyPair(master, path);
        }
    }


}