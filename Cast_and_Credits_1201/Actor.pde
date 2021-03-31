/**  
*    画布上所有可呈现的单位，抽象类，一种概念单位，不能实例
**/
abstract class Actor{
  String IDnumber; //出场次序记录  如 - 1_0 1_1 2_1 2_2
  PVector pos;
  boolean isshow;
  boolean isfinished;
  boolean isdying = false;
  boolean isdead = false;
  Actor()
  {
    pos = new PVector();
    isshow = false;
    isfinished = false;
  }
  
    public void setID(String strid)
  {
    this.IDnumber = strid;
  }

  public void setPos(PVector pv)
  {
    this.pos = pv.copy();
  }
  
    public void setdying(){
    isdying = true;
  }
  void update()
  {
    
  }
  
  void draw()
  {
    
  }
}
