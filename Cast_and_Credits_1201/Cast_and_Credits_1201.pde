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
void Init()
{
  HandleSourceText();
  starringstore = HandleStarring(strs);
  aniEngine = new AniEngine();
  aniEngine.initEngine();
}

void settings()
{
  size(1920, 1080);
}

void setup()
{
  Init();
  starringstore.ShowStables();//text信息反馈
}


void draw()
{
  background(10);
  aniEngine.run();//动画引擎运行时，包括绘制、时间推演
}
