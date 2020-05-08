package q007;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 座標管理用Mapクラス
 */
public class MazeMap {
    public enum PointType {
        Wall, Start, Goal, Passway
    }

    private static final int CR = 13;
    private static final int LF = 10;
    private MazeInputStream mazeInputStream = null;
    private ArrayList<ArrayList<String>> mazeMap = new ArrayList<ArrayList<String>>();

    public MazeMap() {
        this.mazeInputStream = new MazeInputStream();
        createMazeMap(this.mazeInputStream);
    }

    /**
     * ArrayListを用いた二次元配列をもちいて、迷路データを作成する
     */
    private void createMazeMap(MazeInputStream stream){
        // 改行文字以外を読み込む→列を増やす
        // 改行文字を読み込む→行を増やす
        while(stream.available() != 0){
            ArrayList<String> row = new ArrayList<String>();
            int c = stream.read();
            while(c != CR && c != LF){
                byte[] asciiCodes = new byte[]{(byte)c};
                row.add(new String(asciiCodes, StandardCharsets.US_ASCII));
                c = stream.read();
            }

            if(row.size() > 0){
                mazeMap.add(row);
            }
        }
    }

    /**
     * 作成した迷路データを出力する
     */
    public void printMazeMap(){
        for(Iterator<ArrayList<String>> iterator1 = mazeMap.iterator(); iterator1.hasNext();){
            ArrayList<String> line = iterator1.next();
            for(Iterator<String> iterator2 = line.iterator(); iterator2.hasNext();){
                String s = iterator2.next();
                System.out.print(s);
            }
            System.out.println();
        }
    }

    /**
     * 指定された座標の種別を返す
     */
    public PointType getPointType(int row, int column){
        String s = mazeMap.get(row).get(column);
        if(s.equals("S")){
            return PointType.Start;
        } else if(s.equals("E")){
            return PointType.Goal;
        } else if(s.equals("X")){
            return PointType.Wall;
        } else {
            return PointType.Passway;
        }
    }

    /**
     * 作成した迷路のサイズを返す
     */
    public HashMap<String, Integer> getMazeSize(){
        int row = mazeMap.size();
        int column = mazeMap.get(0).size();
        HashMap<String, Integer> size = new HashMap<String, Integer>();
        size.put(Q007.KEY_ROW, new Integer(row));
        size.put(Q007.KEY_COLUMN, new Integer(column));

        return size;
    }

    /**
     * 迷路のスタート地点の座標を返す
     */
    public HashMap<String, Integer> getStartPoint(){
        HashMap<String, Integer> start = new HashMap<String, Integer>();

        for(int i=0;i<mazeMap.size();i++){
            for(int j=0;j<mazeMap.get(0).size();j++){
                if(getPointType(i, j).equals(PointType.Start)){
                    start.put(Q007.KEY_ROW, i);
                    start.put(Q007.KEY_COLUMN, j);
                    return start;
                }
            }
        }

        // もしスタート地点が見つからなかったらエラー
        throw new RuntimeException("Can't find start point");
    }
}
