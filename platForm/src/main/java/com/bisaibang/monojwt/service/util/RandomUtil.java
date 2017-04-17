package com.bisaibang.monojwt.service.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

/**
 * Utility class for generating random Strings.
 */
public final class RandomUtil {

    private static final int DEF_COUNT = 20;

    private RandomUtil() {
    }

    /**
     * Generates a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
     * Generates an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
     * Generates a reset key.
     *
     * @return the generated reset key
     */
    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    // bsb v2 migrate from v1
    public static String get6SMSCode() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(9) + 1);
        }
        return sb.toString();
    }

    public static String get6Payment() {
        return get6SMSCode();
    }

    public static String get4InviteCode() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(9) + 1);
        }
        return sb.toString();
    }

    public static String get11Phone() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }
        return "TES" + sb.toString();
    }

    public static String getUserDefaultUrl() {
        Random random = new Random();
        int i = random.nextInt(10);
        if (i < 3) {
            return "http://omhemfx8a.bkt.clouddn.com/av.jpg";
        } else if (i < 4) {
            return "http://omhemfx8a.bkt.clouddn.com/av1.jpg";
        } else if (i < 5) {
            return "http://omhemfx8a.bkt.clouddn.com/av2.jpg";
        } else if (i < 6) {
            return "http://omhemfx8a.bkt.clouddn.com/av3.jpg";
        } else if (i < 7) {
            return "http://omhemfx8a.bkt.clouddn.com/av4.jpg";
        } else if (i < 8) {
            return "http://omhemfx8a.bkt.clouddn.com/av5.jpg";
        } else {
            return "http://omhemfx8a.bkt.clouddn.com/av6.jpg";
        }
    }

    public static String getScore() {
        int persent = (int) (Math.random() * 100) % 10;
        if (persent <= 3) {
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            sb.append(random.nextInt(5));
            sb.append(":");
            sb.append(random.nextInt(5));
            return sb.toString();
        } else if (persent <= 8) {
            return "1:0";
        } else {
            return "waitSubmit";
        }
    }
}
