package q009;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PrimeFactorizationRunner implements Runnable{
    private static final BigInteger ZERO = new BigInteger("0");
    private static final BigInteger ONE = new BigInteger("1");
    private static final BigInteger TWO = new BigInteger("2");

    private BigInteger num;          // 素因数分解を行う数
    private List<BigInteger> result; // 素因数分解結果を格納するリスト
    private boolean finished;        // 計算が終了したかを表すフラグ


    public PrimeFactorizationRunner(BigInteger num) {
        this.num = num;
        this.result = new ArrayList<BigInteger>();
        this.finished = false;
    }

    @Override
    public void run() {
        BigInteger val = new BigInteger("2");
        BigInteger[] r = num.divideAndRemainder(val);
        while(r[0].compareTo(ONE) >= 0){
            // r[0]が1以上の間繰り返し
            // r[1]が0なら、r[0]でnumを更新＆valをresultにadd
            // r[1]が1なら、valの値を増やす
            if(r[1].compareTo(ZERO) == 0){
                result.add(val);
                num = new BigInteger(r[0].toString());
            } else {
                if(val.compareTo(TWO) == 0){
                    val = new BigInteger("3");
                } else {
                    val = val.add(TWO);
                }
            }
            r = num.divideAndRemainder(val);
        }

        // 分解し終えたら、resultから重複を取り除きfinishedをtrueにする
        result = new ArrayList<BigInteger>(new HashSet<BigInteger>(result));
        finished = true;
    }

    public List<BigInteger> getResult() {
        return result;
    }

    public boolean isFinished() {
        return finished;
    }

}