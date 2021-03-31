import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Cast_and_Credits_1201 extends PApplet {

/*****************************************
  模仿《终结者1》片头 演职表效果
  主要3个部分： 1.演职表文本数据结构搭建
                2.画布呈现内容，基于actor为单位
                3.actor"出演"次序（逻辑）
                
                          sharp eye
                          2021.3.30
******************************************/

StarringStore starringstore;  //演职表仓库
AniEngine aniEngine;          //动画引擎，包含绘制输出

/**  
*    初始化、配置信息   分析处理text,准备动画引擎
**/
public void Init()
{
  HandleSourceText();
  starringstore = HandleStarring(strs);
  aniEngine = new AniEngine();
  aniEngine.initEngine();
}

public void settings()
{
  size(1600, 900);
}

public void setup()
{
  Init();
  starringstore.ShowStables();//text信息反馈
}


public void draw()
{
  background(10);
  aniEngine.run();//动画引擎运行时，包括绘制、时间推演
}
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
  public void update()
  {
    
  }
  
  public void draw()
  {
    
  }
}
/**  
*    动画绘制引擎！！主要有actor逻辑、绘制遍历，时间推演
*    包含canvas画布管理类
**/
class AniEngine {

  float deltatime;
  float oldtime;
  float nowtime;
  float counttime;

  EngineConfig config;
  Canvas canvas;

  AniEngine() {
    config = new EngineConfig();
    canvas = new Canvas(this);
    
    nowtime = millis();
    deltatime = nowtime - oldtime;
    oldtime = nowtime;
  }
  public void initEngine() {
    canvas.m_starringstore = starringstore;
    canvas.AddActor();
  }



  public void run()
  {
    nowtime = millis();
    deltatime = nowtime - oldtime;
    oldtime = nowtime;

    canvas.update();

    canvas.draw();
  }
}  
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

  public void AddActor()
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

  public void update()
  {

    cancontrol.update();
  }

  public void draw()
  {
    //所有演员（字符串）呈现方法的遍历
    for (Actor ac : allactor)
    {
      ac.draw();
    }
  }
}
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


  public void beginActor(Actor ac)
  {
    //actor类中定义好的效果，即 isshow为true,那么动画就播放，绘制就开始
    ac.isshow = true;
    m_cursoractor.isshow = true;
    m_cursoractor.setPos(ac.pos);
    // m_currentactor = ac;
    //tempactors.add(m_currentactor);
  }

  public void beginActor(CursorActor ca)
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

  public void update()
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

  public void update()
  {
    if(openflash)
    {
      alpha = alpha == 255 ? 0 : 255;
    }
    else
      alpha = 255;
  }

  public void draw()
  {
    if (!isshow) return;
    
    push();
    translate(pos.x, pos.y);
    fill(255,alpha);
    //rect(-25-4, -25, 25, 25, 4);
    rect(-30*2.5f, -25*2.4f, 25*2.5f, 25*2.5f, 4*1.6f);

    pop();
  }
}
/**  
*    可添加的配置类，用于控制整个引擎运行的参数，如速度，字体大小等
*    可设为读取外部info文本来设置
**/
class EngineConfig{
  
}
/**  
*    Functions   基本逻辑调用，不隶属于任何类
**/

public StarringStore HandleStarring(ArrayList<String> _strs)
{
  StarringStore ss = new StarringStore();

  for (int i = 0; i < _strs.size(); i ++)
  {
    String str = _strs.get(i);
    String[] strs = str.split(" ");
    ss.Add2Stables(strs);
  }

  return ss;
}

ArrayList<String> strs = new ArrayList<String>();
String[] lines;
int count;

public void HandleSourceText()
{
    lines = loadStrings("stables.txt");
  int index = 0;
  while (index < lines.length) {
    String[] pieces = split(lines[index], "###");

    for (String str : pieces)
    {
      if (str.length() >= 1)
      {
        strs.add(str);
        count ++;
      }
    }
    index = index + 1;
  }
}
/**  
*    文本信息单位，供仓库使用
**/
class Stable {
  ArrayList<String> names;

  Stable(String[] strs) {
    names = new ArrayList<String>();
    for(int i = 0; i < strs.length;i ++)
    {
      String str = strs[i];
      names.add(str);
    }
  }
}
/**  
*    演职表信息存储的类[仓库]
*    ----可以任意修改文本来改变呈现内容----
**/
class StarringStore {
  ArrayList<Stable> stables;

  StarringStore() {
    stables = new ArrayList<Stable>();
  }

  public void Add2Stables(String[] names)
  {
    Stable st = new Stable(names);
    stables.add(st);
  }

  public void ShowStables() {
    for (int i = 0; i < stables.size(); i ++)
    {
      Stable st = stables.get(i);
    }
  }
}
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




  public void update()
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

  public void draw()
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Cast_and_Credits_1201" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
