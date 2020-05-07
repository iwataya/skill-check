package q005;

import java.util.HashMap;

/**
 * 作業時間管理クラス
 * 自由に修正してかまいません
 */
public class WorkData {
    /** 社員番号 */
    private String number;

    /** 部署 */
    private String department;

    /** 役職 */
    private String position;

    /** Pコードおよび作業時間(分) */
    private HashMap<String, String> workTime;

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
    }
    
	public HashMap<String, String> getWorkTime() {
		return this.workTime;
	}

	public void setWorkTime(HashMap<String, String> workTime) {
		this.workTime = workTime;
	}

}
