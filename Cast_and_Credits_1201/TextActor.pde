/**  
*    文本actor，其中设置了透明度、颜色等信息
**/
class TextActor extends Actor {
  String textstr;
  String currentstr;
  int strindex = 0;
  int alpha = 255;
  
  TextActor()
  {
    textstr = "";
    currentstr = "";
  }



  public void setText(String str)
  {
    textstr = str;
  }




  void update()
  {
    if (!isshow) return;

    if (isfinished)
    {
      if(isdying)
      {
        alpha -= 8;
        if(alpha <= 0)
        {
          isshow = false;
          isdying = false;
          isfinished = false;
          isdead = true;
          println(IDnumber + " is dead");
        }
      }
    } else
    {
      if (strindex >= textstr.length())
        return;

      currentstr += textstr.charAt(strindex++);//依次添加字符，这样就可以实现打字般的效果
      if (strindex >= textstr.length())
      {
        isfinished = true;
      }
    }
  }

  void draw()
  {
    if (!isshow) return;

    push();
    textSize(80);
    translate(pos.x, pos.y);
    if(!isdying)
      fill(255, alpha);
    else
      fill(38,188,64, alpha);
    text(currentstr, 0, 0);
    pop();
  }
}
