import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * BulletinBoardクラスは、掲示板の表示とユーザーの投稿を管理するクラスです。
 * ActionListenerインターフェースを実装し、投稿ボタンのアクションを処理します。
 * 
 * @author 
 */
public class BulletinBoard implements ActionListener {
    
    /**
     * JFインスタンス
     */
    public JFrame bdFrame = new JFrame("lemon");

    /**
     * データベーステーブル名
     */
    public String table_name  = "user";
    public String table_name2 = "tweet";

    /**
     * コメントのフィールド
     */
    public String comment1 = null;
    public String comment2 = null;
    public String comment3 = null;
    public String comment4= null;
    public String comment5 = null;
    public String comment6 = null;
    public String comment7 = null;
    public String comment8 = null;
    public String comment9 = null;

    /**
     * DB処理の結果を取得するフィールド
     */
    public ResultSet rs;

    /**
     * データベースから取得したコメントを表示するためのJLabel
     */
    public JLabel lab1 = new JLabel();
    public JLabel lab2 = new JLabel();
    public JLabel lab3 = new JLabel();
    public JLabel lab4 = new JLabel();
    public JLabel lab5 = new JLabel();
    public JLabel lab6 = new JLabel();
    public JLabel lab7 = new JLabel();
    public JLabel lab8 = new JLabel();
    public JLabel lab9 = new JLabel();

    /**
     * 各コメントを表示するためのパネル
     */
    public JPanel pal1  = new JPanel(new FlowLayout(FlowLayout.CENTER));
    public JPanel pal2  = new JPanel(new FlowLayout(FlowLayout.LEFT));
    public JPanel pal3  = new JPanel(new FlowLayout(FlowLayout.LEFT));
    public JPanel pal4  = new JPanel(new FlowLayout(FlowLayout.LEFT));
    public JPanel pal5  = new JPanel(new FlowLayout(FlowLayout.LEFT));
    public JPanel pal6  = new JPanel(new FlowLayout(FlowLayout.LEFT));
    public JPanel pal7  = new JPanel(new FlowLayout(FlowLayout.LEFT));
    public JPanel pal8  = new JPanel(new FlowLayout(FlowLayout.LEFT));
    public JPanel pal9  = new JPanel(new FlowLayout(FlowLayout.LEFT));
    public JPanel pal10 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    public JPanel pal11 = new JPanel(new FlowLayout(FlowLayout.LEFT));
    public JPanel pal12 = new JPanel(new FlowLayout(FlowLayout.CENTER));

    /**
     * アイコン
     */
    public ImageIcon icon ;

    /**
     * 投稿ボタン
     */
    Button tweetBtn = new Button("投稿");

    /**
     * 更新ボタン
     */
    Button reloadBtn = new Button("更新");

    /**
     * タイトルアイコン
     */
    public JLabel titleIcon;

    /**
     * コメントを入力するテキストエリア
     */
    public JTextArea text1 = new JTextArea();

    /**
     * 投稿数カウント
     */
    public int count = 1;

    /**
     * ユーザー名
     */
    public String username;

    /**
     * 掲示板の表示をセットアップします。
     * ウィンドウサイズ、ラベル、パネル、ボタンなどのGUIコンポーネントを構成します。
     * @throws IllegalStateException データベースのドライバがロードできない場合に発生した例外
     * @throws SQLException データベースへの接続時に発生した例外
     */
    
    public void bulletinBoard() throws IllegalStateException, SQLException {

         // フレームのレイアウトとサイズ設定
        bdFrame.setLayout(new GridLayout(12,1));
        bdFrame.setSize(1920, 1050);
       
        // 各コメントの初期化と表示領域の設定
        lab1.setText("");
        lab1.setPreferredSize(new Dimension(800,100));
        
        lab2.setText("");
        lab2.setPreferredSize(new Dimension(800,100));

        lab3.setText("");
        lab3.setPreferredSize(new Dimension(800,100));

        lab4.setText("");
        lab4.setPreferredSize(new Dimension(800,100));

        lab5.setText("");
        lab5.setPreferredSize(new Dimension(800,100));

        lab6.setText("");
        lab6.setPreferredSize(new Dimension(800,100));

        lab7.setText("");
        lab7.setPreferredSize(new Dimension(800,100));

        lab8.setText("");
        lab8.setPreferredSize(new Dimension(800,100));

        lab9.setText("");
        lab9.setPreferredSize(new Dimension(800,100));

         // パネルの背景色設定
        pal1. setBackground(Color.white);
        pal2. setBackground(Color.white);
        pal3. setBackground(Color.white);
        pal4. setBackground(Color.white);
        pal5. setBackground(Color.white);
        pal6. setBackground(Color.white);
        pal7. setBackground(Color.white);
        pal8. setBackground(Color.white);
        pal9. setBackground(Color.white);
        pal10.setBackground(Color.white);
        pal11.setBackground(Color.white);        
        pal12.setBackground(Color.green);

        // テキストエリアの設定
        text1.setPreferredSize(new Dimension(600,100));

        // タイトルアイコンの設定
        icon      = new ImageIcon("img/lemon2.png");
        titleIcon = new JLabel(icon);
        
        // 投稿ボタンの設定とアクションリスナーの登録
        tweetBtn.setPreferredSize(new Dimension(100, 30)); 
        tweetBtn.addActionListener(this);

        //　更新ボタンの設定とアクションリスナーの登録
        reloadBtn.setPreferredSize(new Dimension(100, 30)); 
        reloadBtn.addActionListener(this);
        
        // パネルとコンポーネントの追加
        bdFrame.add(pal1);
        bdFrame.add(pal2);
        bdFrame.add(pal3);
        bdFrame.add(pal4);
        bdFrame.add(pal5);
        bdFrame.add(pal6);
        bdFrame.add(pal7);
        bdFrame.add(pal8);
        bdFrame.add(pal9);
        bdFrame.add(pal10);
        bdFrame.add(pal11);
        bdFrame.add(pal12);

         // 各パネルにコンポーネントの追加
        pal1.add(titleIcon);
        pal2.add(lab1);
        pal3.add(lab2);
        pal4.add(lab3);
        pal5.add(lab4);
        pal6.add(lab5);
        pal7.add(lab6);
        pal8.add(lab7);
        pal9.add(lab8);
        pal10.add(lab9);
        pal12.add(text1);
        pal12.add(tweetBtn);
        pal12.add(reloadBtn);

        // データベースからコメントを取得して表示
        dbConnect2();

        // フレームの可視化
        bdFrame.setVisible(true);
    
    }

