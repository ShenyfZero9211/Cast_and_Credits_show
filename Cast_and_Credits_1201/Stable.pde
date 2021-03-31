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
