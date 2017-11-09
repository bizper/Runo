# USER ATTENTION

To use this software, you need to import the **runo** jar file.

This software written by _KOTLIN_.

To parse the json string, you can use the following code.
```
Parser p = new Parser();
p.parse("your json file path");
```
And search information from this json file, you can use the following code.
```
Bean b = p.parse("your json file path");
b.check("$.elementName");
b.check("$.elementName.#");//get the length of array
b.check("$.elementName[#-1]");//get the last element of array
```
support normal operator such as __+ - * /__

all result will be casted to __String__.

out of array index will get __Null__.

###### SEARCH SYNTAX

  symbol |    meaning    
:-------:|:---------------:
   $     | root node
   .     |    sperator   
  [x]    |x is index of array
   @     |  current node
   #     | length of element
