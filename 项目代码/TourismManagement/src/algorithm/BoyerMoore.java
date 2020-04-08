package algorithm;
/**
 * 方法类，包含一个BoyerMoore算法方法，做字符串匹配
 */
public class BoyerMoore {
    public static int match(String pattern, String target) {
        int tLen = target.length(); //目标字符串长度
        int pLen = pattern.length(); //输入字符串长度
        int[] bad_table = build_bad_table(pattern); //坏字符规则表
        int[] good_table = build_good_table(pattern);//好后缀规则表

        //先判断字符串长度能否比较
        if (pLen > tLen) {
            return -1;
        }

        //比较
        for (int i = pLen - 1, j; i < tLen;) {
            for (j = pLen - 1; target.charAt(i) == pattern.charAt(j); i--, j--) {
                if (j == 0) {
                    return i;
                }
            }

            //取好后缀和坏字符规则移位最大值
            i += Math.max(good_table[pLen - j - 1], bad_table[target.charAt(i)]);
        }
        return -1;
    }

    /**
     * 字符信息表，构建坏字符规则表
     */
    public static int[] build_bad_table(String pattern) {
        final int table_size = 40000;
        int[] bad_table = new int[table_size];
        int pLen = pattern.length();

        for (int i = 0; i < bad_table.length; i++) {
            bad_table[i] = pLen;  //默认初始化全部为匹配字符串长度
        }
        for (int i = 0; i < pLen - 1; i++) {
            int k = pattern.charAt(i);
            bad_table[k] = pLen - 1 - i;
        }

        return bad_table;
    }

    /**
     * 字符信息表，构建好后缀规则表
     */
    public static int[] build_good_table(String pattern) {
        int pLen = pattern.length();
        int[] good_table = new int[pLen];
        int lastPrefixPosition = pLen;

        for (int i = pLen - 1; i >= 0; --i) {
            if (isPrefix(pattern, i + 1)) {
                lastPrefixPosition = i + 1;
            }
            good_table[pLen - 1 - i] = lastPrefixPosition - i + pLen - 1;
        }

        for (int i = 0; i < pLen - 1; ++i) {
            int slen = suffixLength(pattern, i);
            good_table[slen] = pLen - 1 - i + slen;
        }
        return good_table;
    }

    /**
     * 前缀匹配
     */
    private static boolean isPrefix(String pattern, int p) {
        int patternLength = pattern.length();
        //前缀移位位数
        for (int i = p, j = 0; i < patternLength; ++i, ++j) {
            if (pattern.charAt(i) != pattern.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 后缀匹配
     */
    private static int suffixLength(String pattern, int p) {
        int pLen = pattern.length();
        int len = 0;
        //移位位数
        for (int i = p, j = pLen - 1; i >= 0 && pattern.charAt(i) == pattern.charAt(j); i--, j--) {
            len += 1;
        }
        return len;
    }
}
