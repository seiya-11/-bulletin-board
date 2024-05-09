import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * LoginGUIクラスは、ログイン画面を管理し、ユーザーのログイン処理を行います。
 * ActionListenerインターフェースを実装し、ボタンのアクションを処理します。
 * 
 * @author 
 */
public class LoginGUI extends JFrame implements ActionListener {

    /**
     * データベースのテーブル名
     */
    public String table_name = "user";

    /**
     * 入力されたユーザー名とパスワード
     */
    public String userName;
    public String userPassword;

    /**
     * データベースから取得したユーザー名とパスワード
     */
    public String dbUserName;
    public String dbUserPassword;

    /**
     * データベースから取得したユーザー情報の結果セット
     */
    public ResultSet rs;

    /**
     * 登録されているユーザー名とパスワードの配列
     */
    public String[] users ;
    public String[] passwords ;

    /**
     * GUI作成フラグ
     */
    public int    createFlag  = 0;
    public int    createFlag2 = 0;
    public int    i;

    /**
     * ユーザー名とパスワードの入力が正しいかどうか
     */
    public boolean userFlag = false;
    public boolean passFlag = false;
    
    /**
     * ログイン画面のフレーム
     */
    public JFrame loginFrame  = new JFrame("ログイン");

    /**
     * パネル
     */
    public JPanel pal1  = new JPanel();
    public JPanel pal3  = new JPanel(new GridLayout(3,1));
    public JPanel pal2  = new JPanel();
    public JPanel pal4  = new JPanel();
    public JPanel pal5  = new JPanel(new GridLayout(2,1));
    public JPanel pal6  = new JPanel();
    public JPanel pal7  = new JPanel();

    /**
     * アイコン
     */
    public ImageIcon icon ;

    /**
     * ラベル
     */
    public JLabel titleIcon ;
    public JLabel manIcon ;
    public JLabel pasIcon;
    public JLabel resultLabel = new JLabel();
    
    /**
     * テキストフィールド
     */
    public JTextField nameField = new JTextField("ユーザ名");
    public JTextField pasField = new JTextField("パスワード");
    
    /**
     * ボタン
     */
    public JButton loginBtn      = new JButton("ログインする");
    public JButton signUpBtn     = new JButton("新規登録の方はこちら");
    public JButton passwordBtn   = new JButton("パスワード忘れた方はこちら");
    
    /**
     * ログイン画面を表示します。
     * フレームの設定、GUIコンポーネントの配置などを行います。
     */
    public void login() {

        //　フレームの設定
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(600, 600);
        loginFrame.setLayout(new GridLayout(2,1));

        // 背景色の設定
        pal1.setBackground(Color.white);
        pal2.setBackground(Color.white);
        pal4.setBackground(Color.white);
        pal5.setBackground(Color.white);
        pal6.setBackground(Color.white);
        pal7.setBackground(Color.white);
        
        
        //　テキストフィールドのサイズ設定
        nameField.setPreferredSize(new Dimension(300, 30));
        pasField.setPreferredSize(new Dimension(300, 30));

        //　ボタンの設定
        loginBtn.setPreferredSize(new Dimension(200, 70));
        loginBtn.addActionListener(this);
        signUpBtn.setPreferredSize(new Dimension(200, 25));
        signUpBtn.addActionListener(this);
        passwordBtn.setPreferredSize(new Dimension(200, 25));
        passwordBtn.addActionListener(this);

        // パネルの追加
        loginFrame.add(pal1);
        loginFrame.add(pal3);

        pal3.add(pal2);
        pal3.add(pal4);
        pal3.add(pal5);
        pal5.add(pal6);
        pal5.add(pal7);

        icon      = new ImageIcon("img/lemon.png");
        titleIcon = new JLabel(icon);

        icon      = new ImageIcon("img/man.png");
        manIcon   = new JLabel(icon);

        icon      = new ImageIcon("img/pas.png");
        pasIcon   = new JLabel(icon);

        // パネルへの追加
        pal1.add(titleIcon);
        pal2.add(nameField);
        pal2.add(manIcon);
        pal2.add(pasField);
        pal2.add(pasIcon);
        pal4.add(loginBtn);
        pal4.add(resultLabel);
        pal6.add(signUpBtn);
        pal7.add(passwordBtn);
        
        //　フレームの設定
        loginFrame.setResizable(false);
        loginFrame.setVisible(true);

    }

