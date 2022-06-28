package com.dustess.oniondemo.service;

import cn.hutool.core.util.StrUtil;
import com.dustess.oniondemo.interfaces.IDGenerator;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SnowflakeIDGenerator implements IDGenerator {

    /**
     * SnowFlake的结构如下(每部分用-分开):<br>
     * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
     * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
     * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
     * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。
     * 41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
     * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
     * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
     * 加起来刚好64位，为一个Long型。<br>
     * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，
     * 经测试，SnowFlake每秒能够产生26万ID左右。
     */

    private static final List<String> FILTER_DEFAULT_IP = new ArrayList<>(Arrays.asList("127.0.0.1", "localhost"));

    /**
     * 开始时间截 (2020-1-1)
     */
    private static final long START_TIME = 1577808000000L;

    /**
     * 机器id所占的位数
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 数据标识id所占的位数
     */
    private static final long DATA_CENTER_ID_BITS = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private static final long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);

    /**
     * 序列在id中占的位数
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 机器ID向左移12位
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

    /**
     * 工作机器ID(0~31)
     */
    private static int workerId;

    /**
     * 数据中心ID(0~31)
     */
    private static int dataCenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private static long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private static long lastTimestamp = -1L;

    /**
     * ip正则表达式
     */
    private static String ipPattern = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\." +
            "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    @Override
    public synchronized Long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - START_TIME) << TIMESTAMP_LEFT_SHIFT)
                | (getDataId() << DATA_CENTER_ID_SHIFT)
                | (getWorkId() << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public int getWorkId() {
        try {
            if (workerId <= 0) {
                workerId = getHostId(Inet4Address.getLocalHost().getHostAddress(), MAX_WORKER_ID);
            }
        } catch (Exception e) {
            return new Random().nextInt((int) MAX_WORKER_ID + 1);
        }
        return workerId;
    }

    public int getDataId() {
        try {
            if (dataCenterId <= 0) {
                dataCenterId = getHostId(Inet4Address.getLocalHost().getHostAddress(), MAX_DATA_CENTER_ID);
            }
        } catch (Exception e) {
            return new Random().nextInt((int) MAX_DATA_CENTER_ID + 1);
        }
        return dataCenterId;
    }

    public int getHostId(String s, Long max) throws UnknownHostException, UnsupportedEncodingException {
        //去除获取不到ip地址的情况
        if (StrUtil.isBlank(s) || FILTER_DEFAULT_IP.contains(s)) {
            throw new UnknownHostException();
        }
        //去除非ip地址的情况
        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher(ipPattern);
        if (!matcher.matches()) {
            throw new UnknownHostException();
        }
        byte[] bytes = s.getBytes("UTF-8");
        int sums = 0;
        for (byte b : bytes) {
            sums += b;
        }
        return sums % (max.intValue() + 1);
    }
}