    /**
     * 投稿ボタンのアクションを処理します。
     * テキストエリアの内容を取得し、コメントを表示およびデータベースに登録します。
     * @param e アクションイベント
     */
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == tweetBtn) {// 投稿ボタンが押された場合
            
            if(count < 10){// 最大投稿数に達していない場合
                
                try {
                    numberIndexAdd(count);// データベースにコメントを追加
                } catch (IllegalStateException | SQLException e1) {
                    e1.printStackTrace();
                }
                count+=1;// 投稿数をインクリメント
                text1.setText("");// テキストエリアをクリア
            } else {// 最大投稿数に達している場合
                try {
                    numberIndexAdd(count);// データベースにコメントを追加
                } catch (IllegalStateException | SQLException e1) {
                    e1.printStackTrace();
                }
                text1.setText(""); // テキストエリアをクリア
            }

        }

        if (e.getSource() == reloadBtn) {
            
            try {
                dbConnect2();
            } catch (IllegalStateException | SQLException e1) {
                e1.printStackTrace();
            }
            
        }
             
    }

    /**
     * インデックスに応じてコメントを追加し、ラベルに表示します。
     * インデックスに応じてコメントを取得し、対応するラベルに表示し、データベースに登録します。
     * @param index インデックス番号
     * @throws IllegalStateException データベースのドライバがロードできない場合に発生した例外
     * @throws SQLException データベースへの接続時に発生した例外
     */
    public void numberIndexAdd(int index) throws IllegalStateException, SQLException{

        switch (index){

            case (1):
            {   
                comment1 = text1.getText();//テキストエリアの内容を取得
                lab1.setText("<html>" + username + "<br>" + comment1 + "<html>");//ラベルに表示
                dbConnect(comment1);// データベースにコメントを追加
                break;
            }
            case (2):
            {
                comment2 = text1.getText();//テキストエリアの内容を取得
                lab2.setText("<html>" + username + "<br>" + comment2 + "<html>");//ラベルに表示
                dbConnect(comment2);// データベースにコメントを追加
                break;
            }
            case (3):
            {
                comment3 = text1.getText();//テキストエリアの内容を取得
                lab3.setText("<html>" + username + "<br>" + comment3 + "<html>");
                dbConnect(comment3);
                break;
            }
            case (4):
            {
                comment4 = text1.getText();//テキストエリアの内容を取得
                lab4.setText("<html>" + username + "<br>" + comment4 + "<html>");
                dbConnect(comment4);
                break;
            }
            case (5):
            {
                comment5= text1.getText();//テキストエリアの内容を取得
                lab5.setText("<html>" + username + "<br>" + comment5 + "<html>");
                dbConnect(comment5);
                break;
            }
            case (6):
            {
                comment6 = text1.getText();//テキストエリアの内容を取得
                lab6.setText("<html>" + username + "<br>" + comment6 + "<html>");
                dbConnect(comment6);
                break;
            }
            case (7):
            {
                comment7 = text1.getText();//テキストエリアの内容を取得
                lab7.setText("<html>" + username + "<br>" + comment7 + "<html>");
                dbConnect(comment7);
                break;
            }
            case (8):
            {
                comment8 = text1.getText();//テキストエリアの内容を取得
                lab8.setText("<html>" + username + "<br>" + comment8 + "<html>");
                dbConnect(comment8);
                break;
            }
            case (9):
            {
                comment9 = text1.getText();//テキストエリアの内容を取得
                lab9.setText("<html>" + username + "<br>" + comment9 + "<html>");
                dbConnect(comment9);
                break;
            }
            case (10):
            {
                System.out.println("もう無理です");// 投稿可能な最大数に達した場合
                break;
            }

        }

    }

    /**
     * ユーザーの投稿を指定されたインデックスのラベルに追加し、データベースにも保存します。
     * @param index インデックス番号（1から10の範囲で指定）
     * @param username ユーザー名
     * @param comment 投稿の内容
     * @throws IllegalStateException データベースドライバのロードエラーが発生した場合
     * @throws SQLException データベースへの接続エラーが発生した場合
     */
    public void numberIndexAdd(int index, String username, String comment) throws IllegalStateException, SQLException{

        switch (index){

            case (1):
            {   
                lab1.setText("<html>" + username + "<br>" + comment + "<html>");
                break;
            }
            case (2):
            {
                lab2.setText("<html>" + username + "<br>" + comment + "<html>");
                break;
            }
            case (3):
            {
                lab3.setText("<html>" + username + "<br>" + comment + "<html>");
                break;
            }
            case (4):
            {
                lab4.setText("<html>" + username + "<br>" + comment + "<html>");
                break;
            }
            case (5):
            {
                lab5.setText("<html>" + username + "<br>" + comment + "<html>");
                break;
            }
            case (6):
            {
                lab6.setText("<html>" + username + "<br>" + comment + "<html>");
                break;
            }
            case (7):
            {
                lab7.setText("<html>" + username + "<br>" + comment + "<html>");
                break;
            }
            case (8):
            {
                lab8.setText("<html>" + username + "<br>" + comment + "<html>");
                break;
            }
            case (9):
            {
                lab9.setText("<html>" + username + "<br>" + comment + "<html>");
                break;
            }
            case (10):
            {
                System.out.println("もう無理です");
                break;
            }

        }

    }

    /**
     * データベースにユーザーの投稿を追加します。
     * @param comment 投稿の内容
     * @throws IllegalStateException データベースエラーが発生した場合
     * @throws SQLException データベースへの接続エラーが発生した場合
     */
    public void dbConnect(String comment) throws SQLException, IllegalStateException {
        
        try {
            
            Class.forName("org.mariadb.jdbc.Driver");

        } catch (ClassNotFoundException anException) {
            
            throw new IllegalStateException("Cannot load DB driver");

        }

        String server_ip   = "119.230.52.86";
        int    server_port = 3306;
        String db_name     = "lemon";
        String db_user     = "test";
        String db_password = "password";

        StringBuilder url = new StringBuilder();
        url.append("jdbc:mariadb://");
        url.append(server_ip).append(":").append(server_port).append("/");
        url.append(db_name);
        url.append("?user=").append(db_user);
        url.append("&password=").append(db_password);

        try (
            Connection aConnection = DriverManager.getConnection(url.toString())
        ) {
            
            String sql = "INSERT INTO " + table_name2 + " VALUES(?,?)";
            PreparedStatement aStatement = aConnection.prepareStatement(sql);
            aStatement.setString(1, this.username);
            aStatement.setString(2, comment);
            aStatement.executeUpdate();

        } catch (SQLException anException) {
            throw new IllegalStateException(anException.getMessage());
        }

    }

    /**
     * データベースから過去の投稿を取得し、掲示板に表示します。
     * @throws IllegalStateException データベースエラーが発生した場合
     * @throws SQLException データベースへの接続エラーが発生した場合
     */
    public void dbConnect2() throws SQLException, IllegalStateException {
        
        try {
            
            Class.forName("org.mariadb.jdbc.Driver");

        } catch (ClassNotFoundException anException) {
            
            throw new IllegalStateException("Cannot load DB driver");

        }

        String server_ip   = "119.230.52.86";
        int    server_port = 3306;
        String db_name     = "lemon";
        String db_user     = "test";
        String db_password = "password";

        StringBuilder url = new StringBuilder();
        url.append("jdbc:mariadb://");
        url.append(server_ip).append(":").append(server_port).append("/");
        url.append(db_name);
        url.append("?user=").append(db_user);
        url.append("&password=").append(db_password);

        try (
            Connection aConnection = DriverManager.getConnection(url.toString())
        ) {
            
            String sql = "SELECT * FROM " + table_name2 ;
            PreparedStatement aStatement = aConnection.prepareStatement(sql);
            rs = aStatement.executeQuery();
            count = 1;
            
            while(rs.next()) {

                if(count < 10){
                
                try {

                    String user = rs.getString(1);
                    String comm = rs.getString(2);
                    numberIndexAdd(count,user,comm);

                } catch (IllegalStateException | SQLException e1) {
                    e1.printStackTrace();
                }
                count+=1;

                } else {
                    
                    try {

                        String user = rs.getString(1);
                        String comm = rs.getString(2);
                        numberIndexAdd(count,user,comm);

                    } catch (IllegalStateException | SQLException e1) {
                        e1.printStackTrace();
                    }

                }

            }

        } catch (SQLException anException) {
            throw new IllegalStateException(anException.getMessage());
        }

    }

}