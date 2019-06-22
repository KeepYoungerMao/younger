package com.mao.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串方面：StringUtils简写
 * 已有功能：
 * BASE64加密、解密；byte转string；MD5加密；SHA加密；最大匹配替换；最小匹配替换；
 * 获取随机数；获取时间戳；判断空值；字符串反转；判断字符串是否对称；字符串数组拼接；
 * 判断是否是数字；判断是否是身份证号码；判断是否是手机号码
 * 其它方法借鉴：
 * 1.字符串数组排序:按照字典顺序进行排序
 * @author mao by 11:14 2019/6/18
 */
public class SU {

    /**
     * MD5算法类型
     */
    private static final String KEY_MD5 = "MD5";

    /**
     * SHA-1算法类型
     */
    private static final String KEY_SHA = "SHA";

    /**
     * 随机字符串的默认长度
     */
    private static final int RANDOM_STRING_LENGTH = 32;

    /**
     * a empty string
     */
    private static final String EMPTY = "";

    /**
     * 身份证权重因子
     */
    private static final int[] POWER = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

    /**
     * 手机号 正则表达式
     */
    private static final String PHONE_REGEX =
            "^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(166)|(17[0135678])|(18[0-9])|(19[89]))\\d{8}$";

    /**
     * Email 正则表达式
     */
    private static final String EMAIL_REGEX = "(?:(?:[A-Za-z0-9\\-_@!#$%&'*+/=?" +
            "^`{|}~]|(?:\\\\[\\x00-\\xFF]?)|(?:\"[\\x00-\\xFF]*\"))+(?:\\.(?:(?:[A-Za-z0-9\\-" +
            "_@!#$%&'*+/=?^`{|}~])|(?:\\\\[\\x00-\\xFF]?)|(?:\"[\\x00-\\xFF]*\"))+)*)" +
            "@(?:(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+" +
            "(?:(?:[A-Za-z0-9]*[A-Za-z][A-Za-z0-9]*)(?:[A-Za-z0-9-]*[A-Za-z0-9])?))";

    /**
     * BASE64加密：严格来说属于编码格式，并非加密算法。
     * 加密后产生的字节数是8的倍数，如果不够位数以“=”符号补充。
     * 常用于邮件，HTTP加密，截取HTTP信息
     * @param key byte[]
     * @return String
     */
    public static String BASE64Encode(byte[] key){
        return new BASE64Encoder().encodeBuffer(key);
    }

    /**
     * BASE64加密：传String
     * @param key String
     * @return String
     */
    public static String BASE64Encode(String key){
        return new BASE64Encoder().encodeBuffer(key.getBytes());
    }

