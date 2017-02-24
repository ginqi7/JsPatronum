(function(A) {
  this[A] = 10;
  console.log(this[A]);
})("hello");
