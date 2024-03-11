package cn.iocoder.yudao.framework.common.util.web3j;

import cn.iocoder.yudao.framework.common.util.crypto.ECCSign;
import cn.iocoder.yudao.framework.common.util.crypto.ECCUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes1;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
public class EthUtils {
    public static Web3j webContract;
    public static Long chainId;
    public static Web3j web3j = Web3j.build(new HttpService("https://bsc-dataseed1.binance.org"));
    public static Long web3jchainId = 56L;
    public static String busdContractAddress = "0x55d398326f99059fF775485246999027B3197955";
    public static String usdtContractAddress = "0x55d398326f99059fF775485246999027B3197955";
    public static String commissionContractOwner = "0x7efaef62fddcca950418312c6c91aef321375a00";
    public static String pancakeContractAddress = "0x10ED43C718714eb63d5aA57B78B54704E256024E";
    public static String zijinchiContractAddress = "0x7EFaEf62fDdCCa950418312c6C91Aef321375A00";
    public static String dogeContractAddress = "0xbA2aE424d960c26247Dd6c32edC70B295c744C43"; // doge代币
    public static String btcContractAddress = "0x7130d2A12B9BCbFAe4f2634d864A1Ee1Ce3Ead9c";
    public static String ethContractAddress = "0x2170Ed0880ac9A755fd29B2688956BD959F933F8";

    public static String treasuryAccount = "0x4DD6d6FA0F371870474a0CB34BC7281E831db958";
    public static String scanUrl;
    public static BigInteger startBlock = BigInteger.valueOf(29107967);

    public static BigInteger gasLimit = new BigInteger("500000");

    public static int dogeAccurate = 8;