    /**
     * BASE64解密
     * @param key String
     * @return byte[]
     */
    public static byte[] BASE64Decode(String key){
        byte[] bytes = new byte[0];
        try {
            bytes = new BASE64Decoder().decodeBuffer(key);
        }catch (IOException e){
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * byte[]转String
     * @param bytes byte[]
     * @return String
     */
    public static String byteToString(byte[] bytes){
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * MD5加密：Message-Digest Algorithm(信息摘要算法)
     * 单向加密；生成固定长度加密值（十六进制字符串）
     * MD5加密的特点：
     * 1.压缩性：任意长度的数据，算出的MD5值长度是固定的；
     * 2.易计算：从原始数据计算出MD5值很容易；
     * 3.抗修改性：原始数据作出任意改动，得出的MD5值的差别很大；
     * 4.弱抗碰撞：已知原始数据和MD5值，找到一个具有相同MD5值得数据是非常困难的；
     * 5.强抗碰撞：找2个不同的原始数据，使它们具有相同的MD5值是非常困难的；
     * @param str str
     */
    public static String MD5(String str){
        //md5
        return encrypt(str,KEY_MD5);
    }

    /**
     * SHA加密：Secure Hash Algorithm（安全散列算法）
     * SHA算法被广泛应用到电子商务信息安全领域，虽然SHA和MD5都通过碰撞法破解了，
     * 但SHA任然是公认的安全加密算法，较MD5更安全。
     * SHA-1与MD5的区别：
     * 1.二者都由MD4导出，二者很类似。因此二者的强度和其它特性都很类似。
     * 2.SHA-1的摘要长度要比MD5长32位，使用强行技术，产生任何一个报文要等于给定报文摘要的难度对
     *   MD5是2^128数量级的操作，而对SHA-1是2^160数量级的操作，因此对SHA-1强行攻击的难度更大。
     * 3.相同硬件上，SHA-1的运算速度比MD5慢。
     * @param str String
     * @return String
     */
    public static String SHA(String str){
        //sha1 as same
        return encrypt(str,KEY_SHA);
    }

    /**
     * 加密方法：MD5和SHA加密方法相同，MessageDigest类中已分配好
     * @param str String
     * @param type 加密类型
     * @return String
     */
    private static String encrypt(String str, String type){
        byte[] bytes = new byte[0];
        try{
            bytes = MessageDigest.getInstance(type).digest(str.getBytes());
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return new BigInteger(1,bytes).toString(16);
    }

    /**
     * Min-Replace:最小匹配替换
     * map为替换数据，替换文本text中相同的字符串。
     * 最小匹配：替换过的文本区域不能再次替换（相同区域替换次数<=1）
     * introduction:
     * 替换成功后，为该替换的字符串记录一个在文本text中的坐标（indexOf），
     * 当下一个替换词进来匹配替换时，判断本次替换的字符串的坐标是否在已经匹配成功的字符串的坐标的范围内，
     * 如果在范围内则不进行匹配。直到替换结束。
     * 其中，后者匹配成功的字符串可能会对前面匹配成功的字符串的坐标有影响，因此每次匹配替换成功后，
     * 需要判断，该次替换的字符串的坐标的后面是否有替换成功的字符串，如果有，需要修订其坐标：
     *                5  6              <==  已替换的字符串区间“5-6”
     * 你 好 ， 我 是 张 三 。
     * -----------------------------    本次替换：我 ==> 我的名字
     *
     *          3        6     8  9     <==  上一次的区间“5-6”需要修订为“8-9”
     * 你 好 ， 我 的 名 字 是 张 三 。
     *                                  本次替换：名字 ==> 姓名  ==> “名字”在区间“3-6”中，不替换
     * @param text text
     * @param map to trans
     * @return text which had translated
     */
    public static String minReplace(String text, Map<String, String> map){
        List<int[]> has = new ArrayList<>();
        for (Map.Entry<String,String> entry : map.entrySet()){
            String phase = entry.getKey();
            if (text.contains(phase)){
                int begin = text.indexOf(phase);
                int end = begin+phase.length();
                boolean flag = true;
                if (has.size() > 0){
                    for (int[] i : has){
                        int t_begin = i[0];
                        int t_end = i[1];
                        if ( (begin > t_begin && begin < t_end) || (end > t_begin && end < t_end) ){
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag){
                    String trans = entry.getValue();
                    text = text.replaceAll(phase,trans);
                    int step = trans.length() - phase.length();
                    end += step;
                    if (has.size() > 0){
                        for (int[] i : has){
                            if (i[0] > begin){
                                i[0] += step;
                                i[1] += step;
                            }
                        }
                    }
                    has.add(new int[]{begin,end});
                }
            }
        }
        return text;
    }

    /**
     * Max-Replace:最大匹配替换
     * 循环遍历替换的值，匹配成功后，对文本text进行替换，
     * 替换成功后，进行下一循环，对替换成功后的文本text再次匹配替换
     * 这样可能会出现：
     * 1.替换后文本中有新的词汇会对后者的匹配结果产生很大的影响
     * 2.匹配的顺序的不同也会对结果有很大的影响：因此建议使用LinkedHashMap作为参数
     * @param text text
     * @param map to trans,LinkedHashMap is better
     * @return text which had translated
     */
    public static String maxReplace(String text, Map<String, String> map){
        for (Map.Entry<String, String> entry : map.entrySet()){
            if (text.contains(entry.getKey())){
                text = text.replaceAll(entry.getKey(),entry.getValue());
            }
        }
        return text;
    }

    /**
     * get a random string
     * the length of the return string is the specified length.
     * if the specified length lass than 1:
     *   it will throw NullPointerException
     * if the specified length equals the total length which length of
     * prefix and length of suffix length:
     *   direct return "prefix + suffix"
     * if the total length greater than the specified length:
     *   it will throw StringIndexOutOfBoundsException
     * the final string returned:
     *   prefix + the random string(length:specified length - total length) + suffix
     * @see UUID
     * @param prefix prefix
     * @param suffix suffix
     * @param length length
     * @return random string
     */
    public static String randomString(String prefix, String suffix, int length){
        if (length <= 0)
            throw new NullPointerException("length must greater than 0.");
        int init_len = 0;
        if (isNotEmpty(prefix))
            init_len += prefix.length();
        else
            prefix = "";
        if (isNotEmpty(suffix))
            init_len += suffix.length();
        else
            suffix = "";
        if (init_len == length)
            return prefix + suffix;
        if (init_len > length)
            throw new StringIndexOutOfBoundsException
                    ("Additional strings must not exceed the specified length.");
        return prefix + UUIDString(length - init_len) + suffix;
    }

    /**
     * get a random string with prefix and suffix
     * the length of the return string is the total length which
     * default length(32),length of prefix and length of suffix
     * @param prefix prefix
     * @param suffix suffix
     * @return random string
     */
    public static String randomString(String prefix, String suffix){
        //default length
        int len = RANDOM_STRING_LENGTH;
        if (isNotEmpty(prefix))
            len += prefix.length();
        if (isNotEmpty(suffix))
            len += suffix.length();
        return randomString(prefix, suffix,len);
    }

    /**
     * get a random string with a prefix.
     * the length of the return string is the specified length.
     * if the length of the prefix greater than specified length:
     *  it will throw StringIndexOutOfBoundsException
     * @param prefix prefix
     * @param length the specified length
     * @return random string
     */
    public static String randomString(String prefix, int length){
        //default suffix
        return randomString(prefix,null,length);
    }

    /**
     * get a random string with a prefix
     * the length of the returned string is total length
     * which the default length(32) and the length of prefix.
     * @param prefix prefix
     * @return random string
     */
    public static String randomString(String prefix){
        //default suffix,length
        int len = RANDOM_STRING_LENGTH;
        if (isNotEmpty(prefix))
            len += prefix.length();
        return randomString(prefix,null,len);
    }

    /**
     * get a random string which specified length
     * the length need greater than 0. if not
     * it will throw NullPointerException.
     * @param length specified length
     * @return random string
     */
    public static String randomString(int length){
        //default prefix,suffix
        return randomString(null,null,length);
    }

    /**
     * get a random string
     * the default length is 32
     * @return random string
     */
    public static String randomString(){
        //default prefix,suffix,length
        return randomString(null,null,RANDOM_STRING_LENGTH);
    }

    /**
     * get a timestamp string
     * @return string
     */
    public static String timestamp(){
        //system time
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * get the UUID String which specified length.
     * Random String are generated by UUID Class.
     * the default initial length is 32,
     * if specified length less than it:
     *   interception from the initial string;
     * if specified length greater than it:
     *   generate a 32-initial-length string which same as the first time.
     *   and stitching with the previous string.
     *   until the length of the string is greater than the specified length.
     * @param length the specified length
     * @return random string
     */
    private static String UUIDString(int length){
        StringBuilder builder = new StringBuilder();
        builder.append(UUIDString());
        int len = RANDOM_STRING_LENGTH;
        while (len < length){
            builder.append(UUIDString());
            len += RANDOM_STRING_LENGTH;
        }
        if (len > length)
            return builder.substring(0,length);
        return builder.toString();
    }

    /**
     * get the UUID String which removed "-".
     * @return random string
     */
    private static String UUIDString(){
        // UUID
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 判断字符串是否为空
     * 仿写commons-lang3
     * @param str str
     * @return true/false
     */
    public static boolean isEmpty(String str){
        // " " is false
        return null == str || str.length() == 0;
    }

    /**
     * 判断字符串是否不为空
     * 仿写commons-lang3
     * @param str str
     * @return true/false
     */
    public static boolean isNotEmpty(String str){
        // " " is true
        return null != str && str.length() > 0;
    }

    /**
     * 将一个字符串进行反转
     * @param str string
     * @throws NullPointerException if it is empty
     * @return string
     */
    public static String reverse(String str){
        if (isEmpty(str) || str.length() == 1)
            return str;
        int len = str.length();
        char[] reverse = new char[len];
        for (int i = len - 1 , j = 0 ; i >= 0 ; i -- , j ++)
            reverse[j] = str.charAt(i);
        return new String(reverse);
    }

    /**
     * 判断字符串是否是对称的
     * 使用反转方法
     * @param str str
     * @throws NullPointerException if it is empty
     * @return string
     */
    public static boolean isSymmetric(String str){
        // method reverse decide empty exception
        return reverse(str).equals(str);
    }

    /**
     * splicing an object array into a string
     * use the char-type separator as a chain
     * @param array a object-type array
     * @param separator a char
     * @return string
     */
    public static String join(Object[] array, char separator){
        int len;
        if (array == null || (len = array.length) < 1)
            return null;
        StringBuilder buf = new StringBuilder(len * 16);
        for (int i = 0 ; i < len ; i ++){
            if (array[i] != null && !EMPTY.equals(array[i])){
                buf.append(array[i]);
                if (i != len - 1){
                    buf.append(separator);
                }
            }
        }
        return buf.toString();
    }

    /**
     * 判断字符串是否全是数字
     * @param str string
     * @return true/false
     */
    public static boolean isNumber(String str){
        if (isEmpty(str))
            return false;
        for (int i = 0 , len = str.length() ; i < len ; i ++)
            if (!Character.isDigit(str.charAt(i)))
                return false;
        return true;
    }

    /**
     * 中国 身份证识别
     * 可识别15位及18位身份证
     * @param idcard IdCard
     * @return true/false
     */
    public static boolean isIdCard(String idcard){
        if (isEmpty(idcard))
            return false;
        if (idcard.length() == 15){
            if (!isNumber(idcard))
                return false;
            idcard = translate15IdcardTo18Idcard(idcard);
            if (null == idcard)
                return false;
        }
        return isNumber(idcard.substring(0,idcard.length() - 1)) && validate18IdCard(idcard);
    }

    /**
     * translate IDCard-15 into IDCard-18
     * @param idcard IDCard-15
     * @return 18-IDCard or null
     */
    private static String translate15IdcardTo18Idcard(String idcard){
        //get birth
        String birth = idcard.substring(6,12);
        Date birthDate = null;
        try{
            birthDate = new SimpleDateFormat("yyMMdd").parse(birth);
        }catch (ParseException e){
            e.printStackTrace();
        }
        if (null == birthDate)
            return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthDate);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String idcard17 = idcard.substring(0,6) + year + idcard.substring(8);
        int[] ints =convertCharToInt(idcard17.toCharArray());
        int sum17 = getPowerSum(ints);
        String checkCode = getCheckCodeBySum(sum17);
        if (null == checkCode)
            return null;
        return idcard17 + checkCode;
    }

    /**
     * Judging the Legitimacy of IDCard-18
     * According to the stipulation of citizenship number in the national
     * standard GB11643-1999 of the People's Republic of China，
     * Citizenship number is a feature combination code,
     * which consists of 17-digit digital ontology code and one-digit digital check code.
     * The order from left to right is: six digit address code,
     * eight digit date of birth code, three digit sequence code and one digit check code.
     * sequence code:
     * Indicates the person born in the same year, month and day within the
     * area marked by the same address code.
     * In sequence code, odd numbers for men and even numbers for women。
     *  1.前1、2位数字表示：所在省份的代码；
     *  1. The first 1 and 2 digits represent the code of the province in which it is located.
     *  2.第3、4位数字表示：所在城市的代码；
     *  2. The 3rd and 4th digits indicate: the code of the city in which it is located;
     *  3.第5、6位数字表示：所在区县的代码；
     *  3. Numbers 5 and 6 denote the code of the district or county in which it is located.
     *  4.第7~14位数字表示：出生年、月、日；
     *  4. Numbers 7-14 denote the year, month and day of birth;
     *  5.第15、16位数字表示：所在地的派出所的代码；
     *  The fifteenth and sixteenth digits indicate the code of the police station
     *  where it is located.
     *  6.第17位数字表示性别：奇数表示男性，偶数表示女性；
     *  6. Seventeenth digit denotes gender: odd number denotes male, even number denotes female;
     *  7.第18位数字是校检码：也有的说是个人信息码，
     *  7. The eighteenth digit is the proofreading code.
     *  Others say it is the personal information code.
     *    一般是随计算机的随机产生，用来检验身份证的正确性。
     *    Generally, it is generated randomly with the computer to
     *    verify the correctness of the ID card.
     *    校检码可以是0~9的数字，和罗马数字x表示。
     *    The check code can be a number of 0-9 and a Roman number X.
     * 第十八位数字(校验码)的计算方法为：
     * 1.将前面的身份证号码17位数分别乘以不同的系数。
     *   从第一位到第十七位的系数分别为：
     *    7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * 2.将这17位数字和系数相乘的结果相加。
     * 3.用加出来和除以11，看余数是多少？
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。
     *   其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3 2。
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。
     *   如果余数是10，身份证的最后一位号码就是2。
     * @param idcard ID Card
     * @return true/false
     */
    private static boolean validate18IdCard(String idcard){
        if (idcard.length() == 18){
            String checkCode =idcard.substring(17,18);
            char[] chars = idcard.substring(0,17).toCharArray();
            int sum17 =getPowerSum(convertCharToInt(chars));
            String _checkCode =getCheckCodeBySum(sum17);
            return null != _checkCode && _checkCode.equals(checkCode);
        }
        return false;
    }

    /**
     * converting char arrays to int arrays
     * Used for authentication of identity cards
     * @param chars char arrays
     * @return int arrays
     * @throws NumberFormatException not a number
     */
    private static int[] convertCharToInt(char[] chars) throws NumberFormatException{
        int len = chars.length;
        int[] ints = new int[len];
        for (int i = 0 ; i < len ; i ++){
            ints[i] = Integer.parseInt(String.valueOf(chars[i]));
        }
        return ints;
    }

    /**
     * get the 18th Bit Check Code in the Id Card Number
     * Used for authentication of identity cards
     * @param sum the sum of Weighting factors
     * @return Bit Check Code
     */
    private static String getCheckCodeBySum(int sum){
        switch (sum%11){
            case 10: return "2";
            case 9: return "3";
            case 8: return "4";
            case 7: return "5";
            case 6: return "6";
            case 5: return "7";
            case 4: return "8";
            case 3: return "9";
            case 2: return "x";
            case 1: return "0";
            case 0: return "1";
            default: return null;
        }
    }

    /**
     * the sum of Weighting factors
     * Used for authentication of identity cards
     * @param ints Weighting factors
     * @return int
     */
    private static int getPowerSum(int[] ints){
        int sum = 0;
        int len = ints.length;
        if (POWER.length != len)
            return sum;
        for (int i = 0 ; i < len ; i ++ )
            sum += ints[i] * POWER[i];
        return sum;
    }

    /**
     * Determine whether a string is a cell phone number
     * China Telecom:133,149,153,173,177,180,181,189,199
     * China Unicom:130,131,132,145,155,156,166,175,176,185,186
     * China Mobile:134(0-8),13(5-9),147,15(0-2),15(7-9),178,18(2-4),187,188,198
     * @param phone phone number
     * @return true/false
     * @since 2019/03
     */
    public static boolean isPhone(String phone){
        if (phone.length() == 11){
            Pattern pattern = Pattern.compile(PHONE_REGEX);
            Matcher matcher = pattern.matcher(phone);
            return matcher.matches();
        }
        return false;
    }

    /**
     * Verify the correctness of Email
     * validation rule:RFC 5322
     * user name
     * 1.ALLOW:A-Z,a-z,0-9,.-_@!#$%&'*+/=?^{}|~
     * 2.ALLOW:ASCII char(including Control-char).
     * 3.[.] can not appear at the beginning or end,Can't be next to each other more than two.
     * domain name
     * 1. ONLY ALLOW:A-Z,a-z,0-9,-(this one can not appear at the beginning or end).
     * 2. Top-level domain name cannot be all digital.
     * 3. at least have a secondary domain name.
     * @param email email
     * @return true/false
     */
    public static boolean isEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * get message from idcard
     * 0-5 : home address
     * 6-13 : birth
     * 16-17: sex
     * attention: home_address need search from MySQL
     *   only return address_code to User's home_address field
     * @param idcard Identity card
     * @return User
     */
    /*public static User getMsgFromIdcard(String idcard){
        if (isIdCard(idcard)){
            try{
                String address_code = idcard.substring(0,6);
                User user = new User();
                user.setHome_address(address_code);
                Date birth = stringToDate(idcard.substring(6,14));
                user.setBirth(birth);
                user.setAge(getAgeFromBirth(birth));
                user.setSex(parserSex(idcard.substring(16,17)));
                return user;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }*/

    /**
     * String to Date
     * 错误解析：起初使用
     * LocalDateTime time = LocalDateTime.parse(reg,df);
     * 报错
     * @param reg String
     * @return Date
     */
    private static Date stringToDate(String reg){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate time = LocalDate.parse(reg,df);
        return localDateTimeToDate(time);
    }

    /**
     * Date 转 String
     * @param date Date
     * @return String
     */
    public static String dateToString(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant,zoneId);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return df.format(localDateTime);
    }

    /**
     * LocalDateTime to Date
     * @param time LocalDateTime time
     * @see LocalDateTime
     * @return Date time
     */
    private static Date localDateTimeToDate(LocalDate time){
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = time.atStartOfDay().atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    /**
     * parser sex from the 17th number of Identity card
     * @param reg the 17th number
     * @return sex
     */
    private static String parserSex(String reg){
        return ((Integer.parseInt(reg)) % 2 != 0) ? "男" : "女";
    }

    /**
     * get age from birth
     * @param birth birth Date
     * @return age
     */
    private static int getAgeFromBirth(Date birth){
        Calendar cal = Calendar.getInstance();
        if (cal.before(birth)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birth);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }

}