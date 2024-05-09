import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * パスワード入力およびワンタイムパスワード生成のためのGUIを提供するクラスです。
 * 
 * 
 */
public class PasswordGUI extends JFrame implements ActionListener {
    
    /**
     * データベースのテーブル名
     */
    public String table_name = "user";

    /**
     * ユーザー名
     */
    public String userName;

    /**
     * ユーザーのワンタイムパスワード
     */
    public String userOnetimePas;

    /**
     * ワンタイムパスワードの値
     */
    public int    onetimePas;

    /**
     * ワンタイムパスワードの有効期限（秒）
     */
    public int    sec;

    /**
     * ワンタイムパスワードの生成結果
     */
    public int    resultpass = 0;

    /**
     * パスワードの入力値
     */
    public int    pass = -1;

    
    /**
     * ループ変数
     */
    public int    i=0;

    /**
     * ユーザー名が一致しているか
     */
    public boolean userFlag = false;

    /**
     * データベースから取得したユーザー名のリスト
     */
    public String[] users ;

    /**
     * データベースからの結果セット
     */
    public ResultSet rs;

    /**
     * 一秒後ごとに動作する　actionperformedに飛ぶ
     */
    public Timer timer = new Timer(1000, this);

    /**
     * フレームのインスタンス
     */
    public JFrame passFrame = new JFrame("ワンタイムパスワード");
    
    /**
     * パネルのインスタンス
     */
    public JPanel pal1  = new JPanel(); 
    public JPanel pal2  = new JPanel(new GridLayout(3, 1));
    public JPanel pal3  = new JPanel();
    public JPanel pal4  = new JPanel();
    public JPanel pal5  = new JPanel(new GridLayout(2,1));
    public JPanel pal6  = new JPanel();
    public JPanel pal7  = new JPanel();

    /**
     * アイコンのインスタンス
     */
    public ImageIcon icon ;

    /**
     * テキストフィールドのインスタンス
     */
    public JTextField nameField  = new JTextField("ユーザー名");
    public JTextField pasField   = new JTextField("ワンタイムパスワード");

    /**
     * ラベルのインスタンス
     */
    public JLabel titleIcon ;
    public JLabel manIcon;
    public JLabel pasIcon;
    public JLabel resultLabel = new JLabel();

    /**
     * ボタンのインスタンス
     */
    public JButton loginBtn          = new JButton("ログインする");
    public JButton onetimePasBtn     = new JButton("ワンタイムパスワードを送信");

    /**
     * ワンタイムパスワード送信画面を表示するメソッドです。
     * ユーザー名とワンタイムパスワードの入力フィールド、送信ボタン、および結果表示ラベルを含む画面を構築して表示します。
     * 
     * ユーザー名とワンタイムパスワードの入力フィールドおよび送信ボタンのアクションリスナーはこのクラス自体が担当し、
     * ボタンがクリックされると {@link #actionPerformed(ActionEvent)} メソッドが呼び出されます。
     */
    public void password() {

        // フレームの設定
        passFrame.setSize(600, 600);
        passFrame.setLayout(new GridLayout(2, 1));

        // パネルの背景色設定
        pal1.setBackground(Color.white);        
        pal3.setBackground(Color.white);
        pal4.setBackground(Color.white);
        pal6.setBackground(Color.white);
        pal7.setBackground(Color.white);

        //　テキストフィールドのサイズ設定
        nameField.setPreferredSize(new Dimension(300, 30));
        pasField.setPreferredSize(new Dimension(300, 30));

        // ボタンの設定
        loginBtn.setPreferredSize(new Dimension(200, 70));
        loginBtn.addActionListener(this);
        onetimePasBtn.setPreferredSize(new Dimension(200, 25));
        onetimePasBtn.addActionListener(this);

        // 結果表示ラベルのサイズ設定
        resultLabel.setPreferredSize(new Dimension(400, 30));

        // アイコンの設定
        icon      = new ImageIcon("img/lemon.png");
        titleIcon = new JLabel(icon);

        icon      = new ImageIcon("img/man.png");
        manIcon   = new JLabel(icon);

        icon      = new ImageIcon("img/pas.png");
        pasIcon   = new JLabel(icon);

        // パネルおよびコンポーネントの追加
        passFrame.add(pal1);
        passFrame.add(pal2);

        pal1.add(titleIcon);
        pal2.add(pal3);
        pal2.add(pal4);
        pal2.add(pal5);
        pal3.add(nameField);
        pal3.add(manIcon);
        pal3.add(pasField);
        pal3.add(pasIcon);
        pal4.add(loginBtn);
        pal5.add(pal6);
        pal5.add(pal7);
        pal6.add(onetimePasBtn);
        pal7.add(resultLabel);

        // フレームの設定
        passFrame.setResizable(false);
        passFrame.setVisible(true);

    }
    
