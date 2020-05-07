package q005;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Q005 データクラスと様々な集計
 *
 * 以下のファイルを読み込んで、WorkDataクラスのインスタンスを作成してください。 resources/q005/data.txt
 * (先頭行はタイトルなので読み取りをスキップする)
 *
 * 読み込んだデータを以下で集計して出力してください。 (1) 役職別の合計作業時間 (2) Pコード別の合計作業時間 (3) 社員番号別の合計作業時間
 * 上記項目内での出力順は問いません。
 *
 * 作業時間は "xx時間xx分" の形式にしてください。 また、WorkDataクラスは自由に修正してください。
 *
 * [出力イメージ]
 * 部長: xx時間xx分
 * 課長: xx時間xx分
 * 一般: xx時間xx分
 * Z-7-31100: xx時間xx分
 * I-7-31100: xx時間xx分
 * T-7-30002: xx時間xx分
 * （省略）
 * 194033: xx時間xx分
 * 195052: xx時間xx分
 * 195066: xx時間xx分
 * （省略）
 */
public class Q005 {
    /**
     * データファイルを開く resources/q005/data.txt
     */
    private static InputStream openDataFile() {
        return Q005.class.getResourceAsStream("data.txt");
    }
    
    private static WorkData createNewWorkData(String line){
        String[] d = line.split(",", 0);
        WorkData data = new WorkData();
        data.setNumber(d[0]);
        data.setDepartment(d[1]);
        data.setPosition(d[2]);
        HashMap<String, String> t = new HashMap<String, String>();
        t.put(d[3], d[4]);
        data.setWorkTime(t);

        return data;
    }

    private static int summerizeWorkTimeByPosition(String position, List<WorkData> workDatas){
        return workDatas.stream()
                        .filter(value -> value.getPosition().equals(position))
                        .mapToInt(mapper -> mapper.getWorkTime().values().stream().mapToInt(Integer::parseInt).sum())
                        .sum();
    }

    private static int summerizeWorkTimeByPCode(String code, List<WorkData> workDatas){
        return workDatas.stream()
                        .filter(value -> value.getWorkTime().keySet().contains(code))
                        .mapToInt(mapper -> Integer.parseInt(mapper.getWorkTime().get(code)))
                        .sum();
    }

    private static int summerizeWorkTimeByNumer(String number, List<WorkData> workDatas){
        return workDatas.stream()
                        .filter(value -> value.getNumber().equals(number))
                        .mapToInt(mapper -> mapper.getWorkTime().values().stream().mapToInt(Integer::parseInt).sum())
                        .sum();
    }

    private static String convertTime(int time){
        int hour = time / 60;
        int minute = time - 60 * hour;

        StringBuilder builder = new StringBuilder();
        if(hour > 0){
            builder.append(hour);
            builder.append("時間");
        }
        builder.append(minute);
        builder.append("分");

        return builder.toString();
    }

    public static void main(final String[] args) {
        // データファイルを読み込み、WorkDataクラスに変換する
        Reader r = new InputStreamReader(openDataFile());
        BufferedReader br = new BufferedReader(r);

        List<WorkData> workDatas = new ArrayList<WorkData>();
        try {
            // 先頭行はヘッダなので、2回読み込む
            String line = br.readLine();
            line = br.readLine();
            while(line != null){
                 workDatas.add(createNewWorkData(line));
                 line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // 役職別の合計作業時間を集計して出力
        List<String> positionList = new ArrayList<String>();
        workDatas.forEach(s -> {
            positionList.add(s.getPosition());
        });
        List<String> uPositionList = new ArrayList<String>(new HashSet<>(positionList));
        uPositionList.stream().sorted(Comparator.reverseOrder()).forEach(e -> {
            System.out.println(e + " : " + convertTime(summerizeWorkTimeByPosition(e, workDatas)));
        });

        // Pコード別の合計作業時間を集計して出力
        List<String> codeList = new ArrayList<String>();
        workDatas.forEach(s ->{
            codeList.addAll(new ArrayList<String>(s.getWorkTime().keySet()));
        });
        List<String> uCodeList = new ArrayList<String>(new HashSet<>(codeList));
        uCodeList.stream().sorted(Comparator.reverseOrder()).forEach(e -> {
            System.out.println(e + " : " + convertTime(summerizeWorkTimeByPCode(e, workDatas)));
        });

        // 社員番号別の合計作業時間を集計して出力
        List<String> numberList = new ArrayList<String>();
        workDatas.forEach(s ->{
            numberList.add(s.getNumber());
        });
        List<String> uNumberList = new ArrayList<String>(new HashSet<>(numberList));
        uNumberList.stream().sorted().forEach(e -> {
            System.out.println(e + " : " + convertTime(summerizeWorkTimeByNumer(e, workDatas)));
        });

    }

}
// 完成までの時間: 1時間 50分