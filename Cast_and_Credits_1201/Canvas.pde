/**  
*    canvas画布管理类
*    管理画布上的所有actor"演员"[这个概念或是比喻，很想ue4引擎里的actor定义]
*    其中AddActor() 设置添加演职表内容至相应actor的方法
**/
class Canvas {
  ArrayList<Actor> allactor;
  StarringStore m_starringstore;
  CanvasController cancontrol;
  Canvas(AniEngine anie) {
    allactor = new ArrayList<Actor>();
    cancontrol = new CanvasController(allactor, anie);
  }

  void AddActor()
  {
    int fint = 1;
    int lint = 0;
    for (int i = 0; i < m_starringstore.stables.size(); i ++,fint ++,lint = 0)
    {
      Stable st = m_starringstore.stables.get(i);
      println(st.names);
      
      //让其呈现在画布不同位置上，加了一个随机向量
      PVector _pos = new PVector(random(0+100,width-250),random(0+100,height-350));
      int temp_posy = 0;
      for(int j = 0;j < st.names.size(); j++)
      {
         String nextstrid = String.valueOf(fint) + "_"+ String.valueOf(lint);//依次给每行字符串添加编号
        TextActor ac = new TextActor();
        ac.setID(nextstrid);
        ac.setText(st.names.get(j));
        //ac.setPos(new PVector(200, 200 + temp_posy));
        ac.setPos(new PVector(0+_pos.x, 0 + _pos.y+ temp_posy) );
        allactor.add(ac);
        
        temp_posy += 90;
        if(!(j == st.names.size() -1))
           lint ++;
      }

      
    }


    CursorActor ca = new CursorActor();
    ca.setID("0_0");
    allactor.add(ca);

    cancontrol.beginActor(ca);
    //println(m_starringstore.stables.get(0).firstname,m_starringstore.stables.get(0).lastname);
  }

  void update()
  {

    cancontrol.update();
  }

  void draw()
  {
    //所有演员（字符串）呈现方法的遍历
    for (Actor ac : allactor)
    {
      ac.draw();
    }
  }
}
