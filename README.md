# Night crawler
Web crawler application, which builds a site map.

Night crawler uses:
- NIO for loading page content
- Concurrent processing for parsing

### Building	
Night crawler uses Maven as a build manager. To build the jar do:

```
mvn install
```

### Execution
```
java -jar crawler.jar [page URL]
```

### Output
JSON site map is printed to standard output. It consists of any number of page objects, each formatted as follows:
```
"[URL]" : {
  "links" : [array of URLs] OR "redirect" : [URL],
  "resources" : [array of URIs],  
}
```

#### Example
```
{
   "http://www.example.com":{
      "links":[
         "http://www.google.com",
         "http://www.example.com/1.html"
      ],
      "resources":[
         "http://http://www.example.com/logo.png"
      ]
   },
   "http://www.example.com/1.html":{
      "redirect":"http://www.example.com",
      "resources":[

      ]
   }
}
```