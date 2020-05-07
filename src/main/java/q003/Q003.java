package q003;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * Q003 集計と並べ替え
 *
 * 以下のデータファイルを読み込んで、出現する単語ごとに数をカウントし、アルファベット辞書順に並び変えて出力してください。
 * resources/q003/data.txt 
 * 単語の条件は以下となります
 * - "I"以外は全て小文字で扱う（"My"と"my"は同じく"my"として扱う）
 * - 単数形と複数形のように少しでも文字列が異れば別単語として扱う（"dream"と"dreams"は別単語）
 * - アポストロフィーやハイフン付の単語は1単語として扱う（"isn't"や"dead-end"）
 *
 * 出力形式:単語=数
 *
 * [出力イメージ] （省略） highest=1 I=3 if=2 ignorance=1 （省略）
 * 
 * 参考 http://eikaiwa.dmm.com/blog/4690/
 */
public class Q003 {
    /**
     * データファイルを開く resources/q003/data.txt
     */
    private static InputStream openDataFile() {
        return Q003.class.getResourceAsStream("data.txt");
    }

    public static void main(String[] args) {
        // データファイルを1行の文字列として読み込む & 重複なし単語リストを作る
        Reader r = new InputStreamReader(openDataFile());
        BufferedReader br = new BufferedReader(r);

        List<String> wordList = new ArrayList<String>();
        StringBuilder lines = new StringBuilder();
        List<String> uWordList = new ArrayList<>();
        try {
            String line = br.readLine();
            while(line != null){
                lines.append(line.toLowerCase() + " ");
                String[] l = line.toLowerCase().split("[ ,.;]+", 0);
                wordList.addAll(Arrays.asList(l));
                line = br.readLine();
            }

            uWordList = new ArrayList<String>(new HashSet<>(wordList));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        // 重複なし単語リストの頭から、StringUtils.countMatchesで数えた個数と単語の組み合わせのHashMapを作成する
        HashMap<String, Integer> mMap = new HashMap<String, Integer>();
        uWordList.forEach(s -> {
            mMap.put(s, StringUtils.countMatches(lines, s));
        });

        // 作成したhashMapをアルファベット順に表示する
        // i のみ大文字にして表示する
        for (String word: new ArrayList<String>(mMap.keySet()).stream().sorted().collect(Collectors.toList())){
            if(word.equals("–")){
                continue;
            } else if(word.equals("i")){
                System.out.println(word.toUpperCase() + "=" + mMap.get(word));
            } else {
                System.out.println(word + "=" + mMap.get(word));
            }
        }
        
    }
}
// 完成までの時間: 1時間 7分