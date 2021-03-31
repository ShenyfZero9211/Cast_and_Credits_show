/**  
*    光标actor，其中设置了闪烁效果
**/
class CursorActor extends Actor {
  int alpha = 255;
  boolean openflash = false;
  CursorActor()
  {
  }

  public void setflash(boolean b)
  {
    openflash = b;
  }

  void update()
  {
    if(openflash)
    {
      alpha = alpha == 255 ? 0 : 255;
    }
    else
      alpha = 255;
  }

  void draw()
  {
    if (!isshow) return;
    
    push();
    translate(pos.x, pos.y);
    fill(255,alpha);
    //rect(-25-4, -25, 25, 25, 4);
    rect(-30*2.5, -25*2.4, 25*2.5, 25*2.5, 4*1.6);

    pop();
  }
}
