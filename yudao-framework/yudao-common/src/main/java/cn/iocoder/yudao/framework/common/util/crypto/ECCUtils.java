package cn.iocoder.yudao.framework.common.util.crypto;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class ECCUtils {
    public static String sm2En(String data) {
        //通过公钥对象获取公钥的基本域参数。
        String pubKey = "046aae899b919067580f62f6e50ad5c1ef1918860e94288b9463d48b5936cda7f65dde32df6b9016095d614b3e2826fd02482d950774f0c13de1e376ba607aaaa7";
        BCECPublicKey publicKey  = getECPublicKeyByPublicKeyHex(pubKey);
        ECParameterSpec ecParameterSpec = publicKey.getParameters();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                ecParameterSpec.getG(), ecParameterSpec.getN());
        //通过公钥值和公钥基本参数创建公钥参数对象
        ECPublicKeyParameters ecPublicKeyParameters = new ECPublicKeyParameters(publicKey.getQ(), ecDomainParameters);
        //根据加密模式实例化SM2公钥加密引擎
        SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        //初始化加密引擎
        sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));
        byte[] arrayOfBytes = null;
        try {
            //将明文字符串转换为指定编码的字节串
            byte[] in = data.getBytes("utf-8");
            //通过加密引擎对字节数串行加密
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将加密后的字节串转换为十六进制字符串
        return Hex.toHexString(arrayOfBytes);
    }

    //参数加密数据
    public static String sm2De(String cipherData){
        try {
            cipherData = "04"+cipherData;
            byte[] cipherDataByte = Hex.decode(cipherData);

            //刚才的私钥Hex，先还原私钥
            String privateKey = "00"+"74eb0aa3c703afccbff46d31dd54d4cb0c5b98920d59a574cbf18a10166a64c6";
            BigInteger privateKeyD = new BigInteger(privateKey, 16);
            ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters1);

            //用私钥解密
            SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
            sm2Engine.init(false, privateKeyParameters);

            //processBlock得到Base64格式，记得解码，前端没base64，就不用base64一次，直接解码
//            byte[] arrayOfBytes = Base64.decodeBase64(sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length));

            //得到明文：SM2 Encryption Test
            String data = new String(sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length),"utf-8");
            return data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //参数加密数据
    public static String javaSm2De(String cipherData){
        try {
            byte[] cipherDataByte = Hex.decode(cipherData);

            //刚才的私钥Hex，先还原私钥
            String privateKey = "74eb0aa3c703afccbff46d31dd54d4cb0c5b98920d59a574cbf18a10166a64c6";
            BigInteger privateKeyD = new BigInteger(privateKey, 16);
            ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters1);

            //用私钥解密
            SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
            sm2Engine.init(false, privateKeyParameters);

            //processBlock得到Base64格式，记得解码，前端没base64，就不用base64一次，直接解码
//            byte[] arrayOfBytes = Base64.decodeBase64(sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length));

            //得到明文：SM2 Encryption Test
            String data = new String(sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length),"utf-8");
            return data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //椭圆曲线ECParameters ASN.1 结构
    private static X9ECParameters x9ECParameters = GMNamedCurves.getByName("sm2p256v1");
    //椭圆曲线公钥的基本域参数。
    private static ECParameterSpec ecDomainParameters = new ECParameterSpec(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN());
    //椭圆曲线私钥的基本域参数。
    private static ECDomainParameters domainParameters1 = new ECDomainParameters(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN());

    /**
     * @Description 公钥字符串转换为 BCECPublicKey 公钥对象
     * @Author msx
     * @param pubKeyHex 64字节十六进制公钥字符串(如果公钥字符串为65字节首个字节为0x04：表示该公钥为非压缩格式，操作时需要删除)
     * @return BCECPublicKey SM2公钥对象
     */
    public static BCECPublicKey getECPublicKeyByPublicKeyHex(String pubKeyHex) {
        //截取64字节有效的SM2公钥（如果公钥首个字节为0x04）
        if (pubKeyHex.length() > 128) {
            pubKeyHex = pubKeyHex.substring(pubKeyHex.length() - 128);
        }
        //将公钥拆分为x,y分量（各32字节）
        String stringX = pubKeyHex.substring(0, 64);
        String stringY = pubKeyHex.substring(stringX.length());
        //将公钥x、y分量转换为BigInteger类型
        BigInteger x = new BigInteger(stringX, 16);
        BigInteger y = new BigInteger(stringY, 16);
        //通过公钥x、y分量创建椭圆曲线公钥规范
        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(x9ECParameters.getCurve().createPoint(x, y), ecDomainParameters);
        //通过椭圆曲线公钥规范，创建出椭圆曲线公钥对象（可用于SM2加密及验签）
        return new BCECPublicKey("EC", ecPublicKeySpec, BouncyCastleProvider.CONFIGURATION);
    }


    public static void main(String[] args) {
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
        try {
            keyPairGenerator.init(new ECKeyGenerationParameters(domainParameters, SecureRandom.getInstance("SHA1PRNG")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
        //私钥，16进制格式，自己保存
        BigInteger privatekey = ((ECPrivateKeyParameters) asymmetricCipherKeyPair.getPrivate()).getD();
        String privateKeyHex = privatekey.toString(16);
        System.out.println("private Key :" + privateKeyHex);

        //公钥，16进制格式，发给前端
        ECPoint ecPoint = ((ECPublicKeyParameters) asymmetricCipherKeyPair.getPublic()).getQ();
        String publicKeyHex = Hex.toHexString(ecPoint.getEncoded(false));
        System.out.println("Public Key :" + publicKeyHex);
    }

}
