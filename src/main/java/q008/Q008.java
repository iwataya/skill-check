package q008;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Q008 埋め込み文字列の抽出
 *
 * 一般に、定数定義の場合を除いて、プログラム中に埋め込み文字列を記述するのは良くないとされています。
 * そのような埋め込み文字列を見つけるために、埋め込み文字列や埋め込み文字（"test"や'a'など）が
 * 記述された行を出力するプログラムを作成してください。
 *
 * - 実行ディレクトリ配下（再帰的にチェック）に存在するすべてのjavaファイルをチェックする
 * - ファイル名はディレクトリ付きでも無しでも良い
 * - 行の内容を出力する際は、先頭のインデントは削除しても残しても良い

[出力イメージ]
Q001.java(12): return "test";  // テストデータ
Q002.java(10): private static final DATA = "test"
Q002.java(11): + "aaa";
Q002.java(20): if (test == 'x') {
Q004.java(13): Pattern pattern = Pattern.compile("(\".*\")|(\'.*\')");

 */
public class Q008 {
    /**
     * JavaファイルのStreamを作成する
     *
     * @return
     * @throws IOException
     */
    private static Stream<File> listJavaFiles() throws IOException {
        return Files.walk(Paths.get(".")).map(Path::toFile).filter(f -> f.getName().endsWith(".java"));
    }

    public static void main(String[] args) {
        // Fileストリームを返す関数を呼んで、Fileリストを作る
        ArrayList<File> fileList = new ArrayList<File>();
        try {
            listJavaFiles().forEach(fileList::add);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // リストをforで回しながらチェック
        fileList.stream().forEach(f ->{
            Path path = f.toPath();
            String name = path.getFileName().toString();
            try {
                List<String> lines = Files.readAllLines(path);
                int index = 1;
                for(String line: lines){
                    if(line.matches(".+\".+\"$")){
                        StringBuilder builder = new StringBuilder();
                        builder.append(name);
                        builder.append("("+ index +"): ");
                        builder.append(line);
                        System.out.println(builder.toString());
                    } else if(line.matches("^.+[\"'].+[\"'].*[,;{]$")){
                        StringBuilder builder = new StringBuilder();
                        builder.append(name);
                        builder.append("("+ index +"): ");
                        builder.append(line);
                        System.out.println(builder.toString());
                    } else if(line.matches("^.+\".+\"[:;].+$")){
                        StringBuilder builder = new StringBuilder();
                        builder.append(name);
                        builder.append("("+ index +"): ");
                        builder.append(line);
                        System.out.println(builder.toString());
                    }
                    index++;
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        });

    }
}
// 完成までの時間: 1時間 19分
