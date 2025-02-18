# java-compiler-api
- springboot based java compiler API


## use docker image
```
docker pull 0bliviate/java-compiler-api:main
```

## example

### request
- http://localhost:8080/api/compile
- post
```
{
    "code": "public class Main { public static void main(String[] args) {  System.out.println(12); } }"
}
```

### response
```
{
    "status": "SUCCESS",
    "compilePath": "tmp/tmp_5ebf34cd-624a-47e8-aeb0-18a477aff36f",
    "message": "12\n",
    "code": "public class Main { public static void main(String[] args) {  System.out.println(12); } }",
    "maxMemory": null,
    "maxTime": null
}
```

### api params
```
    code: your source code
    maxMemory: set memory
    maxTime: set max time
    inputFile: set input (if you use inputStream)
```

## contact
If you have any questions, please raise an issue or contact me at sjo9810@naver.com
