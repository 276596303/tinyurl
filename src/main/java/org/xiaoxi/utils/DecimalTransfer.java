package org.xiaoxi.utils;

/**
 * 10 进制与 62进制之间的转换
 * Created by YanYang on 2016/6/6.
 */
public final class DecimalTransfer {

    public static long shortUrlToID(String shortUrl) {
        long id = 0;
        for (int i = 0; i < shortUrl.length(); i++) {
            id = id * 62 + toBase62(shortUrl.charAt(i));
        }
        return id;
    }


    public static String idToShortUrl(long id) {
        StringBuilder sb = new StringBuilder();
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        while (id > 0) {
            long index = id - 62 * (id / 62);
            sb.append(chars.charAt((int)index));
            id /= 62;
        }
        while (sb.length() < 6) {
            sb.append('0');
        }
        sb.reverse();
        return sb.toString();
    }

    private static int toBase62(Character c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        } else if (c >= 'a' && c <= 'z') {
            return c - 'a' + 10;
        } else {
            return c - 'A' + 36;
        }
    }
}
