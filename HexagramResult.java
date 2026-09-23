package com.example.divination;

import java.util.ArrayList;
import java.util.List;

/** 一次起卦的结果。lines 为 6 爻值：6=老阴(动)、7=少阳、8=少阴、9=老阳(动)。 */
public final class HexagramResult {

    public final int[] lines;          // 自下而上
    public final List<Integer> changing; // 动爻下标（0=初爻，自下而上）
    public final Hexagram ben;         // 本卦
    public final Hexagram bian;        // 变卦（无动爻时为 null）
    public final Hexagram hu;          // 互卦（中间四爻所成，无动爻时为 null）
    public final String method;        // 起卦方式名称
    public final String question;      // 所问之事
    public final String timeText;      // 起卦时间
    public final boolean coinMethod;

    public HexagramResult(int[] lines, List<Integer> changing, Hexagram ben, Hexagram bian,
                          String method, String question, String timeText, boolean coinMethod) {
        this(lines, changing, ben, bian, null, method, question, timeText, coinMethod);
    }

    public HexagramResult(int[] lines, List<Integer> changing, Hexagram ben, Hexagram bian,
                          Hexagram hu, String method, String question, String timeText, boolean coinMethod) {
        this.lines = lines;
        this.changing = changing;
        this.ben = ben;
        this.bian = bian;
        this.hu = hu;
        this.method = method;
        this.question = question;
        this.timeText = timeText;
        this.coinMethod = coinMethod;
    }

    public List<String> changingLineTexts() {
        List<String> out = new ArrayList<>();
        if (ben != null && ben.lt != null) {
            for (int i : changing) {
                if (i >= 0 && i < ben.lt.size()) {
                    out.add(ben.lt.get(i));
                }
            }
        }
        return out;
    }

    /** 爻位名，如 初九 / 六三 / 上六 */
    public static String lineName(int index, int value) {
        String[] pos = {"初", "二", "三", "四", "五", "上"};
        String y = (value == 7 || value == 9) ? "九" : "六";
        String p = pos[index];
        if (index == 0) p = "初";
        if (index == 5) p = "上";
        return p + y;
    }

    /** 变卦爻值（6→9 或 9→6，7/8 不变），用于定位变卦 */
    public static int[] mutate(int[] lines) {
        int[] out = new int[lines.length];
        for (int i = 0; i < lines.length; i++) {
            int v = lines[i];
            if (v == 6) out[i] = 9;
            else if (v == 9) out[i] = 6;
            else out[i] = v;
        }
        return out;
    }

    /** 稳定爻线（动爻还原为普通阴阳），用于反查卦象 */
    public static int[] stableLines(int[] lines) {
        int[] out = new int[lines.length];
        for (int i = 0; i < lines.length; i++) {
            int v = lines[i];
            if (v == 6 || v == 8) out[i] = 8;
            else out[i] = 7;
        }
        return out;
    }

    /** 互卦爻线：由本卦中间四爻（二、三、四、五爻）组成，上卦为三、四、五爻，下卦为二、三、四爻 */
    public static int[] huLines(int[] lines) {
        int[] stable = stableLines(lines);
        return new int[]{stable[1], stable[2], stable[3], stable[2], stable[3], stable[4]};
    }
}
