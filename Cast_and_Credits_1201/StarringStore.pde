/**  
*    演职表信息存储的类[仓库]
*    ----可以任意修改文本来改变呈现内容----
**/
class StarringStore {
  ArrayList<Stable> stables;

  StarringStore() {
    stables = new ArrayList<Stable>();
  }

  void Add2Stables(String[] names)
  {
    Stable st = new Stable(names);
    stables.add(st);
  }

  void ShowStables() {
    for (int i = 0; i < stables.size(); i ++)
    {
      Stable st = stables.get(i);
    }
  }
}
