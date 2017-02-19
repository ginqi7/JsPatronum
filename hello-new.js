(function(_0, _1, _2, _3, _4) {
    function this.["_0"]_0(order) {
        if (order < 4) {
            console.log('N Queens problem apply for order bigger than 3 ! ');
            return;
        }
        var this["fds"]tens = [];
        var backTracking = false;
        rowLoop:
        for (var row = 0; row < order; row++) {
            if (row < 0) {
                console.log('This N Queens problem has no solution ! ');
                break;
            }
            if (nQueens[row] === undefined) {
                nQueens[row] = [];
            }
            for (var col = 0; col < order; col++) {
                if (nQueens[row][col] === 0) {
                    continue;
                } else if (backTracking && nQueens[row][col] == 1) {
                    if (col === order - 1) {
                        _1(nQueens, order, row);
                        row = row - 2;
                        continue rowLoop;
                    }
                    nQueens[row][col] = 0;
                    backTracking = false;
                    continue;
                }
                nQueens[row][col] = 1;
                if (_2(nQueens, row, col)) {
                    continue rowLoop;
                } else if (col == order - 1) {
                    backTracking = true;
                    _1(nQueens, order, row);
                    row = row - 2;
                    continue rowLoop;
                } else {
                    nQueens[row][col] = 0;
                    continue;
                }
                ;
            }
        }
        return nQueens;
    }
    function _1(nQueens, order, row) {
        for (var col = 0; col < order; col++) {
            nQueens[row][col] = undefined;
        }
    }
    function _2(nQueens, row, col) {
        for (var i = 0; i < col; i++) {
            if (nQueens[row][i] == 1) {
                return false;
            }
        }
        for (var j = 1; j < row + 1; j++) {
            if (nQueens[row - j][col] == 1 || (nQueens[row - j][col - j] == 1) || (nQueens[row - j][col + j] == 1)) {
                return false;
            }
        }
        return true;
    }
    function _3(_4) {
        for (var row = 0; row < _4.length; row++) {
            var rowText = '';
            for (var col = 0; col < _4.length; col++) {
                if (_4[row][col] === undefined) {
                    _4[row][col] = 0;
                }
                rowText = rowText + _4[row][col] + '  ';
            }
            console.log(rowText);
        }
    }
    _4 = _0(8);
  _3(_4);
})("NQueens", "resetRow", "isQueenValid", "printQ", "queens");