    /**
     * データベースに接続し、ユーザー名とパスワードを取得するメソッドです。
     * データベースからユーザー名とパスワードを取得し、配列に格納します。
     * @throws IllegalStateException データベースのドライバがロードできない場合に発生した例外
     * @throws SQLException データベースへの接続時に発生した例外
     */
    public void dbConnect() throws SQLException, IllegalStateException {
        
        try {
             // MariaDBのJDBCドライバをロード
            Class.forName("org.mariadb.jdbc.Driver");

        } catch (ClassNotFoundException anException) {
             // ドライバのロード失敗時に例外をスロー
            throw new IllegalStateException("Cannot load DB driver");

        }

         // データベース接続情報の設定
        String server_ip   = "119.230.52.86";
        int    server_port = 3306;
        String db_name     = "lemon";
        String db_user     = "test";
        String db_password = "password";

        // 接続URLの構築
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mariadb://");
        url.append(server_ip).append(":").append(server_port).append("/");
        url.append(db_name);
        url.append("?user=").append(db_user);
        url.append("&password=").append(db_password);

        try (
             // データベースへの接続を確立
            Connection aConnection = DriverManager.getConnection(url.toString())
        ) {
             // ユーザー名とパスワードの取得を行うSQLクエリ
            String sql = "SELECT count(name) FROM " + table_name;
            PreparedStatement aStatement = aConnection.prepareStatement(sql);

            rs = aStatement.executeQuery();
            rs.next();
            int num = rs.getInt(1);
            users = new String[num];
            passwords = new String[num];

            sql = "SELECT name,password FROM " + table_name;
            aStatement = aConnection.prepareStatement(sql);
            rs = aStatement.executeQuery();

            i = 0;
            while(rs.next()){
                
                users[i] = rs.getString("name");
                passwords[i] = rs.getString("password");
                i++;
                
            }

        } catch (SQLException anException) {
            // SQL実行時の例外をスロー
            throw new IllegalStateException(anException.getMessage());
        }

    }

    //　ボタンを押したときの動作
    /**
     * ボタンのアクションを処理する。
     * ログインボタン、新規登録ボタン、パスワード忘れた方ボタンのアクションを処理する。
     * @param event アクションイベント
     */
    public void actionPerformed(ActionEvent event) {
        
         // ログインボタンがクリックされた場合
        if(event.getSource() == loginBtn) {

            try {
                // データベースに接続してユーザー情報を取得
                dbConnect();

            } catch (IllegalStateException e) {
                
                e.printStackTrace();

            } catch (SQLException e) {

                e.printStackTrace();

            }

            // 入力されたユーザー名とパスワードの取得
            userName = nameField.getText();
            userPassword = pasField.getText();

             // ユーザー名の存在チェック
            for(int f = 0; f < users.length; f++){
                
                if (userName.equals(users[f])) {
                    userFlag = true;
                }
            }

            // パスワードの存在チェック
            for(int j = 0; j < passwords.length; j++){
                
                if (userPassword.equals(passwords[j])) {
                    passFlag = true;
                }
            }
            
            // ユーザー名とパスワードが適切な場合
            if (passFlag == true && userFlag == true ) {
                
                resultLabel.setText("ログインが完了しました");
                BulletinBoard bd = new BulletinBoard();
                
                try {
                    bd.bulletinBoard();
                } catch (IllegalStateException | SQLException e) {
                    e.printStackTrace();
                }
                
                bd.username = this.userName;
            
            } else {
                // ユーザー名とパスワードが不適切な場合
                //部品を入れておけばsetvisibleの必要はない
                resultLabel.setText("ユーザー名とパスワードが適切ではありません");

            }

        }
        
        // 新規登録ボタンがクリックされた場合
        if(event.getSource() == signUpBtn) {
            
            if (createFlag == 0) {
                
                SignUpGUI signUpGUI = new SignUpGUI();
                signUpGUI.signup();
                createFlag++;

            } else {
                createFlag = 0;
            }
        
        }

         // パスワード忘れボタンがクリックされた場合
        if (event.getSource() == passwordBtn) {
            
            if (createFlag2 == 0) {
                
                PasswordGUI passwordGUI = new PasswordGUI();
                passwordGUI.password();
                createFlag2++;

            } else {
                createFlag2 = 0;
            }
            
        }

        

    }

}