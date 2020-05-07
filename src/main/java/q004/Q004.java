package q004;

/**
 * Q004 ソートアルゴリズム
 *
 * ListManagerクラスをnewして、小さい順に並び変えた上でcheckResult()を呼び出してください。
 *
 * 実装イメージ:
 * ListManager data = new ListManager();
 * // TODO 並び換え
 * data.checkResult();
 *
 * - ListManagerクラスを修正してはいけません
 * - ListManagerクラスの dataList を直接変更してはいけません
 * - ListManagerクラスの比較 compare と入れ替え exchange を使って実現してください
 */
public class Q004 {

    public static void main(String[] args) {
        ListManager data = new ListManager();

        for(int i=0;i<data.size();i++){
            for(int j=i+1;j<data.size();j++){
                if(data.compare(i, j) > 0){
                    data.exchange(i, j);
                }
            }
        }

        data.checkResult();
    }
}
// 完成までの時間: 0時間 7分