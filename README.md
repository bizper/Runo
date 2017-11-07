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
    b.check("$.elementName");//will return a string object
```

###### SEARCH SYNTAX

  symbol |    meaning    
:-------:|:---------------:
   $     | root location
   .     |    sperator   
  [x]    |   x is index of array
