!function(Gin) {
  !function(gin1, gin2) {
  gin1[gin2] = 10;
}(this, "a");
}(function(Gin) {
  return function() {
  for (var t = arguments, r = "", u = 0, i = t.length; i > u; u++) 
    r += Gin[t[u]];
  return r;
};
  !function(gin1, gin2) {
  gin1[gin2] = 10;
}(this, "a");
}(["a"]));