    // 默认是单价是2.2GWEI,太高了，去10又太低了，所以动态去取gasPrice
//    public static BigInteger gasPrice = DefaultGasProvider.GAS_PRICE;
//    public static BigInteger gasPrice = Convert.toWei("10", Convert.Unit.GWEI).toBigInteger();
    // ps:Convert.Unit如果为GWEI代表为10个GWEI，如果为ETHER,代表为10个WZM币或10个ETH币
    public static String ethSendRawTransaction(String privateKey, Function function, String contractAddress) {
        try {
            Credentials credentials = Credentials.create(privateKey);

            String fromAddress = credentials.getAddress();

            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                    fromAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            String encodedFunction = FunctionEncoder.encode(function);
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce, getGasPrice(), gasLimit,
                    contractAddress, encodedFunction);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, web3jchainId, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            String transactionHash = null;
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
            log.error("代币广播结果" + JSON.toJSONString(ethSendTransaction));
            transactionHash = ethSendTransaction.getTransactionHash();
            return transactionHash;

        } catch (Exception e) {
            log.error("代币交易异常", e);
        }
        return null;
    }

    public static JSONObject getTransactionParam(String fromAddress, Function function, String contractAddress) {
        try {
            String encodedFunction = FunctionEncoder.encode(function);

            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                    fromAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce, getGasPrice(), gasLimit,
                    contractAddress, encodedFunction);
            JSONObject jo = JSONObject.parseObject(JSON.toJSONString(rawTransaction));
            jo.put("from", fromAddress);
            return jo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Type> getEthCall(Function function, String contractAddress) {
        try {
            String encodedFunction = FunctionEncoder.encode(function);
            Transaction transaction = Transaction.createEthCallTransaction(contractAddress, contractAddress, encodedFunction);
            EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
//            log.error("返回结果："+JSON.toJSONString(ethCall));
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            return results;
        } catch (Exception e) {
        }
        return null;
    }

    public static String swap(String privateKey, String toAddress, String usdtNum, String contractAddress) {
        try {
            Uint256 deadline = new Uint256(System.currentTimeMillis() / 1000 + 300);
            Uint256 amountIn = new Uint256(
                    Convert.toWei(usdtNum, Convert.Unit.ETHER).toBigInteger()
            );
            Uint256 amountOutMin = getAmountsOut(usdtNum, contractAddress);
            List<Address> addList = new ArrayList<Address>();
            addList.add(new Address(usdtContractAddress));
            addList.add(new Address(busdContractAddress));
            DynamicArray<Address> path = new DynamicArray<Address>(Address.class, addList) {
            };

            Function function = new Function(
                    "swapExactTokensForTokens",
                    Arrays.asList(amountIn, amountOutMin, path, new Address(toAddress), deadline),
                    Arrays.asList(new TypeReference<Type>() {
                    }));

            return ethSendRawTransaction(privateKey, function, contractAddress);
        } catch (Exception e) {
            log.error("交换异常", e);
        }
        return null;
    }

    public static Uint256 getAmountsOut(String usdtNum, String contractAddress) {
        try {
            Uint256 amountIn = new Uint256(
                    Convert.toWei(usdtNum, Convert.Unit.ETHER).toBigInteger()
            );
            List<Address> addList = new ArrayList<Address>();
            addList.add(new Address(usdtContractAddress));
            addList.add(new Address(busdContractAddress));
            DynamicArray<Address> path = new DynamicArray<Address>(Address.class, addList) {
            };
            List<Type> inputParameters = new ArrayList<>();
            inputParameters.add(amountIn);
            inputParameters.add(path);
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            outputParameters.add(new TypeReference<DynamicArray<Uint256>>() {
            });

            Function function = new Function(
                    "getAmountsOut",
                    inputParameters,
                    outputParameters);
            List<Type> results = getEthCall(function, contractAddress);
            /*
            [
                {
                    "componentType": "org.web3j.abi.datatypes.generated.Uint256",
                    "typeAsString": "uint256[]",
                    "value": [
                        {
                            "typeAsString": "uint256",
                            "value": "1000000000000000000"
                        },
                        {
                            "typeAsString": "uint256",
                            "value": "995067212438412742"
                        }
                    ]
                }
            ]
             */
            JSONArray value = JSON.parseArray(JSON.toJSONString(results.get(0).getValue()));
            String busdNum = value.getJSONObject(1).getString("value");
            return new Uint256(new BigInteger(busdNum));
        } catch (Exception e) {
            log.error("查询异常", e);
        }
        return null;
    }

    public static Uint256 getAmountsIn(BigDecimal usdtNum, String contractAddress) {
        try {
            Uint256 amountOut = new Uint256(
                    Convert.toWei(usdtNum, Convert.Unit.ETHER).toBigInteger()
            );
            List<Address> addList = new ArrayList<Address>();
            addList.add(new Address(busdContractAddress));
            addList.add(new Address(usdtContractAddress));
            DynamicArray<Address> path = new DynamicArray<Address>(Address.class, addList) {
            };
            List<Type> inputParameters = new ArrayList<>();
            inputParameters.add(amountOut);
            inputParameters.add(path);
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            outputParameters.add(new TypeReference<DynamicArray<Uint256>>() {
            });

            Function function = new Function(
                    "getAmountsIn",
                    inputParameters,
                    outputParameters);
            List<Type> results = getEthCall(function, contractAddress);

            JSONArray value = JSON.parseArray(JSON.toJSONString(results.get(0).getValue()));
            String busdNum = value.getJSONObject(0).getString("value");
            return new Uint256(new BigInteger(busdNum));
        } catch (Exception e) {
            log.error("查询异常", e);
        }
        return null;
    }


    public static BigDecimal getErc20Balance(String address, String contractAddress) {
        try {
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            outputParameters.add(new TypeReference<Uint256>() {
            });
            Function function = new Function(
                    "balanceOf",
                    Arrays.asList(new Address(address)),
                    outputParameters);
            List<Type> results = getEthCall(function, contractAddress);
            if (results.isEmpty()) {
                return BigDecimal.ZERO;
            }
            return uintToBigdeciml(String.valueOf(results.get(0).getValue()));
        } catch (Exception e) {
            log.error("查询异常", e);
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal getErc20Balance(int accurate, String address, String contractAddress) {
        try {
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            outputParameters.add(new TypeReference<Uint256>() {
            });
            Function function = new Function(
                    "balanceOf",
                    Arrays.asList(new Address(address)),
                    outputParameters);
            List<Type> results = getEthCall(function, contractAddress);
            if (results.isEmpty()) {
                return null;
            }
            BigInteger decimals = BigInteger.TEN.pow(accurate); // DOGE 代币精度为 8
            return new BigDecimal(String.valueOf(results.get(0).getValue())).divide(new BigDecimal(decimals));
        } catch (Exception e) {
            log.error("查询异常", e);
        }
        return null;
    }


    public static BigDecimal getErc20Allowance(String address, String contractAddress, String spender) {
        try {
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            outputParameters.add(new TypeReference<Uint256>() {
            });
            Function function = new Function(
                    "allowance",
                    Arrays.asList(new Address(address),
                            new Address(spender)),
                    outputParameters);
            List<Type> results = getEthCall(function, contractAddress);
            return uintToBigdeciml(String.valueOf(results.get(0).getValue()));
        } catch (Exception e) {
            log.error("查询异常", e);
        }
        return BigDecimal.ZERO;
    }

    public static BigDecimal getOrderStatus(Long orderNo, String contractAddress) {
        try {
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            outputParameters.add(new TypeReference<Uint256>() {
            });
            Function function = new Function(
                    "getOrderStatus",
                    Arrays.asList(new Uint256(orderNo)),
                    outputParameters);
            List<Type> results = getEthCall(function, contractAddress);
            /*[{"typeAsString": "uint256", "value": "100"  //还要判断金额，0位未支付}]*/
            if (results == null || results.isEmpty()) {
                System.out.println("EthUtil get order status error : " + orderNo);
                return BigDecimal.ZERO;
            }
            return uintToBigdeciml(String.valueOf(results.get(0).getValue()));
        } catch (Exception e) {
            log.error("查询异常", e);
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public static String getAccount(String contractAddress) {
        try {
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            outputParameters.add(new TypeReference<Address>() {
            });
            Function function = new Function(
                    "getAccount",
                    Arrays.asList(),
                    outputParameters);
            List<Type> results = getEthCall(function, contractAddress);
            /*
            [
                {
                    "typeAsString": "uint256",
                    "value": "100"  //还要判断金额，0位未支付
                }
            ]
             */
            return String.valueOf(results.get(0).getValue());
        } catch (Exception e) {
            log.error("查询异常", e);
        }
        return null;
    }

    /*
fromPrivateKey 发送者的私钥
toAddress 接收地址
发送多少代币,比如1个
*/
    public static JSONObject withdraw(String fromAddress, String toAddress, String usdtNum, String contractAddress, String tokenAddress) {
        try {
            Uint256 uint256 = new Uint256(
                    Convert.toWei(usdtNum, Convert.Unit.ETHER).toBigInteger()
            );

            Function function = new Function(
                    "withdraw",
                    Arrays.asList(new Address(toAddress), uint256, new Address(tokenAddress)),
                    Arrays.asList(new TypeReference<Type>() {
                    }));
            return getTransactionParam(fromAddress, function, contractAddress);
        } catch (Exception e) {
            log.error("代币交易异常", e);
        }
        return null;
    }

    public static JSONObject cashlp(String fromAddress, String toAddress, String usdtNum, String contractAddress) {
        try {
            Uint256 uint256 = new Uint256(
                    Convert.toWei(usdtNum, Convert.Unit.ETHER).toBigInteger()
            );

            Function function = new Function(
                    "withdrawLp",
                    Arrays.asList(new Address(toAddress), uint256, new Uint256(System.currentTimeMillis() / 1000 + 300)),
                    Arrays.asList(new TypeReference<Type>() {
                    }));
            return getTransactionParam(fromAddress, function, contractAddress);
        } catch (Exception e) {
            log.error("代币交易异常", e);
        }
        return null;
    }

    public static JSONObject setNoNeedQuotaAddress(String fromAddress, String toAddress, String contractAddress) {
        try {
            Function function = new Function(
                    "setNoNeedQuotaAddress",
                    Arrays.asList(new Address(toAddress)),
                    Arrays.asList(new TypeReference<Type>() {
                    }));
            return getTransactionParam(fromAddress, function, contractAddress);
        } catch (Exception e) {
        }
        return null;
    }

    public static JSONObject setSwapAdd(String fromAddress, String toAddress, String contractAddress) {
        try {

            Function function = new Function(
                    "setSwapAdd",
                    Arrays.asList(new Address(toAddress)),
                    Arrays.asList(new TypeReference<Type>() {
                    }));
            return getTransactionParam(fromAddress, function, contractAddress);
        } catch (Exception e) {
        }
        return null;
    }

    public static JSONObject setServicefeeAddress(String fromAddress, String tokenAddress, String contractAddress) {
        try {

            Function function = new Function(
                    "setServicefeeAddress",
                    Arrays.asList(new Address(tokenAddress)),
                    Arrays.asList(new TypeReference<Type>() {
                    }));
            return getTransactionParam(fromAddress, function, contractAddress);
        } catch (Exception e) {
        }
        return null;
    }

    public static JSONObject setSlipPointAddress(String fromAddress, String tokenAddress, Long status, String contractAddress) {
        try {

            Function function = new Function(
                    "setSlipPointAddress",
                    Arrays.asList(new Address(tokenAddress), new Uint256(status)),
                    Arrays.asList(new TypeReference<Type>() {
                    }));
            return getTransactionParam(fromAddress, function, contractAddress);
        } catch (Exception e) {
        }
        return null;
    }

    public static JSONObject setnoSlipPointAddress(String fromAddress, String tokenAddress, Long status, String contractAddress) {
        try {

            Function function = new Function(
                    "setNoSlipPointAddress",
                    Arrays.asList(new Address(tokenAddress), new Uint256(status)),
                    Arrays.asList(new TypeReference<Type>() {
                    }));
            return getTransactionParam(fromAddress, function, contractAddress);
        } catch (Exception e) {
        }
        return null;
    }

    public static JSONObject setIsCanOut(String fromAddress, Long status, String contractAddress) {
        try {

            Function function = new Function(
                    "setIsCanOut",
                    Arrays.asList(new Uint256(status)),
                    Arrays.asList(new TypeReference<Type>() {
                    }));
            return getTransactionParam(fromAddress, function, contractAddress);
        } catch (Exception e) {
        }
        return null;
    }

    public static JSONObject setAccount(String fromAddress, String tokenAddress, String contractAddress) {
        try {

            Function function = new Function(
                    "setAccount",
                    Arrays.asList(new Address(tokenAddress)),
                    Arrays.asList(new TypeReference<Type>() {
                    }));
            return getTransactionParam(fromAddress, function, contractAddress);
        } catch (Exception e) {
        }
        return null;
    }

    public static JSONObject setSignAccount(String fromAddress, String tokenAddress, String contractAddress) {
        try {

            Function function = new Function(
                    "setSignAccount",
                    Arrays.asList(new Address(tokenAddress)),
                    Arrays.asList(new TypeReference<Type>() {
                    }));
            return getTransactionParam(fromAddress, function, contractAddress);
        } catch (Exception e) {
        }
        return null;
    }

    public static JSONObject setRedAccount(String fromAddress, String tokenAddress, String contractAddress) {
        try {

            Function function = new Function(
                    "setRedAccount",
                    Arrays.asList(new Address(tokenAddress)),
                    Arrays.asList(new TypeReference<Type>() {
                    }));
            return getTransactionParam(fromAddress, function, contractAddress);
        } catch (Exception e) {
        }
        return null;
    }

    public static String approve(String privateKey, String toAddress, String usdtNum, String contractAddress) {
        try {

            Uint256 uint256 = new Uint256(
                    Convert.toWei(usdtNum, Convert.Unit.ETHER).toBigInteger()
            );

            Function function = new Function(
                    "approve",
                    Arrays.asList(new Address(toAddress), uint256),
                    Arrays.asList(new TypeReference<Type>() {
                    }));

            return ethSendRawTransaction(privateKey, function, contractAddress);

        } catch (Exception e) {
            log.error("授权异常", e);
        }
        return null;
    }

    public static JSONObject approveParam(String fromAddress, String toAddress, String contractAddress) {
        try {
            Uint256 uint256 = new Uint256(
                    Convert.toWei("115792089237316195423570985008687907853269984665640564039457584007913129639935", Convert.Unit.WEI).toBigInteger()
            );

            Function function = new Function(
                    "approve",
                    Arrays.asList(new Address(toAddress), uint256),
                    Arrays.asList(new TypeReference<Type>() {
                    }));

            return getTransactionParam(fromAddress, function, contractAddress);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getMoneyParam(String fromAddress, String amountIn, Long orderNo, String contractAddress) {
        try {
            Uint256 uintAmountIn = new Uint256(
                    Convert.toWei(amountIn, Convert.Unit.ETHER).toBigInteger()
            );
            Uint256 uintOrderNo = new Uint256(orderNo);

            Function function = new Function(
                    "getMoney",
                    Arrays.asList(uintAmountIn, uintOrderNo),
                    Arrays.asList(new TypeReference<Type>() {
                    }));

            return getTransactionParam(fromAddress, function, contractAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    // 除以这个WEI就是我们能看懂的具体数量
    public static final BigDecimal WEI = new BigDecimal("1000000000000000000");
    public static final BigDecimal DWEI = new BigDecimal("1000000000000000000"); // doge精度
    public static final BigDecimal USDTWEI = new BigDecimal("1000000");


    private static BigInteger getGasPrice() {
        BigInteger gasPrice = DefaultGasProvider.GAS_PRICE;
        try {
            EthGasPrice send = web3j.ethGasPrice().send();
            gasPrice = send.getGasPrice().add(Convert.toWei("1", Convert.Unit.GWEI).toBigInteger());
        } catch (Exception e) {
        }
        return gasPrice;
    }

    public static JSONObject cashOut(Long id, BigDecimal price, String accountAddress, String contractAddress, String privateKey) {
        try {
            Uint256 uintOrderNo = new Uint256(id);
            Uint256 amountOut = new Uint256(
                    Convert.toWei(price, Convert.Unit.ETHER).toBigInteger()
            );

            StringBuilder msg = new StringBuilder();
            msg.append(TypeEncoder.encode(uintOrderNo));
            msg.append(TypeEncoder.encode(amountOut));
            msg.append(accountAddress.substring(2));
            msg.append(contractAddress.substring(2));

            Sign.SignatureData sign = ECCSign.createSign(privateKey, msg);

            Function function = new Function(
                    "giveMoney",
                    Arrays.asList(uintOrderNo, amountOut, new Bytes32(sign.getR()), new Bytes32(sign.getS()), new Bytes1(sign.getV())),
                    Arrays.asList(new TypeReference<Type>() {
                    }));
            return getTransactionParam(accountAddress, function, contractAddress);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("cashOut 异常", e);
        }
        return null;
    }

//    public static JSONObject swap(TContract contract, Long orderNo, String type,
//                                  String fromAddress, BigDecimal amountIn, BigDecimal amountOut, Uint256 expireTimer) {
//        try {
//            Uint256 uintOrderNo = new Uint256(orderNo);
//            Uint256 amountOutuint = new Uint256(
//                    Convert.toWei(amountOut, Convert.Unit.ETHER).toBigInteger()
//            );
//            Uint256 amountInuint = new Uint256(
//                    Convert.toWei(amountIn, Convert.Unit.ETHER).toBigInteger()
//            );
//            StringBuilder msg = new StringBuilder();
//            msg.append(TypeEncoder.encode(uintOrderNo));
//            msg.append(TypeEncoder.encode(amountOutuint));
//            msg.append(TypeEncoder.encode(amountInuint));
//            msg.append(TypeEncoder.encode(expireTimer));
//            msg.append(fromAddress.substring(2));
//            msg.append(contract.getContractAddress().substring(2));
//            Sign.SignatureData sign = SignTuoyuan.createSign(TuofengUtils.javaSm2De(contract.getSignPrivateKey()), msg);
//            System.out.println("==================swap====================");
//            System.out.println("uintOrderNo " + TypeEncoder.encode(uintOrderNo));
//            System.out.println("amountInuint " + TypeEncoder.encode(amountInuint));
//            System.out.println("amountOutuint " + TypeEncoder.encode(amountOutuint));
//            System.out.println("expireTimer " + TypeEncoder.encode(expireTimer));
//            System.out.println("===");
//            Function function = new Function(
//                    "swapHtt",
//                    Arrays.asList(uintOrderNo, amountInuint, amountOutuint, expireTimer
//                            , new Bytes32(sign.getR()), new Bytes32(sign.getS()), new Bytes1(sign.getV())),
//                    Arrays.asList(new TypeReference<Type>() {
//                    }));
//            if (type.equalsIgnoreCase("swap_usdt")) {
//                function = new Function(
//                        "swapUsdt",
//                        Arrays.asList(uintOrderNo, amountInuint, amountOutuint, expireTimer),
//                        Arrays.asList(new TypeReference<Type>() {
//                        }));
//            }
//
//            return getTransactionParam(fromAddress, function, contract.getContractAddress());
//
//        } catch (Exception e) {
//            log.error("签名异常", e);
//        }
//        return null;
//    }

    public static BigDecimal getSwapAmountsOut(BigDecimal num, BigDecimal usdtNum, BigDecimal tokenNum, String type) {
        try {
            BigDecimal k = usdtNum.multiply(tokenNum);
            if (type.equalsIgnoreCase("usdtToToken")) {
                usdtNum = usdtNum.add(num);
                BigDecimal hasTokenNum = k.divide(usdtNum, 6, RoundingMode.DOWN);
                return tokenNum.subtract(hasTokenNum);
            }
            if (type.equalsIgnoreCase("tokenToUsdt")) {
                tokenNum = tokenNum.add(num);
                BigDecimal hasUsdtNum = k.divide(tokenNum, 6, RoundingMode.DOWN);
                return usdtNum.subtract(hasUsdtNum);
            }
        } catch (Exception e) {
            log.error("查询异常", e);
        }
        return null;
    }

    public static BigDecimal getSwapAmountsIn(BigDecimal num, BigDecimal usdtNum, BigDecimal tokenNum, String type) {
        try {
            BigDecimal k = usdtNum.multiply(tokenNum);
            if (type.equalsIgnoreCase("usdtToToken")) {
                tokenNum = tokenNum.subtract(num);
                BigDecimal hasUsdtNum = k.divide(tokenNum, 6, RoundingMode.DOWN);
                BigDecimal needUsdt = hasUsdtNum.subtract(usdtNum);
                return needUsdt.divide(num, 6, RoundingMode.DOWN);
            }
            if (type.equalsIgnoreCase("tokenToUsdt")) {
                usdtNum = usdtNum.subtract(num);
                BigDecimal hasTokenNum = k.divide(usdtNum, 6, RoundingMode.DOWN);
                BigDecimal needToken = hasTokenNum.subtract(tokenNum);
                return needToken.divide(num);
            }
        } catch (Exception e) {
            log.error("查询异常", e);
        }
        return null;
    }


    public static Long getBlockTimer() {
        try {
            BigInteger latestBlockNum = web3j.ethBlockNumber().send().getBlockNumber();

            // 查询指定block 信息(这里用刚刚获取的blockId示范)
            // 第二个参数表示是否返回区块中的交易数据
            EthBlock.Block block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(latestBlockNum), true)
                    .send()
                    .getBlock();
            Long timer = Long.valueOf(String.valueOf(block.getTimestamp()));
            return timer * 1000;
        } catch (Exception e) {

        }
        return null;
    }

    public static BigDecimal getTokenBurnNum(String contractAddress) {
        try {
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            outputParameters.add(new TypeReference<Uint256>() {
            });
            Function function = new Function(
                    "getBurnNum",
                    Arrays.asList(),
                    outputParameters);
            List<Type> results = getEthCall(function, contractAddress);
            return uintToBigdeciml(String.valueOf(results.get(0).getValue()));
        } catch (Exception e) {
            log.error("查询异常", e);
        }
        return null;
    }


    public static BigDecimal uintToBigdeciml(String value) {
        return new BigDecimal(value).divide(WEI, 8, RoundingMode.HALF_DOWN);
    }

    public static String reduceSwap(String privateKey, String contractAddress) {
        try {
            Function function = new Function(
                    "reduceSwap",
                    Arrays.asList(),
                    Arrays.asList(new TypeReference<Type>() {
                    }));
            return ethSendRawTransaction(privateKey, function, contractAddress);
        } catch (Exception e) {
            log.error("签名异常", e);
        }
        return null;
    }

    public static BigDecimal getCanReduce(String contractAddress) {
        try {
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            outputParameters.add(new TypeReference<Uint256>() {
            });
            Function function = new Function(
                    "getCanReduce",
                    Arrays.asList(),
                    outputParameters);
            List<Type> results = getEthCall(function, contractAddress);
            if (results.isEmpty()) {
                return null;
            }
            return new BigDecimal(String.valueOf(results.get(0).getValue()));
        } catch (Exception e) {
            log.error("查询异常", e);
        }
        return null;
    }

    // 复投续租
    public static String getFakeMoney(BigDecimal number, Long orderNo, String privateKey, String contractAddress) {
        try {

            Uint256 amountIn = new Uint256(
                    Convert.toWei(number, Convert.Unit.ETHER).toBigInteger()
            );
            Uint256 uintOrderNo = new Uint256(orderNo);
            Uint256 deadline = new Uint256(System.currentTimeMillis() / 1000 + 300);


            List<TypeReference<?>> outputParameters = new ArrayList<>();
            outputParameters.add(new TypeReference<Uint256>() {
            });
            Function function = new Function(
                    "getFakeMoney",
                    Arrays.asList(amountIn, uintOrderNo, deadline),
                    outputParameters);
            return ethSendRawTransaction(privateKey, function, contractAddress);
        } catch (Exception e) {
            log.error("getFakeMoney 异常", e);
            e.printStackTrace();
        }
        return null;
    }

    public static String release(String privateKey, String contractAddress) {
        try {
            Function function = new Function(
                    "release",
                    Arrays.asList(),
                    Arrays.asList(new TypeReference<Type>() {
                    }));

            return ethSendRawTransaction(privateKey, function, contractAddress);

        } catch (Exception e) {
            log.error("代币交易异常", e);
        }
        return null;
    }

    // 获取币价
    public static BigDecimal GetSymbolPrice(String contractAddress, String tokenAddress) {
        BigDecimal usdtNum = getErc20Balance(contractAddress, usdtContractAddress);
        BigDecimal tokenNum = getErc20Balance(contractAddress, tokenAddress);
        if (usdtNum == null || tokenNum == null) {
            return BigDecimal.ZERO;
        }
        if (usdtNum.compareTo(BigDecimal.ZERO) == 0 || tokenNum.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return usdtNum.divide(tokenNum, 6, RoundingMode.UP);
    }

//    public static String sendWithdraw(String toAddress, String num, String token, TContract contract) {
//        try {
//
//            BigDecimal readableBalance = new BigDecimal(num); // 可读的 DOGE 余额
//            BigInteger decimals = BigInteger.TEN.pow(8); // DOGE 代币精度为 8
//            BigInteger actualBalance = readableBalance.multiply(new BigDecimal(decimals)).toBigInteger();
//
//            Function function = new Function(
//                    "withdraw",
//                    Arrays.asList(new Address(toAddress), new Uint256(actualBalance), new Address(token)),
//                    Arrays.asList(new TypeReference<Type>() {
//                    }));
//
//            String contractAddress = contract.getContractAddress();
//            String privateKey = TuofengUtils.javaSm2De(contract.getSignPrivateKey());
//            return ethSendRawTransaction(privateKey, function, contractAddress);
//
//        } catch (Exception e) {
//            log.error("代币交易异常", e);
//        }
//        return "";
//    }

//    public static JSONObject transferFrom(TContract contract, TUserMoneyInout moneyInout) {
//        try {
//            Uint256 amount = new Uint256(
//                    Convert.toWei(moneyInout.getChangeMoney(), Convert.Unit.ETHER).toBigInteger()
//            );
//
//            Function function = new Function(
//                    "transferFrom",
//                    Arrays.asList(new Address(moneyInout.getCashoutAccount()), new Address(moneyInout.getCashoutType()), amount),
//                    Arrays.asList(new TypeReference<Type>() {
//                    }));
//
//            return getTransactionParam(moneyInout.getCashoutAccount(), function, contract.getContractAddress());
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("transferFrom 异常", e);
//        }
//        return null;
//    }

    public static JSONObject transfer(String fromAddress, String toAddress, String num, String contractAddress) {
        try {
            Uint256 amount = new Uint256(
                    Convert.toWei(num, Convert.Unit.ETHER).toBigInteger()
            );

            Function function = new Function(
                    "transfer",
                    Arrays.asList(new Address(toAddress), amount),
                    Arrays.asList(new TypeReference<Type>() {
                    }));

            return getTransactionParam(fromAddress, function, contractAddress);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("transferFrom 异常", e);
        }
        return null;
    }


    public static void main(String[] args) {
//        EthUtils.web3j = Web3j.build(new HttpService("https://bsc-dataseed1.binance.org"));
//        EthUtils.web3jchainId = 56L;
//
//        EthUtils.busdContractAddress = "0xe9e7CEA3DedcA5984780Bafc599bD69ADd087D56";
//        EthUtils.usdtContractAddress = "0x55d398326f99059fF775485246999027B3197955";
//        EthUtils.commissionContractOwner = "0x7efaef62fddcca950418312c6c91aef321375a00"; // 抽成地址
//        EthUtils.pancakeContractAddress = "0x10ED43C718714eb63d5aA57B78B54704E256024E";
//        EthUtils.zijinchiContractAddress = "0x7EFaEf62fDdCCa950418312c6C91Aef321375A00";
//        EthUtils.scanUrl = "https://api.bscscan.com/api";
//        EthUtils.startBlock = BigInteger.valueOf(29107967);
//
////        BigDecimal erc20Balance = getErc20Balance("0x5dbe059196cd5856a03adf3ec5a9a66efff6e246", "0x55d398326f99059fF775485246999027B3197955");
////        System.out.println(erc20Balance);
//
//        JSONObject jsonObject = approveParam("0x5dbe059196cd5856a03adf3ec5a9a66efff6e246", "0x7efaef62fddcca950418312c6c91aef321375a00", String.valueOf(BigDecimal.ZERO), "0x55d398326f99059fF775485246999027B3197955");
//        System.out.println(jsonObject);

    }

}
