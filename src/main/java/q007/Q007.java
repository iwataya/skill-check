package q007;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import q007.MazeMap.PointType;

/**
 * q007 最短経路探索
 *
 * 壁を 'X' 通路を ' ' 開始を 'S' ゴールを 'E' で表現された迷路で、最短経路を通った場合に
 * 何歩でゴールまでたどり着くかを出力するプログラムを実装してください。
 * もし、ゴールまで辿り着くルートが無かった場合は -1 を出力してください。
 * なお、1歩は上下左右のいずれかにしか動くことはできません（斜めはNG）。
 *
 * 迷路データは MazeInputStream から取得してください。
 * 迷路の横幅と高さは毎回異なりますが、必ず長方形（あるいは正方形）となっており、外壁は全て'X'で埋まっています。
 * 空行が迷路データの終了です。
 *

[迷路の例]
XXXXXXXXX
XSX    EX
X XXX X X
X   X X X
X X XXX X
X X     X
XXXXXXXXX

[答え]
14
 */
public class Q007 {
    static final String KEY_ROW = "row";
    static final String KEY_COLUMN = "column";
    private static ArrayDeque<HashMap<String, Integer>> nextPoints = new ArrayDeque<HashMap<String, Integer>>();
    private static ArrayDeque<HashMap<String, Integer>> histories = new ArrayDeque<HashMap<String, Integer>>();

    /**
     * nextPointへ座標情報を1つ積む
     * @param point
     */
    private static void enqueueNextPoint(HashMap<String, Integer> point){
        nextPoints.addLast(point);
    }

    /**
     * nextPointから座標情報を1つ取り出す
     * @param point
     */
    private static HashMap<String, Integer> dequeueNextPoint(){
        return nextPoints.removeFirst();
    }

    /**
     * historyへ座標情報を1つ積む
     * @param point
     */
    private static void pushHistory(HashMap<String, Integer> point){
        histories.addLast(point);
    }

    /**
     * historyから座標情報を1つ取り出す
     * @param point
     */
    private static HashMap<String, Integer> popHistory(){
        return histories.removeLast();
    }

    /**
     * 指定された座標が探索済みかチェックする(historyに積まれているか？で判断)
     * @param row
     * @param column
     * @return true:探索済み/false:未探索
     */
    private static boolean isCheckedPoint(Integer row, Integer column){
        for(Iterator<HashMap<String, Integer>> iterator = histories.iterator(); iterator.hasNext();){
            HashMap<String, Integer> p = iterator.next();
            if(p.get(KEY_ROW).equals(row) && p.get(KEY_COLUMN).equals(column)){
                return true;
            }
        }

        return false;
    }

    /**
     * 指定座標の上下左右を確認し、次の探索位置をnextPointに積む
     * @param map
     * @param point
     */
    private static void findNextPoint(MazeMap map, HashMap<String, Integer> point){
        Integer baseRow = point.get(KEY_ROW);
        Integer baseColumn = point.get(KEY_COLUMN);
        HashMap<String, Integer> mapSize = map.getMazeSize();
        ArrayList<HashMap<String, Integer>> addList = new ArrayList<HashMap<String, Integer>>();

        // 指定座標の上を確認
        if(baseRow > 0 && !isCheckedPoint(baseRow-1, baseColumn)){
            if(!map.getPointType(baseRow-1, baseColumn).equals(PointType.Wall)){
                HashMap<String, Integer> nextPoint = new HashMap<String, Integer>();
                nextPoint.put(KEY_ROW, baseRow-1);
                nextPoint.put(KEY_COLUMN, baseColumn);
                addList.add(nextPoint);
            }
        }

        // 指定座標の下を確認
        if(baseRow < mapSize.get(KEY_ROW) - 1 && !isCheckedPoint(baseRow+1, baseColumn)){
            if(!map.getPointType(baseRow+1, baseColumn).equals(PointType.Wall)){
                HashMap<String, Integer> nextPoint = new HashMap<String, Integer>();
                nextPoint.put(KEY_ROW, baseRow+1);
                nextPoint.put(KEY_COLUMN, baseColumn);
                addList.add(nextPoint);
            }
        }

        // 指定座標の右を確認        
        if(baseRow > 0 && !isCheckedPoint(baseRow, baseColumn+1)){
            if(!map.getPointType(baseRow, baseColumn+1).equals(PointType.Wall)){
                HashMap<String, Integer> nextPoint = new HashMap<String, Integer>();
                nextPoint.put(KEY_ROW, baseRow);
                nextPoint.put(KEY_COLUMN, baseColumn+1);
                addList.add(nextPoint);
            }
        }

        // 指定座標の左を確認        
        if(baseRow < mapSize.get(KEY_COLUMN) - 1 && !isCheckedPoint(baseRow, baseColumn-1)){
            if(!map.getPointType(baseRow, baseColumn-1).equals(PointType.Wall)){
                HashMap<String, Integer> nextPoint = new HashMap<String, Integer>();
                nextPoint.put(KEY_ROW, baseRow);
                nextPoint.put(KEY_COLUMN, baseColumn-1);
                addList.add(nextPoint);
            }
        }

        // addListに積んだHashMapをnextPointsに積む
        for(Iterator<HashMap<String, Integer>> iterator = addList.iterator(); iterator.hasNext();){
            HashMap<String, Integer> p = iterator.next();
            enqueueNextPoint(p);
        }

    }

