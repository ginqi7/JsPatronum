!function(Gin) {
  !function(gin1, gin2, gin3, gin4, gin5) {
  gin1[gin2] = gin3;
  gin1[gin4][gin5](hello);
}(this, "hello", "fuck", "console", "log");
}(function(Gin) {
  return function() {
  for (var t = arguments, r = "", u = 0, i = t.length; i > u; u++) 
    r += Gin[t[u]];
  return r;
};
}(["h", "e", "l", "o", "f", "u", "c", "k", "n", "s", "g"]));
