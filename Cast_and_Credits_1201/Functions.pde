/**  
*    Functions   基本逻辑调用，不隶属于任何类
**/

StarringStore HandleStarring(ArrayList<String> _strs)
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

void HandleSourceText()
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
