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
 * 新規アカウントの登録画面を提供するクラスです。
 * 
 * 
 */
public class SignUpGUI extends JFrame implements ActionListener {
    
    /**
     * データベースのテーブル名
     */
    public String table_name = "user";
    
    /**
     * 新しいユーザー名
     */
    public String newUserName;

    /**
     * 新しいパスワード
     */
    public String newPassword;

    /**
     * DBの連番管理のためのid
     */ 
    public int id = 0 ;

    /**
     * データベースからの結果セット
     */
    public ResultSet rs;

    /**
     * パネルのインスタンス
     */
    public JPanel pal1  = new JPanel();
    public JPanel pal2  = new JPanel(new GridLayout(3, 1));
    public JPanel pal3  = new JPanel();
    public JPanel pal4  = new JPanel();
    public JPanel pal5  = new JPanel();

    /**
     * アイコンのインスタンス
     */
    public ImageIcon icon ;

    /**
     * ラベルのインスタンス
     */
    public JLabel titleIcon;
    public JLabel manIcon;
    public JLabel pasIcon;
    public JLabel resultLabel = new JLabel();;
    
    /**
     * フレームのインスタンス
     */
    public JFrame signUpFrame    = new JFrame("新規登録");
    
    /**
     * テキストフィールドのインスタンス
     */
    public JTextField nameField  = new JTextField("ユーザー名");
    public JTextField pasField   = new JTextField("パスワード");

    /**
     * ボタンのインスタンス
     */
    public JButton newAccountBtn = new JButton("新規登録");

    /**
     * 新規登録GUIを初期化し、フレームを設定します。
     */
    public void signup() {

        // フレームのサイズとレイアウトの設定
        signUpFrame.setSize(600, 600);
        signUpFrame.setLayout(new GridLayout(2,1));

        // テキストフィールドのサイズ設定
        nameField.setPreferredSize(new Dimension(300, 30));
        pasField.setPreferredSize(new Dimension(300, 30));

        // ボタンの設定とアクションリスナーの登録
        newAccountBtn.setPreferredSize(new Dimension(200, 70));
        newAccountBtn.addActionListener(this);

         // パネルとコンポーネントの追加
        signUpFrame.add(pal1);
        signUpFrame.add(pal2);

        pal2.add(pal3);
        pal2.add(pal4);
        pal2.add(pal5);

        pal1.setBackground(Color.white);
        pal3.setBackground(Color.white);
        pal4.setBackground(Color.white);
        pal5.setBackground(Color.white);

        // アイコンの設定
        icon      = new ImageIcon("img/lemon.png");
        titleIcon = new JLabel(icon);

        icon      = new ImageIcon("img/man.png");
        manIcon   = new JLabel(icon);

        icon      = new ImageIcon("img/pas.png");
        pasIcon   = new JLabel(icon);

        // コンポーネントの追加
        pal1.add(titleIcon);
        pal3.add(nameField);
        pal3.add(manIcon);
        pal3.add(pasField);
        pal3.add(pasIcon);
        pal4.add(newAccountBtn);
        pal5.add(resultLabel);

         // フレームの設定と表示
        signUpFrame.setResizable(false);
        signUpFrame.setVisible(true);

    }

   /**
    * データベースに新規ユーザー情報を登録するメソッドです。
    * @throws SQLException データベース操作中にエラーが発生した場合
    * @throws IllegalStateException データベースドライバのロードに失敗した場合
    */
    public void dbConnect() throws SQLException, IllegalStateException {
        
        try {
            // MariaDB JDBCドライバをロード
            Class.forName("org.mariadb.jdbc.Driver");

        } catch (ClassNotFoundException anException) {
            // ドライバのロードに失敗した場合、例外をスロー
            throw new IllegalStateException("Cannot load DB driver");

        }

        // データベース接続情報の設定
        String server_ip   = "119.230.52.86";
        int    server_port = 3306;
        String db_name     = "lemon";
        String db_user     = "test";
        String db_password = "password";

         // 接続URLの作成
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mariadb://");
        url.append(server_ip).append(":").append(server_port).append("/");
        url.append(db_name);
        url.append("?user=").append(db_user);
        url.append("&password=").append(db_password);

        try (
            // データベースへの接続を作成
            Connection aConnection = DriverManager.getConnection(url.toString())
        ) {
            // データベース操作
            String sql = "select id from " + table_name;
            PreparedStatement aStatement = aConnection.prepareStatement(sql);
            rs = aStatement.executeQuery();
            rs.last();
            id  = rs.getInt(1);
            id += 1;

            sql = "INSERT INTO " + table_name + " VALUES(?,?,?)";
            aStatement = aConnection.prepareStatement(sql);
            newUserName = nameField.getText();
            newPassword = pasField.getText();
            
            aStatement.setInt(1, id);
            aStatement.setString(2, newUserName);
            aStatement.setString(3, newPassword);
            aStatement.executeUpdate();

        } catch (SQLException anException) {
            // 例外が発生した場合、例外をスロー
            throw new IllegalStateException(anException.getMessage());
        }

    }

    /**
     * アクションイベントが発生した際に呼ばれるメソッドです。
     *
     * @param event アクションイベント
     */
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == newAccountBtn) {
            
            if (pasField.getText().length() >=10) {
                
                resultLabel.setPreferredSize(new Dimension(400, 30));
                resultLabel.setText("新規会員登録が完了しました");
                
                try {
                     // データベースへの接続と新規ユーザー情報の登録を試行
                    dbConnect();
                    
                } catch (SQLException e) {
                    // エラーが発生した場合、スタックトレースを表示
                    e.printStackTrace();
                }
                

            } else {
                
                resultLabel.setText("10文字以上のパスワードを設定してください");
            }
            

        }

    }

}