    /**
     * 指定された2座標が縦横どちらかに隣接しているかチェック
     */
    private static boolean isNextPosition(int currentRow, int currentColumn, int prevRow, int prevColumn){
        int rowDiff = Math.abs(prevRow - currentRow);
        int columnDiff = Math.abs(prevColumn - currentColumn);

        if((rowDiff == 1 && columnDiff == 0) || (rowDiff == 0 && columnDiff == 1)){
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        // 迷路を作成し、スタート地点を取得
        MazeMap map = new MazeMap();
        HashMap<String, Integer> startPoint = map.getStartPoint();
        map.printMazeMap();

        // 現在位置をスタート地点で更新し、探索履歴スタックにadd
        HashMap<String, Integer> currentPoint = startPoint;
        Integer currentRow = currentPoint.get(KEY_ROW);
        Integer currentColumn = currentPoint.get(KEY_COLUMN);
        pushHistory(currentPoint);
    
        // 現在位置の前後左右の内、未探索かつ壁以外の座標を次位置キューへadd
        findNextPoint(map, currentPoint);
    
        // ゴール地点に到着するまで、迷路を探索する
        while(!map.getPointType(currentRow, currentColumn).equals(PointType.Goal)){
            // 1.探索履歴スタックにadd
            // 2.現在位置の上下左右の内、未探索かつ通路の座標を次位置キューへadd
            try {
                currentPoint = dequeueNextPoint();
                currentRow = currentPoint.get(KEY_ROW);
                currentColumn = currentPoint.get(KEY_COLUMN);
                pushHistory(currentPoint);
                findNextPoint(map, currentPoint);
            } catch (NoSuchElementException e) {
                //ゴール未着でnextPointsが空になったら探索ループを抜ける
                break;
            }
        }
    
        // ゴール地点にたどり着いたかチェック
        int count = 0;
        if(map.getPointType(currentRow, currentColumn).equals(PointType.Goal)){
            // 1.探索履歴スタックから1つ取り出す
            // 2.前回位置の座標と縦横どちらかに隣接している座標の場合、カウントアップ
            // 3.2.,3.をスタート地点に到達するまで(スタックが空になるまで)繰り返す
            int prevRow = currentRow.intValue();
            int prevColumn = currentColumn.intValue();
            while(!map.getPointType(currentRow, currentColumn).equals(PointType.Start)){
                currentPoint = popHistory();
                currentRow = currentPoint.get(KEY_ROW);
                currentColumn = currentPoint.get(KEY_COLUMN);

                if(isNextPosition(currentRow.intValue(), currentColumn.intValue(), prevRow, prevColumn)){
                    count++;
                    prevRow = currentRow.intValue();
                    prevColumn = currentColumn.intValue();
                }
            }
        } else {
            count = -1;
        }

        // カウンタの値を出力して終了
        System.out.println("答え： " + count);
    }

}
// 完成までの時間: 6時間 28分