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
  void initEngine() {
    canvas.m_starringstore = starringstore;
    canvas.AddActor();
  }



  void run()
  {
    nowtime = millis();
    deltatime = nowtime - oldtime;
    oldtime = nowtime;

    canvas.update();

    canvas.draw();
  }
}  
