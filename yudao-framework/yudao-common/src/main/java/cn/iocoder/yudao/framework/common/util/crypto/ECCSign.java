package cn.iocoder.yudao.framework.common.util.crypto;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

public class ECCSign {
    public static Sign.SignatureData createSign(String privateKey, StringBuilder msg){
        byte[] bytes = Hex.decode(msg.toString());
        ECKeyPair keyPair = ECKeyPair.create(Hex.decode(privateKey));

        byte[] hash = Hash.sha3(bytes);
        Sign.SignatureData signatureData = Sign.signMessage(hash, keyPair, false);
        return signatureData;
    }
}
