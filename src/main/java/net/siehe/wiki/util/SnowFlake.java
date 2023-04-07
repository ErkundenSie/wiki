package net.siehe.wiki.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Twitter的分布式自增ID雪花算法
 * 时间戳+机器码+递增的序列号
 * 雪花算法的组成部分：时间戳、数据中心、机器中心、序列号
 * 对于数据中心、机器中心不能写死
 **/
@Component
public class SnowFlake {

    /**
     * 起始的时间戳
     */
    private final static long START_STMP = 1672531200000L; // 2023-01-01 00:00:00

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12; //整个方法采用位运算，序列号占用的位数
    private final static long MACHINE_BIT = 5;   //机器标识占用的位数最多2^5个机器
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId = 1;  //数据中心
    private long machineId = 1;     //机器标识
    private long sequence = 0L; //序列号
    private long lastStmp = -1L;//上一次时间戳

    public SnowFlake() {
    }

    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp(); //取一个时间戳与定义开始的时间戳
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增，最多生成2^12id（前面定义了12）
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | datacenterId << DATACENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | sequence;                             //序列号部分
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) throws ParseException {
        // 时间戳
//        System.out.println(System.currentTimeMillis());
//        System.out.println(new Date().getTime());
//
//        String dateTime = "2023-01-01 08:00:00";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        System.out.println(sdf.parse(dateTime).getTime());

        /**
         * 实例
         * 对于对于数据中心、机器中心不能写死
         * 实际new SnowFlake(1, 1);中是变化的有解决方案
         * eg1将配置写成一个配置文件放到机器里，程序初始化启动后，就会去读取这个机器配置文件，将它写到内存里面，以此达到数据中心、机器中心不一样，都是你自己分配的
         * eg2 通过分布式缓存redis,通过一些算法，机器启动时自动到Redis里面去获取一个唯一的不重复的数据中心、机器中心
         * new SnowFlake(1, 1);实际中用注入的写法将这个类交给spring管理
         */

        SnowFlake snowFlake = new SnowFlake(1, 1);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            System.out.println(snowFlake.nextId());
            System.out.println(System.currentTimeMillis() - start);
        }
    }
}
