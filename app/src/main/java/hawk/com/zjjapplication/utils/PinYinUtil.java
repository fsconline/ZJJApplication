
package hawk.com.zjjapplication.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;

public class PinYinUtil {
    public static final int[] secPosValueList = new int[]{1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5600};
    public static final char[] firstLetter = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z'};
    private static final int GB_SP_DIFF = 160;

    public PinYinUtil() {
    }

    public static String converterFromPinYinFirstLetter(String characters) {
        if (TextUtils.isEmpty(characters)) {
            return "";
        } else {
            char ch = characters.charAt(0);
            if (ch >> 7 == 0) {
                return characters;
            } else {
                Character spell = getFirstLetter(ch);
                return String.valueOf(spell);
            }
        }
    }

    public static boolean isHanZi(String character) {
        if (TextUtils.isEmpty(character)) {
            return false;
        } else {
            char ch = character.charAt(0);
            return ch >> 7 != 0;
        }
    }

    public static String converterFromPinYinSpell(String characters) {
        StringBuffer buffer = new StringBuffer();
        if (TextUtils.isEmpty(characters)) {
            buffer.append("");
        } else {
            for (int i = 0; i < characters.length(); ++i) {
                char ch = characters.charAt(i);
                if (ch >> 7 == 0) {
                    buffer.append(String.valueOf(ch));
                } else {
                    Character spell = getFirstLetter(ch);
                    if (spell != null && !spell.equals(Character.valueOf('-'))) {
                        buffer.append(String.valueOf(spell));
                    }
                }
            }
        }

        return buffer.toString();
    }

    public static Character getFirstLetter(char ch) {
        Object uniCode = null;

        byte[] uniCode1;
        try {
            uniCode1 = String.valueOf(ch).getBytes("GBK");
        } catch (UnsupportedEncodingException var3) {

            return null;
        }

        return uniCode1[0] > 0 ? null : Character.valueOf(convert(uniCode1));
    }

    private static char convert(byte[] bytes) {
        char result = 45;
        boolean secPosValue = false;

        int i;
        for (i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte) (bytes[i] - 160);
        }

        int var4 = bytes[0] * 100 + bytes[1];

        for (i = 0; i < 23; ++i) {
            if (var4 >= secPosValueList[i] && var4 < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }

        return result;
    }
}
