(TeX-add-style-hook
 "highlight"
 (lambda ()
   (TeX-run-style-hooks
    "colordvi")
   (TeX-add-symbols
    "hlstd"
    "hlnum"
    "hlesc"
    "hlstr"
    "hlpps"
    "hlslc"
    "hlcom"
    "hlppc"
    "hllin"
    "hlopt"
    "hlipl"
    "hlkwa"
    "hlkwb"
    "hlkwc"
    "hlkwd"))
 :latex)