    /**
     * データベースに接続し、ユーザー名の一覧を取得するメソッドです。
     * データベースへの接続を確立し、指定されたテーブルからユーザー名の一覧を取得します。
     * ユーザー名の取得には SQL クエリを使用し、データベースからの結果セットを処理します。
     * 
     * @throws SQLException データベースの処理中にエラーが発生した場合
     * @throws IllegalStateException データベース ドライバが見つからない場合
     */
    public void dbConnect() throws SQLException, IllegalStateException {
        
        try {
            // データベース ドライバのロードを試行
            Class.forName("org.mariadb.jdbc.Driver");

        } catch (ClassNotFoundException anException) {
            // データベース ドライバが見つからない場合は例外をスロー
            throw new IllegalStateException("Cannot load DB driver");

        }

        // データベース接続情報の設定
        String server_ip   = "119.230.52.86";
        int    server_port = 3306;
        String db_name     = "lemon";
        String db_user     = "test";
        String db_password = "password";

         // 接続 URL の構築
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mariadb://");
        url.append(server_ip).append(":").append(server_port).append("/");
        url.append(db_name);
        url.append("?user=").append(db_user);
        url.append("&password=").append(db_password);

        try (
            // データベースに接続
            Connection aConnection = DriverManager.getConnection(url.toString())
        ) {
            // ユーザー名の数を取得するクエリの実行
            String sql = "SELECT count(name) FROM " + table_name;
            PreparedStatement aStatement = aConnection.prepareStatement(sql);

            // クエリの実行と結果の取得
            rs = aStatement.executeQuery();
            rs.next();
            int num = rs.getInt(1);
            users = new String[num];

             // ユーザー名の取得クエリの実行
            sql = "SELECT name FROM " + table_name;
            aStatement = aConnection.prepareStatement(sql);
            rs = aStatement.executeQuery();

            // ユーザー名を配列に格納
            while(rs.next()){
                
                users[i] = rs.getString("name");
                i++;
                
            }

        } catch (SQLException anException) {
            // エラーが発生した場合は例外をスロー
            throw new IllegalStateException(anException.getMessage());
        }

    }
    
    
    
    /**
     * アクションが発生した際の処理を行うメソッドです。
     * ワンタイムパスワードの生成と検証、およびタイマーによる制限時間の管理を行います。
     *
     * @param event アクションイベント
     */
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == onetimePasBtn) {
            
            // タイマーを開始し、制限時間を設定
            timer.start();
            sec = 60;
            resultLabel.setText(sec + "秒以内に入力して下さい");
            
            // 現在の時刻とランダム値を組み合わせてワンタイムパスワードを生成
            Calendar calendar = Calendar.getInstance();
            int hour    = calendar.get(Calendar.HOUR);
            int minute  = calendar.get(Calendar.MINUTE);
            int second  = calendar.get(Calendar.SECOND);
            int random  = (int)(Math.random()*1000);
            int random2 = (int)(Math.random()*1000);
            onetimePas = (hour + minute + second)* random * random2;
            resultpass = onetimePas;
            
            System.out.println("ワンタイムパスワード" + onetimePas);
            
        }

        if (event.getSource() == loginBtn) {
            
            try {

                dbConnect();

            } catch (IllegalStateException e) {

                e.printStackTrace();

            } catch (SQLException e) {

                e.printStackTrace();
            }
            
            if (pasField.getText().length() <= 9 && Integer.parseInt(pasField.getText()) != 0) {
                
                pass = Integer.valueOf(pasField.getText());

            }


            userName = nameField.getText();

            for(int f = 0; f < users.length; f++){
                
                if (userName.equals(users[f])) {
                    userFlag = true;
                }

            }
            
            if(pass == resultpass && userFlag == true){
                // パスワードとユーザー名の検証が成功した場合
                timer.stop();
                resultLabel.setText("ログインが完了しました");

            } else {
                
                if (resultpass == 0) {
                    resultLabel.setText("再度ワンタイムパスワードを生成してください");
                }else {
                    resultLabel.setText("パスワードが違います");
                }
            }
            
        }

        if(event.getSource() == timer) {
            // タイマーによる制限時間管理
            if(sec == 0) {
                resultLabel.setText("再度ワンタイムパスワードを生成してください");
                resultpass = 0;
                timer.stop();
            } else {
                sec--;
                resultLabel.setText(sec + "秒以内に入力して下さい");
            }

        }

    }

}
