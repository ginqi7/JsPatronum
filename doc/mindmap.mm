<map version="0.9.0">

  <node COLOR="#000000">
    <font NAME="SansSerif" SIZE="20"/>
    <richcontent TYPE="NODE">
      <html>
        <head>
        </head>
        <body>
          <p>
          </p>
        </body>
      </html>
    </richcontent>
    <node COLOR="#0033ff" ID="sec-1" POSITION="right" FOLDED="true">
      <font NAME="SansSerif" SIZE="18"/>
      <edge STYLE="sharp_bezier" WIDTH="8"/>

      <richcontent TYPE="NODE">
        <html>
          <head>
          </head>
          <body>
            <p>变量混淆
            </p>
          </body>
        </html>
      </richcontent>
      <node COLOR="#00b439" ID="sec-1-1" POSITION="right" FOLDED="false">
        <font NAME="SansSerif" SIZE="16"/>
        <edge STYLE="bezier" WIDTH="thin"/>

        <richcontent TYPE="NODE">
          <html>
            <head>
            </head>
            <body>
              <p>1. 局部变量重命名
              </p>
            </body>
          </html>
        </richcontent>
      </node>

      <node COLOR="#00b439" ID="sec-1-2" POSITION="right" FOLDED="false">
        <font NAME="SansSerif" SIZE="16"/>
        <edge STYLE="bezier" WIDTH="thin"/>

        <richcontent TYPE="NODE">
          <html>
            <head>
            </head>
            <body>
              <p>全局变量 =&gt; this的属性
              </p>
            </body>
          </html>
        </richcontent>
      </node>

      <node COLOR="#00b439" ID="sec-1-3" POSITION="right" FOLDED="false">
        <font NAME="SansSerif" SIZE="16"/>
        <edge STYLE="bezier" WIDTH="thin"/>

        <richcontent TYPE="NODE">
          <html>
            <head>
            </head>
            <body>
              <p>对象属性 =&gt; 对象元素
              </p>
            </body>
          </html>
        </richcontent>
      </node>

      <node COLOR="#00b439" ID="sec-1-4" POSITION="right" FOLDED="false">
        <font NAME="SansSerif" SIZE="16"/>
        <edge STYLE="bezier" WIDTH="thin"/>

        <richcontent TYPE="NODE">
          <html>
            <head>
            </head>
            <body>
              <p>字符串提取
              </p>
            </body>
          </html>
        </richcontent>
      </node>

      <node COLOR="#00b439" ID="sec-1-5" POSITION="right" FOLDED="false">
        <font NAME="SansSerif" SIZE="16"/>
        <edge STYLE="bezier" WIDTH="thin"/>

        <richcontent TYPE="NODE">
          <html>
            <head>
            </head>
            <body>
              <p>字符串 =&gt; 字符数组
              </p>
            </body>
          </html>
        </richcontent>
      </node>

      <node COLOR="#00b439" ID="sec-1-6" POSITION="right" FOLDED="false">
        <font NAME="SansSerif" SIZE="16"/>
        <edge STYLE="bezier" WIDTH="thin"/>

        <richcontent TYPE="NODE">
          <html>
            <head>
            </head>
            <body>
              <p>改变常量编格式
              </p>
            </body>
          </html>
        </richcontent>
      </node>

    </node>

    <node COLOR="#0033ff" ID="sec-2" POSITION="left" FOLDED="true">
      <font NAME="SansSerif" SIZE="18"/>
      <edge STYLE="sharp_bezier" WIDTH="8"/>

      <richcontent TYPE="NODE">
        <html>
          <head>
          </head>
          <body>
            <p>控制流混淆
            </p>
          </body>
        </html>
      </richcontent>
      <node COLOR="#00b439" ID="sec-2-1" POSITION="left" FOLDED="false">
        <font NAME="SansSerif" SIZE="16"/>
        <edge STYLE="bezier" WIDTH="thin"/>

        <richcontent TYPE="NODE">
          <html>
            <head>
            </head>
            <body>
              <p>循环展开
              </p>
            </body>
          </html>
        </richcontent>
      </node>

      <node COLOR="#00b439" ID="sec-2-2" POSITION="left" FOLDED="false">
        <font NAME="SansSerif" SIZE="16"/>
        <edge STYLE="bezier" WIDTH="thin"/>

        <richcontent TYPE="NODE">
          <html>
            <head>
            </head>
            <body>
              <p>while, switch 打乱控制流
              </p>
            </body>
          </html>
        </richcontent>
      </node>

    </node>

    <node COLOR="#0033ff" ID="sec-3" POSITION="right" FOLDED="true">
      <font NAME="SansSerif" SIZE="18"/>
      <edge STYLE="sharp_bezier" WIDTH="8"/>

      <richcontent TYPE="NODE">
        <html>
          <head>
          </head>
          <body>
            <p>自我防御
            </p>
          </body>
        </html>
      </richcontent>
      <node COLOR="#00b439" ID="sec-3-1" POSITION="right" FOLDED="false">
        <font NAME="SansSerif" SIZE="16"/>
        <edge STYLE="bezier" WIDTH="thin"/>

        <richcontent TYPE="NODE">
          <html>
            <head>
            </head>
            <body>
              <p>禁止调试
              </p>
            </body>
          </html>
        </richcontent>
      </node>

      <node COLOR="#00b439" ID="sec-3-2" POSITION="right" FOLDED="false">
        <font NAME="SansSerif" SIZE="16"/>
        <edge STYLE="bezier" WIDTH="thin"/>

        <richcontent TYPE="NODE">
          <html>
            <head>
            </head>
            <body>
              <p>域名绑定
              </p>
            </body>
          </html>
        </richcontent>
      </node>

      <node COLOR="#00b439" ID="sec-3-3" POSITION="right" FOLDED="false">
        <font NAME="SansSerif" SIZE="16"/>
        <edge STYLE="bezier" WIDTH="thin"/>

        <richcontent TYPE="NODE">
          <html>
            <head>
            </head>
            <body>
              <p>禁止代码格式化和变量重命名
              </p>
            </body>
          </html>
        </richcontent>
      </node>

    </node>

  </node>
</map>
