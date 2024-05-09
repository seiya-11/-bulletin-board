const board = document.getElementById("board");
const height = 10;
const width  = 13;
const bomb   = 35; 
// 爆弾が置いてある場所を管理 1=爆弾、0=何もない、-1=最初にクリックされたマスと周囲
let data = [];
const result = document.getElementById("result");
const button = document.getElementById("button");

function create() {
    
    for (let h = 0; h < height; h++){
        
        var tr = document.createElement("tr");

        for (let w = 0; w < width; w++) {
            let td = document.createElement("td");
            td.addEventListener("click", left, false);
            td.addEventListener("contextmenu", right, false);
            tr.appendChild(td);
        }

        board.appendChild(tr);
    }   
    
}

function left() {
    
    //　クリックした所の現在位置の取得
    var currentRow = this.parentNode.rowIndex;
    var currentCol = this.cellIndex;


    // すでに空いているマスや旗が置いてあったら何もしない
    // 判定するためにclassList.addが用いられている
    if (this.className === "open" || this.className === "flag") {
      return;
    }
  
    //　データにボムを格納しているかどうか
    //　lengthで配列が空かどうか区別できる　javascriptは0ならfalseなため

    if (!data.length) {

        for (let i = 0; i < height; i++) {
            // 二次元配列にしている　爆弾の位置の初期化
            data[i] = Array(width).fill(0);
        }

        for (let i = currentRow - 1; i <= currentRow + 1; i++) {
        
            for (let j = currentCol - 1; j <= currentCol + 1; j++) {
            
                // 端っこをクリックしたら枠外に出ないように
                if (i >= 0 && i < height && j >= 0 && j < width) {
                data[i][j] = -1;

                }

            }

      }

      putBomb();

    }
  
    // 爆弾を踏んだか判定
    if (data[currentRow][currentCol] === 1) {

        for (let i = 0; i < height; i++) {

            for (let j = 0; j < width; j++) {

                // ミスったとこを色で表示
                if (data[i][j] === 1) {
                    board.rows[i].cells[j].classList.add("bomb");
                }

            }

        }

      board.style.pointerEvents = "none";
      button.style.pointerEvents = "none";
      result.textContent = "残念！！！";
      return;
    }
  
  // 周りのマスのボムを数える
  let bombs = countBomb(currentRow, currentCol);

  if (bombs === 0) {
    
    //　一気に開くやつ
    open(currentRow, currentCol);
    

  } else {
    
    //周りにボムがある場合はボムの数をマス目に入れる
    //一気に開かない
    this.textContent = bombs;
    this.classList.add("open");

  }

  // クリア判定
  if (countOpenCell()) {
    
    for (let i = 0; i < height; i++) {

      for (let j = 0; j < width; j++) {

        // ボムの位置を表示する clearクラスのcssを適用する
        if (data[i][j] === 1) {
          board.rows[i].cells[j].classList.remove("flag");
          board.rows[i].cells[j].classList.add("clear");
        }

      }

    }

    board.style.pointerEvents = "none";
    button.style.pointerEvents = "none";
    result.textContent = "素晴らしい！";
    return;

  }

}



function right(event) {

  //　通常の右クリックの動作を防ぐ
  event.preventDefault();

  if (this.className === "open") {
    
    return;
  
  }

  // toggleはクラスのつけ外しができる
  this.classList.toggle("flag");

}

// 爆弾を設置
function putBomb() {
  
  
  for (let i = 0; i < bomb; i++) {
  
    let flag = 0;

    while (flag === 0 ) {

      const y = Math.floor(Math.random() * height);
      const x = Math.floor(Math.random() * width);

      if (data[y][x] === 0) {

        data[y][x] = 1;
        flag = 1;

      }

    }

  }

}



// マスの周りの爆弾の数を数える

function countBomb(currentRow, currentCol) {
  
  let bombs = 0;

  for (let i = currentRow - 1; i <= currentRow + 1; i++) {

    for (let j = currentCol - 1; j <= currentCol + 1; j++) {

      //端っこ出ないように
      if (i >= 0 && i < height && j >= 0 && j < width) {

        if (data[i][j] === 1) {

          bombs++;

        }

      }

    }

  }

  return bombs;

}


// 空いているマスを数える

function countOpenCell() {
  
  let openCell = 0;

  for (let i = 0; i < height; i++) {

    for (let j = 0; j < width; j++) {

      if (board.rows[i].cells[j].className === "open") {
        openCell++;
      }

    }

  }

  if (height * width - openCell === bomb) {
    
    return true;

  } else{

    return false;

  }

}


//　一気に開けるための操作
function open(currentCol, currentRow) {

  for (let i = currentCol - 1; i <= currentCol + 1; i++) {
    
    for (let j = currentRow - 1; j <= currentRow + 1; j++) {

      if (i >= 0 && i < height && j >= 0 && j < width) {

        let bombs = countBomb(i, j);

        if (

          board.rows[i].cells[j].className === "open" ||
          board.rows[i].cells[j].className === "flag"

        ) {

          continue;
        
        }

        if (bombs === 0) {
          
          board.rows[i].cells[j].classList.add("open");
          open(i, j);

        } else {
          
          board.rows[i].cells[j].textContent = bombs;
          board.rows[i].cells[j].classList.add("open");
        
        }

      }

    }

  }

}


