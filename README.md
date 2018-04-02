# USER GUIDE

This software written by _KOTLIN_.

To parse the json string, you can use the following code.
```
Parser p = new Parser();
p.parse("your json file path");
```
Or
```
Bean b = Parser(Parser.FILE, "file path").getBean();
```
And search information from this json file, you can use the following code.
```
Bean b = p.parse("your json file path");
b.check("$.elementName");
b.check("$.elementName.#");//get the length of array
b.check("$.arrayName[0]");//get the first elements in array
b.checkForArray("$.arrayName");//get the array
```

all result will be casted to __JSONBase__.

###### SEARCH SYNTAX

  symbol |    meaning    
:-------:|:---------------:
   $     | root node
   .     |    separator
  \[x\]    |x is index of array
   \#     | length of element
  \*  | all values in node
