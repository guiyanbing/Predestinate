/**
 *
 */
package com.juxin.library.enc;

import com.juxin.library.log.PLogger;
import com.juxin.library.utils.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5编码处理。
 *
 * @author JohnsonLi
 * @version 1.0
 * @qq 505214658
 * @date 2015-04-08
 */
public class MD5 {

    private static final String ALGORITHM = "MD5";

    private static char sHexDigits[] = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'
    };
    private static MessageDigest sDigest;

    static {
        try {
            sDigest = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            PLogger.d("Get MD5 Digest failed.");
        }
    }

    private MD5() {
    }

    final public static String encode(String source) {
        byte[] btyes = source.getBytes();
        byte[] encodedBytes = sDigest.digest(btyes);

        return StringUtils.hexString(encodedBytes);
    }

    public final static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public final static int getMD5(String source) {
        try {
            return source.hashCode();
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }

        return 0;
    }
}
