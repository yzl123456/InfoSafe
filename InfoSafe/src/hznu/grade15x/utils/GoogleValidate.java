package hznu.grade15x.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
/*
 * 谷歌提供的算法
 * 下面讲讲我的大致理解:
 * 1.用户注册成功后第一次登录时，系统会自动生成一个16位纯数字的字符串 seed保存在服务端
 * 2.然后服务器端通过谷歌提供的算法createCredentials（seed） 创建了一个secretKey 密钥字符串，这个密钥字符串就是二维码URL后面所带的参数
 *   可以直接在谷歌APP上通过手动输入密钥来代替扫码环节
 * 3.在谷歌APP上输入完密钥后会有6位数的动态密码
 * 4.服务器端通过一开始为用户生成的独特的字符串seed 进行Base32加码  calculateCode(seed.getBytes(), new Date().getTime()/30000)
 * 	 返回一个result即为服务端的验证码
 * 5.服务端与谷歌APP段验证码一致则验证成功
 */
public class GoogleValidate {
	private static final String HMAC_HASH_FUNCTION = "HmacSHA1";

	
	/**
	 * 此为谷歌提供的算法，似乎用到了hash什么牛逼的东西
	 * 
	 * @param key key为服务器端一开始为用户生成的randomNumber
	 * @param tm  当前的时间  new Date().getTime()/30000 除以30000 表示30s一变
	 * @return  返回服务器端计算出来的动态密码
	 */
	static int calculateCode(byte[] key, long tm)
    {
        // Allocating an array of bytes to represent the specified instant
        // of time.
        byte[] data = new byte[8];
        long value = tm;

        // Converting the instant of time from the long representation to a
        // big-endian array of bytes (RFC4226, 5.2. Description).
        for (int i = 8; i-- > 0; value >>>= 8)
        {
            data[i] = (byte) value;
        }

        // Building the secret key specification for the HmacSHA1 algorithm.
        SecretKeySpec signKey = new SecretKeySpec(key, HMAC_HASH_FUNCTION);

        try
        {
            // Getting an HmacSHA1 algorithm implementation from the JCE.
            Mac mac = Mac.getInstance(HMAC_HASH_FUNCTION);

            // Initializing the MAC algorithm.
            mac.init(signKey);

            // Processing the instant of time and getting the encrypted data.
            byte[] hash = mac.doFinal(data);

            // Building the validation code performing dynamic truncation
            // (RFC4226, 5.3. Generating an HOTP value)
            int offset = hash[hash.length - 1] & 0xF;

            // We are using a long because Java hasn't got an unsigned integer type
            // and we need 32 unsigned bits).
            long truncatedHash = 0;

            for (int i = 0; i < 4; ++i)
            {
                truncatedHash <<= 8;

                // Java bytes are signed but we need an unsigned integer:
                // cleaning off all but the LSB.
                truncatedHash |= (hash[offset + i] & 0xFF);
            }

            // Clean bits higher than the 32nd (inclusive) and calculate the
            // module with the maximum validation code value.
            truncatedHash &= 0x7FFFFFFF;
            truncatedHash %= 1000000;

            // Returning the validation code to the caller.
            return (int) truncatedHash;
        }
        catch (NoSuchAlgorithmException | InvalidKeyException ex)
        {
            // Logging the exception.
//	            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);

            // We're not disclosing internal error details to our clients.
//	            throw new GoogleAuthenticatorException("The operation cannot be "
//	                    + "performed now.");
        }
		return 0;
    }
	
	 public static  String createCredentials(String randomNumber){
		   String sharedKey= new Base32().encode(randomNumber.getBytes());
		   System.out.println("-->"+sharedKey);
		   return sharedKey;
	 }
	
	
	
	/*
	 * main是当时用来测试的，把种子写死做的测试，注意种子唯一 生成的secretKey密钥也是唯一的
	 */
	public static void main(String[] args) {
		String seed="1234abcd1234abcd";//字母数字混合这个样例是可以的  其他的没试过了 不过不保险
//		String seed="abcdefghijkl"; 纯字母的试了10+次没一次成功的  纯数字的一定可以
		String pngs_key=createCredentials(seed);
		int result=calculateCode(seed.getBytes(), new Date().getTime()/30000);
		System.out.printf("%06d",result);
//		printf("%6d\n",result);
	}
	
	/*
	 * 返回服务器端生成的动态密码
	 */
	public static String getValidateCode(String secretKey) throws Exception{
		int result=calculateCode(Base32.decode(secretKey), new Date().getTime()/30000);
		String code = String.format("%06d", result);
		return code;
	}
	
}
