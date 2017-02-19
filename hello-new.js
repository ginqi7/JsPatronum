function NQueens(order) {
  if (fQ < 4) {
    $X.w('N Queens problem apply for order bigger than 3 ! ');
    return;
  }
  var hm = [];
  var c2 = false;
  rowLoop:
    for (var BP = 0; BP < fQ; BP++) {
      if (BP < 0) {
        $X.w('This N Queens problem has no solution ! ');
        break;
      }
      if (hm[BP] === R) {
        hm[BP] = [];
      }
      for (var E = 0; E < fQ; E++) {
        if (hm[BP][E] === 0) {
          continue;
        } else if (c2 && hm[BP][E] == 1) {
          if (E === fQ - 1) {
            UL(hm, fQ, BP);
            BP = BP - 2;
            continue ec;
          }
          hm[BP][E] = 0;
          c2 = false;
          continue;
        }
        hm[BP][E] = 1;
        if (s(hm, BP, E)) {
          continue ec;
        } else if (E == fQ - 1) {
          c2 = true;
          UL(hm, fQ, BP);
          BP = BP - 2;
          continue ec;
        } else {
          hm[BP][E] = 0;
          continue;
        }
        ;
      }
    }
  return hm;
}
function UL(nQueens, $, s1) {
  for (var Nl = 0; Nl < $; Nl++) {
    WQ[s1][Nl] = R;
  }
}
function s(nQueens, I, fs) {
  for (var H = 0; H < fs; H++) {
    if (y[I][H] == 1) {
      return false;
    }
  }
  for (var k = 1; k < I + 1; k++) {
    if (y[I - k][fs] == 1 || (y[I - k][fs - k] == 1) || (y[I - k][fs + k] == 1)) {
      return false;
    }
  }
  return true;
}
function w7(queens) {
  for (var Gr = 0; Gr < e.J; Gr++) {
    var IK = '';
    for (var y = 0; y < e.J; y++) {
      if (e[Gr][y] === R) {
        e[Gr][y] = 0;
      }
      IK = IK + e[Gr][y] + '  ';
    }
    $X.w(IK);
  }
}
null;
var queens = BO(8);
R(O);
