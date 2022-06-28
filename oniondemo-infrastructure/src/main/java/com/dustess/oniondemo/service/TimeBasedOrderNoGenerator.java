package com.dustess.oniondemo.service;

import com.dustess.oniondemo.interfaces.OrderNoGenerator;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TimeBasedOrderNoGenerator implements OrderNoGenerator {

    private static int sequence = 0;
    private static int length = 6;

    public synchronized String getLocalTrmSeqNum() {
        sequence = sequence >= 999999 ? 1 : sequence + 1;
        String datetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String s = Integer.toString(sequence);
        return datetime + addLeftZero(s, length);
    }

    /**
     * 左填0
     * @author shijing
     * 2018年6月29日下午1:24:32
     * @param s
     * @param length
     * @return
     */
    public String addLeftZero(String s, int length) {
        // StringBuilder sb=new StringBuilder();
        int old = s.length();
        if (length > old) {
            char[] c = new char[length];
            char[] x = s.toCharArray();
            if (x.length > length) {
                throw new IllegalArgumentException(
                        "Numeric value is larger than intended length: " + s
                                + " LEN " + length);
            }
            int lim = c.length - x.length;
            for (int i = 0; i < lim; i++) {
                c[i] = '0';
            }
            System.arraycopy(x, 0, c, lim, x.length);
            return new String(c);
        }
        return s.substring(0, length);
    }

    @Override
    public String nextNo() {
        return getLocalTrmSeqNum();
    }
}
