package d1.project.questionnaire.util;

import java.util.HashMap;

/**
 * 数据字典
 *
 * @author baozh
 */
public class DataDictionary {
    public final static HashMap<String, String> GUA_NAME = new HashMap<String, String>() {
        {
            put("111111", "乾为天");
            put("000000", "坤为地");
            put("010001", "水雷屯");
            put("100010", "山水蒙");
            put("010111", "水天需");
            put("111010", "天水讼");
            put("000010", "地水师");
            put("010000", "水地比");
            put("110111", "风天小畜");
            put("111011", "天泽履");
            put("000111", "地天泰");
            put("111000", "天地否");
            put("111101", "天火同人");
            put("101111", "火天大有");
            put("000100", "地山谦");
            put("001000", "雷地豫");
            put("011001", "泽雷随");
            put("100110", "山风蛊");
            put("000011", "地泽临");
            put("110000", "风地观");
            put("101001", "火雷噬嗑");
            put("100101", "山火贲");
            put("100000", "山地剥");
            put("000001", "地雷复");
            put("111001", "天雷无妄");
            put("100111", "山天大畜");
            put("100001", "山雷颐");
            put("011110", "泽风大过");
            put("010010", "坎为水");
            put("101101", "离为火");
            put("011100", "泽山咸");
            put("001110", "雷风恒");
            put("111100", "天山遁");
            put("001111", "雷天大壮");
            put("101000", "火地晋");
            put("000101", "地火明夷");
            put("110101", "风火家人");
            put("101011", "火泽睽");
            put("010100", "水山蹇");
            put("001010", "雷水解");
            put("100011", "山泽损");
            put("110001", "风雷益");
            put("011111", "泽天夬");
            put("111110", "天风姤");
            put("011000", "泽地萃");
            put("000110", "地风升");
            put("011010", "泽水困");
            put("010110", "水风井");
            put("011101", "泽火革");
            put("101110", "火风鼎");
            put("001001", "震为雷");
            put("100100", "艮为山");
            put("110100", "风山渐");
            put("001011", "雷泽归妹");
            put("001101", "雷火丰");
            put("101100", "火山旅");
            put("110110", "巽为风");
            put("011011", "兑为泽");
            put("110010", "风水涣");
            put("010011", "水泽节");
            put("110011", "风泽中孚");
            put("001100", "雷山小过");
            put("010101", "水火既济");
            put("101010", "火水未济");
        }
    };

    public final static HashMap<Integer, String> PERSONAL_SIGNATURE = new HashMap<Integer, String>() {
        {
            put(1, "当你得到什么东西，你就失掉什么东西。");
            put(2, "一个人的信用是你自己坚持出来的。");
            put(3, "看一个孩子，是看他的品德，不是看他的才干。");
            put(4, "一个人，要替自己的所作所为担负全部的责任。");
            put(5, "全面无型的控制，把法律和良心合在一起。");
            put(6, "只要合理，怎样变动都可以。");
            put(7, "命运就是性格，性格决定命运。");
            put(8, "一个人信用是最重要的。");
            put(9, "只要有做好事的心愿，就会走出一条路来。");
            put(10, "在家，家和万事兴；出外，把每一个人都当成朋友。");
            put(11, "道理是直的，但路经常是弯的。");
            put(12, "做人应该只问耕耘不问收获，因为收获不是自己能掌控的。");
            put(13, "一切来自社会，最后是还给社会。");
            put(14, "人不要求全，应该知足，而只有守分，才会知足。");
            put(15, "不求人，格自高。");
            put(16, "抱持既不赞成也不反对的心态来包容一切。");
        }
    };

    public final static HashMap<Integer, String> USER_SEX = new HashMap<Integer, String>() {
        {
            put(0, "男");
            put(1, "女");
            put(-1, "未知");
        }
    };
}