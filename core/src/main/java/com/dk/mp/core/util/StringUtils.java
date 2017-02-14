package com.dk.mp.core.util;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;


/**
 * 常用字符串工�? 
 * @version 2012-9-13
 * @author wangw
 */
public class StringUtils {
    private final static Pattern URL = Pattern
            .compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");
	
	/**
	 * 除法。
	 * @param a 除数
	 * @param b 被除数
	 * @return String
	 */
	public static String getDouble(int a, int b,int num) {
		if(a==0){
			return "0";
		}
		double c = (((float) a / (float) b) * 10000) / 100;
		String format="0";
		if(num>0){
			format+=".";
			for(int i=0;i<num;i++){
				format+="0";
			}
		}
		 DecimalFormat   df1   =   new   DecimalFormat(format);
         return df1.format(c); 
	}
	

    /**
     * 
     * 判断strings中有string.
     * @param string
     * @param strings String[]
     * @return boolean
     */
    public static boolean contain(String string, String[] strings) {
        if (strings == null)
            return false;
        for (int i = 0; i < strings.length; i++) {
            String s = strings[i];
            if (string.equals(s))
                return true;
        }
        return false;
    }

    /**
     * 判断strings包含string（忽略空格和大小写）.
     * @param string  String
     * @param strings    String[]
     * @return boolean
     */
    public static boolean containTrimAndIgnoreCase(String string, String[] strings) {
        if (strings == null)
            return false;
        for (int i = 0; i < strings.length; i++) {
            String s = strings[i];
            if (string.trim().equalsIgnoreCase(s.trim()))
                return true;
        }
        return false;
    }

    /**
     * 
     * 将Object[]转换为String[].
     * @param objects   Object[]
     * @return String[]
     */
    public static String[] toStringArray(Object[] objects) {
        int length = objects.length;
        String[] result = new String[length];
        for (int i = 0; i < length; i++) {
            result[i] = objects[i].toString();
        }
        return result;
    }

    /**
     * 提取html标签.
     * 
     * @param str
     *            htmlString
     * @return String
     */
    public static String extractHTML(String str) {
        if (str == null)
            return "";
        StringBuffer ret = new StringBuffer(str.length());
        int start = 0;
        int beginTag = str.indexOf("<");
        int endTag = 0;
        if (beginTag == -1)
            return str;

        while (beginTag >= start) {
            endTag = str.indexOf(">", beginTag);

            // if endTag found, keep tag
            if (endTag > -1) {
                ret.append(str.substring(beginTag, endTag + 1));

                // move start forward and find another tag
                start = endTag + 1;
                beginTag = str.indexOf("<", start);
            }
            // if no endTag found, break
            else {
                break;
            }
        }
        return ret.toString();
    }

    /**
     * 
     * 翻转数组顺序.
     * 
     * @param arr
     *            String[] {“a�?“b”}
     * @return String[] {“b”，“a”}
     */
    public static String[] reverse(String[] arr) {
        String[] arrr = new String[arr.length];
        for (int i = arr.length - 1; i >= 0; i--) {
            arrr[arr.length - 1 - i] = arr[i];
        }
        return arrr;
    }

    /**
     * @return uuid
     */
    public static String getUUID() {
        UUID id = UUID.randomUUID();
        String tempID = id.toString();
        return tempID;
    }

    /**
     * 判断�?null,"","null" 均视为空.
     * 
     * @param str
     *            字符�?
     * @return 结果 boolean
     */
    public static boolean isNotEmpty(String str) {
        boolean bool = true;
        if (str == null || "null".equals(str) || "".equals(str)) {
            bool = false;
        } else {
            bool = true;
        }
        return bool;
    }

    /**
     * 判断�?null,"","null" 全转换为"".
     * 
     * @param str
     *            字符�?
     * @return 结果 String
     */
    public static String checkEmpty(String str) {
        String result = str;
        if (str == null || "null".equals(str) || "".equals(str)) {
            result = "";
        }
        return result;
    }

    /**
     * 截取中文字符�?
     * 
     * @param sourceString 字符
     * @param maxLength 长度
     * @return 截取后的字符
     */
    public static String subString(String sourceString, int maxLength) {
        String string = sourceString;
        String resultString = "";
        if (string == null || "".equals(string) || maxLength < 1) {
            return resultString;
        } else if (string.length() <= maxLength) {
            return string;
        } else if (string.length() > 2 * maxLength) {
            string = string.substring(0, 2 * maxLength);
        }
        if (string.length() > maxLength) {
            char[] chr = string.toCharArray();
            int strNum = 0;
            int strGBKNum = 0;
            boolean isHaveDot = false;

            for (int i = 0; i < string.length(); i++) {
                if (chr[i] >= 0xa1) // 0xa1汉字�?��位开�?
                {
                    strNum = strNum + 2;
                    strGBKNum++;
                } else {
                    strNum++;
                }
                if (strNum == 2 * maxLength || strNum == 2 * maxLength + 1) {
                    if (i + 1 < string.length()) {
                        isHaveDot = true;
                    }
                    break;
                }
            }
            resultString = string.substring(0, strNum - strGBKNum);
            if (!isHaveDot) {
                resultString = resultString + "...";
            }
        }
        return resultString;
    }

    /**
     * 清除html标签.
     * @param oldHtmlStr html付文�?
     * @return 纯文�?
     */
    public static String operateHtmlStr(String oldHtmlStr) {
        String rexStr = "<[^>]*>|&nbsp;";
        String newHtmlStr = "";
        newHtmlStr = oldHtmlStr.replaceAll(rexStr, "");
        return newHtmlStr;
    }

    /**
     * 根据key获取map中的value，如果value为null则返回空字符.
     * @param map contentArrayThree
     * @param key key
     * @return value
     */
    public static String getValueByKey(Map<String, Object> map, String key) {
        if (map != null) {
            return map.get(key) != null ? map.get(key).toString() : "";
        } else {
            return "";
        }
    }
	public static String removeStyle(String oldHtmlStr) {
		return oldHtmlStr.replaceAll("(\\<\\w+\\s*)[^\\>]*", "$1");
	}
	
	
	/**
	 * 过滤网络连接的特殊字符.
	 * @param x  网络链接
	 * @return 过滤后的连接
	 */
	public static String filterPath(String x) {
		return x.replace("\\", "/").replace("?", "").replace("/", "").replace(":", "");
	}
	
	
	
	/**
	 * 判断字符串编码格式.
	 * @param str 字符串
	 * @return 编码
	 */
	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				return encode;
			}
			encode = "ISO-8859-1";
			if (str.equals(new String(str.getBytes(encode), encode))) {
				return encode;
			}
			encode = "UTF-8";
			if (str.equals(new String(str.getBytes(encode), encode))) {
				return encode;
			}
			encode = "GBK";
			if (str.equals(new String(str.getBytes(encode), encode))) {
				return encode;
			}
		} catch (Exception e) {
			return "";
		}
		return "";
	}

	/**
	 * 字符串转码.
	 * @param string 字符串
	 * @param codeing 需要的编码格式
	 * @return 转码后的字符串
	 */
	public static String getString(String string, String codeing) {
		String str = getEncoding(string);
		try {
			return new String(string.getBytes(codeing), str);
		} catch (UnsupportedEncodingException e) {
			return string;
		}
	}
	
	
	/**
	 * 将dip转换为px
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px转换为dip
	 * @param context
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

    /**
     * 判断是否为一个合法的url地址
     *
     * @param str
     * @return
     */
    public static boolean isUrl(String str) {
        if (str == null || str.trim().length() == 0)
            return false;
        return URL.matcher(str).matches();
    }
}
