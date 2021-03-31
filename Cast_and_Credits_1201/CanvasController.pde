/**  
*    CanvasController 画布运行控制器，主要负责逻辑业务处理
*    actor对象的出场逻辑
*    update()方法非常核心，控制了所有的出场效果
**/
class CanvasController {
  Actor m_currentactor;//当前所选的actor对象
  StarringStore m_starringstore;//就是总仓库
  ArrayList<Actor> m_allactor;
  ArrayList<Actor> tempactors;
  Actor m_cursoractor;
  AniEngine anie;
  int isshouldstop = 0;//是否需要暂停
  float counttime;//时间计时，用于控制暂停时间
  float counttime2;//时间计时 动画播放时间片控制，即为动画速度
  int m_fint;
  int m_lint;
  boolean pauseKEY = false;//暂停开关
  boolean isover = false;//是否接受
  boolean prep4over =false;//是否准备结束
  boolean wait4respawn = false;//是否等待重生
  CanvasController(  ArrayList<Actor> _allactor, AniEngine _anie) {
    tempactors = new ArrayList<Actor>();
    m_allactor = _allactor;
    m_starringstore = starringstore;
    anie = _anie;
    
    

  }


  void beginActor(Actor ac)
  {
    //actor类中定义好的效果，即 isshow为true,那么动画就播放，绘制就开始
    ac.isshow = true;
    m_cursoractor.isshow = true;
    m_cursoractor.setPos(ac.pos);
    // m_currentactor = ac;
    //tempactors.add(m_currentactor);
  }

  void beginActor(CursorActor ca)
  {
     m_cursoractor = ca;
    for (int i = 0; i < m_allactor.size(); i++)
    {
      Actor ac = m_allactor.get(i);
 
      if (ac.IDnumber.equals("1_0"))
      {
        m_currentactor = ac;
        m_currentactor.isshow = true;
         m_cursoractor.isshow = true;
        m_cursoractor.setPos(m_currentactor.pos);
        tempactors.add(m_currentactor);
        break;
      }
    }
  }

  void update()
  {

    //println(tempactors.size());

    if (m_currentactor.isfinished && pauseKEY == false && wait4respawn == false)
    {
      println(m_currentactor.IDnumber, " isfinished");
      //找下一个ID对应的actor[textactor]
      String currentstrid = m_currentactor.IDnumber;
      String[] _strs_ = currentstrid.split("_");
      int fint = Integer.parseInt(_strs_[0]);
      int lint = Integer.parseInt(_strs_[1]);

      //注意！
      //下面两个if的判断条件是很重要的，都是用来侦查是否越界了或是到了某个状态了
      //if (lint >= 1)//如果就两行
      if(lint >= m_starringstore.stables.get(fint-1).names.size()-1)
      {
        if (fint == m_starringstore.stables.size())
          //if(fint == 2)
        {
          //pauseKEY = true;
          //isover = true;
          prep4over = true;
          println("prepOVER");
          //return;
        }
        fint ++;
        lint = 0;
        isshouldstop = 1;
      } else
      {
        lint ++;
        //创建新的id,即为下一个actor
        if (!prep4over)
        {
          String nextstrid = String.valueOf(fint) + "_"+ String.valueOf(lint);

          for (int i = 0; i < m_allactor.size(); i++)
          {
            Actor ac = m_allactor.get(i);
            if (ac.IDnumber.equals(nextstrid))
            {
              m_currentactor = ac;
              tempactors.add(m_currentactor);
              break;
            }
          }
        }
      }
      
      m_fint = fint;
      m_lint = lint;


      if (isshouldstop == 0)
      {
        beginActor(m_currentactor);
      } else
      {
        //需要暂停显示。。。
        pauseKEY = true;
        m_cursoractor.isshow = true;
        println("pause");
      }
    }

    if (pauseKEY)
    {

      counttime += anie.deltatime;
      
      if(counttime >= 2400)
      {
        CursorActor ca = (CursorActor)m_cursoractor;
        ca.setflash(true);//设置光标开始闪烁
      }
      
      
      if (counttime >= 3000)
      {
        counttime = 0;
        pauseKEY = false;
        isshouldstop = 0;
        CursorActor ca = (CursorActor)m_cursoractor;
        ca.setflash(false);
        m_cursoractor.isshow = false;
        for (int i = 0; i < tempactors.size(); i++)
        {
          Actor ac = tempactors.get(i);
          ac.setdying();
          println(ac.IDnumber, "-dying-");
          wait4respawn = true;
        }
        //beginActor(m_currentactor);
      }
    }

    if (wait4respawn && tempactors.get(0).isdead && tempactors.get(1).isdead)
    {
      wait4respawn = false;
      tempactors.clear();

      if (prep4over)
      {
        m_cursoractor.isshow = false;
        isover = true;
        println("isover");
      }

      if (!isover)
      {
        String nextstrid = String.valueOf(m_fint) + "_"+ String.valueOf(m_lint);

          for (int i = 0; i < m_allactor.size(); i++)
          {
            Actor ac = m_allactor.get(i);
            if (ac.IDnumber.equals(nextstrid))//找到对应编号的actor
            {
              m_currentactor = ac;
              tempactors.add(m_currentactor);
              break;
            }
          }
        
        beginActor(m_currentactor);
        println("newspawn");
      }

      //pauseKEY = true;
      //isshouldstop = 1;
      //println("pause");
    }

    //actor对象 遍历！
    //加了时间定时模拟时间流逝，40ms为一个单位，可以修改影响运行速度

    counttime2 += anie.deltatime;
    if (counttime2 >= 40)
    {
      for (Actor ac : m_allactor)
      {
        ac.update();
      }
      counttime2 = 0;
    }
  }
}
