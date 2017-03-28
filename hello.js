function NQueens(order) {
    if (order < 4) {
        console.log("N Queens problem apply for order bigger than 3 ! ");
        return;
    }

    var nQueens = [];
    var backTracking = false;
    rowLoop:
    for (var row=0; row<order; row++) {
        //若出现row小于0, 则说明问题无解
        if(row < 0){
            console.log("This N Queens problem has no solution ! ");
            break;
        }
        //第一次检测到新的一行
        if (nQueens[row] === undefined) {
            nQueens[row] = [];
        }
        //回溯时运行的程序块
        for (var col=0; col<order; col++) {
            //0为已经检测过并为能放置皇后的位置
            if (nQueens[row][col] === 0) {
                continue;
            }
            //回溯过程中，遇到能放皇后的位置，说明这个位置在后面的验证没有通过，需要重新处理
            else if (backTracking && nQueens[row][col] == 1) {
                //回溯时发现,上一行也到行末,需要继续回溯
                if (col === order-1) {
                    resetRow(nQueens, order, row);
                    row = row - 2;
                    continue rowLoop;
                }
                //回溯的行还没到行尾, 标0, 继续
                nQueens[row][col] = 0;
                backTracking = false;
                continue;
            }
            //放置一个皇后
            nQueens[row][col] = 1;
            //找到一个可以放置皇后的位置，跳出到下一行（一行上只能放一个皇后）。
            if (isQueenValid(nQueens, row, col)) {      
                continue rowLoop;
            }
            //每一行都应该有一个皇后，到列尾了还没有找到合适的位置，说明前面的皇后放置有问题，需要回溯！
            else if (col == order-1) {              
                backTracking = true;
                //0与1都表示这个位置已经检测过，因此要将本行清为undefined
                resetRow(nQueens, order, row);
                //减2是因为循环尾还有个自加，其实就是回到上一行
                row = row - 2;
                //退到外层循环，继续
                continue rowLoop;                     
            } else {
                //未到行未，继续检测未检测过的
                nQueens[row][col] = 0;                
                continue;
            };
        }
    }
    return nQueens;
}
//回溯前, 将本行清除
function resetRow(nQueens, order, row) {
    for (var col=0; col<order; col++) {
        nQueens[row][col] = undefined;
    }
}
//检测位置是否能放置皇后
function isQueenValid(nQueens, row, col) {
    //行检测
    for (var i=0; i<col; i++) {
        if (nQueens[row][i] == 1) {
            return false;
        }
    }
    for (var j=1; j<row+1; j++) {
        //      列检测                     左上45度                          右上45度
        if (nQueens[row-j][col]==1 || (nQueens[row-j][col-j]==1) || (nQueens[row-j][col+j]==1)) {
            return false;
        }
    }
    return true;
}

function printQ(queens) {
    for (var row=0; row<queens.length; row++) {
        var rowText = '';
        for (var col=0; col<queens.length; col++) {
            if (queens[row][col]===undefined) {
                queens[row][col] = 0;
            }
            rowText = rowText + queens[row][col] + '  ';
        }
        console.log(rowText);
    }
}

var queens = NQueens(8);
printQ(queens);


