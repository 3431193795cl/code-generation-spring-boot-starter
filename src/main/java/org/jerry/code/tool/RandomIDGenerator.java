package org.jerry.code.tool;

import com.mifmif.common.regex.Generex;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class RandomIDGenerator {

    // 随机生成地区编码
    public static String randomRegionCode() {
        Random random = new Random();
        int regionCode = random.nextInt(1000000);
        return String.format("%06d", regionCode);
    }

    // 随机生成出生年月日
    public static String randomBirthDate() {
        Random random = new Random();
        int year = random.nextInt(100) + 1900;
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1;
        return String.format("%04d%02d%02d", year, month, day);
    }

    // 随机生成顺序码
    public static String randomSequenceCode() {
        Random random = new Random();
        int sequenceCode = 100 + random.nextInt(900);
        return String.format("%02d", sequenceCode);
    }

    // 计算校验码
    public static char calculateCheckCode(String id) {
        if (id == null || id.length() != 17 || !id.matches("[0-9]+")) {
            log.error("Invalid ID: " + id);
            throw new IllegalArgumentException("Invalid ID length or format");
        }

        String[] checkCodes = new String[]{"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        int[] factors = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        int sum = 0;

        for (int i = 0; i < 17; ++i) {
            sum += (id.charAt(i) - '0') * factors[i];
        }

        int index = sum % 11;
        return checkCodes[index].charAt(0);
    }


    // 生成随机的身份证号
    public static String generateRandomID() {
        String regionCode = randomRegionCode();
        String birthDate = randomBirthDate();
        String sequenceCode = randomSequenceCode();
        String id = regionCode + birthDate + sequenceCode;
        char checkCode = calculateCheckCode(id);
        return id + checkCode;
    }

}