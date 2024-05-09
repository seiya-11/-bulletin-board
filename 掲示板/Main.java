/**
 * メインクラスは、プログラムのエントリーポイントとなります。
 * LoginGUIクラスのインスタンスを作成し、ログイン画面を表示します。
 * 
 * 
 */
public class Main {
    /**
     * プログラムのエントリーポイントです。
     * LoginGUIクラスのインスタンスを作成し、ログイン画面を表示します。
     * @param args コマンドライン引数（未使用）
     */
    
    public static void main(String[] args) {
        
        LoginGUI loginGUI = new LoginGUI();
        loginGUI.login();
    }
}
