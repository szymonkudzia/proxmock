# Proxmock (BETA)

In short proxmock is: 
>Simple application for smart mocking services with request proxying capabilities

#### Features:
* mocking
* smart mocking - dynamic responses depending on requests
* conditional proxying - send request to real service 
if mock response is not what you want


#### Why?
Well sometimes external service you are depending on does not work the way you wish it to be :/

For example external service: 
* is half way done and not all of served endpoints work as expected or are missing. 
* is often not available (because external provider has lot of problems with keeping it alive)
* is not always what you want i.e: you are doing unexpected demo 

In such cases I believe you would want to have tool that will alow you 
to freely mock broken/missing endpoints and will not stop you from 
using working ones.

Usual usage scenarion looks like this:
```
your app <--> proxmock <--> extrenal service
``` 
Your app sends all request through proxmock to the external service but
depending on some conditions certain request are proxied to external service
and others are handled by proxmock, by responding with mocked response
or by proxying them to andother service 
(i.e: service running DEV profile instead of PROD)

#### Example:
To run proxmock you need two things `proxmock.jar` and configuration file.
First one you can find on this repo in `distribution` catalog, second one 
you can write by yourself or use the one listed below. 

Exemplary configuration `config.yaml`:
```yaml 
name: Test
port: 9999
endpoints:
- path: /external/not/cool/service
  method: GET
  action:
    conditional:
      condition:
        headerMatches:
          header: correlation-id
          pattern: ^MOCK-.*$
      ifTrue:
        mockResponse:
          headers:
            static:
              X-SESSION-ID: 9843478794734u8jwf893
          body:
            static: mocked body content :)
      ifFalse:
        proxy:
          toUrl:
            static: http://www.google.com
```

To run proxmock with this configuration simply type in terminal this line:
```sh
java -jar proxmock.jar run config.yaml
```

After executing above command proxmock will run and start listening under address
`http://localhost:9999/external/not/cool/service` for GET requests.

After receiving request with header `correlation-id` which value starts with `MOCK-`
proxmock will respond with mocked response (one custome header `X-SESSION-ID` and body set to text: `mocked body content :)`)

Otherwise if request does not have header `correlation-id` or value does not start with `MOCK-` then it
will be proxied to address `http://www.google.com`

## Documentation
* [Command line interface](#command-line-interface) 
* [Basics](#basics)
* [Endpoints](#endpoints)
* [Actions](#actions)
  * [FirstMetConditionAction](#firstmetconditionaction)
  * [GroovyExpressionAction](#groovyexpressionaction)
  * [GroovyExpressionFileAction](#groovyexpressionfileaction)
  * [MockResponseAction](#mockresponseaction)
    * [StatusCodeProvider](#statuscodeprovider)
      * [GroovyExpressionStatusCodeProvider](#groovyexpressionstatuscodeprovider)
      * [GroovyExpressionFromFileStatusCodeProvider](#groovyexpressionfromfilestatuscodeprovider)
      * [ConditionalStatusCodeProvider](#conditionalstatuscodeprovider)
      * [StaticStatusCodeProvider](#staticstatuscodeprovider)
      * [StaticFromFileStatusCodeProvider](#staticfromfilestatuscodeprovider)
      * [SuccessStatusCodeProvider](#successtatuscodeprovider)
    * [HeadersProvider](#headersprovider)
      * [GroovyExpressionHeadersProvider](#groovyexpressionheadersprovider)
      * [GroovyExpressionFromFileHeadersProvider](#groovyexpressionfromfileheadersprovider)
      * [ConditionalHeadersProvider](#conditionalheadersprovider)
      * [EmptyHeadersProvider](#emptyheadersprovider)
      * [StaticHeadersProvider](#staticheadersprovider)
      * [StaticFromFileHeadersProvider](#staticfromfileheadersprovider)
    * [BodyProvider](#bodyprovider)
      * [GroovyExpressionBodyProvider](#groovyexpressionbodysprovider)
      * [GroovyExpressionFromFileBodyProvider](#groovyexpressionfromfilebodysprovider)
      * [ConditionalBodyProvider](#conditionalbodyprovider)
      * [EmptyBodyProvider](#emptybodyprovider)
      * [StaticBodyProvider](#staticbodyprovider)
      * [StaticFromFileBodyProvider](#staticfromfilebodyprovider)
  * [ProxyAction](#proxyaction)
    * [UrlProvider](#urlprovider)
      * [GroovyExpressionUrlProvider](#groovyexpressionurlprovider)
      * [GroovyExpressionFromFileUrlProvider](#groovyexpressionfromfileurlprovider)
      * [StaticUrlProvider](#staticurlprovider)
      * [StaticFromFileUrlProvider](#staticfromfileurlprovider)
  * [ConditionalAction](#conditionalaction)
* [Conditions](#conditions)
  * [Always true](#always-true)
  * [Always false](#always-false)
  * [Body matches](#body-matches)
  * [Groovy expression](#groovy-expression)
  * [Groovy expression file](#groovy-expression-file)
  * [Header matches](#header-matches)
  * [Random](#random)
  * [Uri matches](#uri-matches)
* [Miscellaneous](#miscellaneous)

### Command line interface
<pre>
list of available commands:
  list          - list all running instances
  stop n        - stop instance with name: n
  help          - displays this information
  run filepath  - runs proxymock in background with configuration fetched from file under filepath
                  This command accepts also additional parameters (--name) used by spring boot which
                  can be used to change proxmock behaviour. i.e.:
                   proxmock run /file.yaml --server.port=9090
                  See spring boot documentation for more information about available properties
</pre>
*examples*

```sh 
java -jar proxmock.jar list
```
above command will list all running instances


```sh
java -jar proxmoc.jar stop baka
```
above command will stop instance with name "baka"

### Basics
Proxmock configuration file uses `yaml` syntax and supports only those
features which are supported by `jackson yaml dataformat`

There are three properties requeired to be specified in configuration file:
* name
* port
* endpoints

`name` is used in command line interface for listing and shutting down of 
running proxmock instances 
\
`port` specify under which port proxmock http server will listen.
\
`endpoints` list of at least one endpoint definition 

*exempla*
```yaml
name: TestInstance
port: 9999
endpoints:
- path: /test/path
  action:
    proxy:
      toUrl:
        static: http://test.server.com/test/path
```

### Endpoints
Proxmock alows you to define multiple endpoints. To define single endpoint
you have to provide three information
* path - path under which proxmock will wait for requests 
(syntax is same as in springs @RequestMapping)
* method - set http method type (if omittet then endpoint will accept all methods types)
* action - definition of how request should be handled

*example*
```yaml
...
endpoints:
- path: /test/path
  method: DELETE
  action:
    mockResponse:
      body:
        static: |
          {
            "content": "this is a mocked json content"
          }
      headers:
        static:
          session-id: dfw232nf23rfn23
...
```

### Actions
Actions define the way how proxmock should handle requests.

#### FirstMetConditionAction
Extension to ConditionalAction where you can define more than one condition.
As the name sugests the action of the first met condition will be used
to provide response.
If non of condition will be met then error will be returned.

*example*
```yaml
...
- path: /test/path
  method: GET
  action:
    firstMetCondition:
    - condition:
        uriMatches: .*FIRST_CASE.*
      action:
        mockResponse:
          body:
            static: FIRST_CASE
    - condition:
        uriMatches: .*SECOND_CASE.*
      action:
        mockResponse:
          body:
            static: SECOND_CASE
    - condition:
        alwaysTrue: {}
      action:
        mockResponse:
          body:
            static: DEFAULT
...
```

#### GroovyExpressionAction
When simple mock response is not what you want then you can use 
this action to prepare some dynamic response.
In groovy script you have full control on how the response will
be constructed.

To create response you can:
- use the 
`org.springframework.integration.support.MessageBuilder` builder (which
is by default imported in your scripts)
- implement interface
`org.springframework.messaging.Message` by yourself (which
is by default imported in your scripts)
- use any other method for constructing spring integration message object

*example*
```yaml
...
- path: /test/path
  method: GET
  action:
    groovyExpression: |
      MessageBuilder
        .withPayload('custom body')
        .setHeader('some_header', 'some header value')
        .build()
```

#### GroovyExpressionFromFileAction
Does everything the same way as GroovyExpressionAction with the difference
that groovy script is loaded from file.
Path to the file can be absolute or relative to configuration file passed
as argument when starting proxmock.
File during proxmock runtime can be freely modified and changes
will take effect immediatelly. 

*example*
```yaml
...
- path: /test/path
  method: GET
  action:
    groovyExpressionFromFile: ../scripts/test.groovy
...
```

#### MockResponseAction
MockResponse action lets you define mock response by configuring 
`status code, headers and body` separately by variety of providers.

##### StatusCodeProvider
##### GroovyExpressionStatusCodeProvider
If you want to have more control on determining status code of the response
you can use this provider. From withing your script you can access the 
message object (of type `org.springframework.messaging.Message`) 
which holds all request information (like headers and payload).
From your script you should return single integer value otherwise exception 
will be raised.

*example*
```yaml
...
- path: /test/path
  method: GET
  action:
    mockResponse:
      statusCode:
        groovyExpression: 10 + 500 - 210
...
```

##### GroovyExpressionFromFileStatusCodeProvider
Does everything the same way as GroovyExpressionFromFileStatusCodeProvider
with the difference that groovy script is loaded from file. Path to the
file can be absolute or relative to configuration file passed as 
argument when starting proxmock.
File during proxmock runtime can be freely modified and changes
will take effect immediatelly. 

*example*
```yaml
...
- path: /test/path
  method: GET
  action:
    mockResponse:
      statusCode:
        groovyExpressionFromFile: ../scripts/test.groovy
...
```


##### ConditionalStatusCodeProvider
This provider depending on condition will return status code
given by provider specified in `ifTrue` or `ifFalse` 
properties.

*example*
```yaml
...
mockResponse:
  statusCode:
    conditional:
      condition:
        random: {}
      ifTrue:
        static: 300
      ifFalse:
        static: 400
...
```

##### StaticStatusCodeProvider
Always returns specified status code.

*example*
```yaml
...
mockResponse:
  statusCode:
    static: 404
...
```

##### StaticFromFileStatusCodeProvider
Does everything the same way as StaticStatusCodeProvider with the difference
that status code is loaded from file. Path to file can be absolute or
relative to configuration file passed as argument when starting proxmock.
File should contains single integer value.
File during proxmock runtime can be freely modified and changes
will take effect immediatelly. 

*example*
```yaml
...
- path: /test/path
  method: GET
  action:
    mockResponse:
      statusCode:
        staticFromFile: ../mock/test.txt
...
```

##### SuccessStatusCodeProvider
Always returns 200 status code. (default status code provider)

*example*
```yaml
...
mockResponse:
  statusCode:
    success: {}
...
```

##### HeadersProvider
##### GroovyExpressionHeadersProvider
If you want to have more control on determining headers of the response
you can use this provider. From withing your script you can access the 
message object (of type `org.springframework.messaging.Message`) 
which holds all request information (like headers and payload).
From your script you should return `Map[String, String]` otherwise 
exception will be raised.

*example*
```yaml
...
- path: /test/path
  method: GET
  action:
    mockResponse:
      headers:
        groovyExpression: "[header_1: 'value_1', header_2: 'value_2']"
...
```

##### GroovyExpressionFromFileHeadersProvider
Does everything the same way as GroovyExpressionFromFileHeadersProvider
with the difference that groovy script is loaded from file. Path to the
file can be absolute or relative to configuration file passed as 
argument when starting proxmock.
File during proxmock runtime can be freely modified and changes
will take effect immediatelly. 

*example*
```yaml
...
- path: /test/path
  method: GET
  action:
    mockResponse:
      headers:
        groovyExpressionFromFile: ../scripts/test.groovy
...
```

##### ConditionalHeadersProvider
This provider depending on condition will return headers
given by provider specified in `ifTrue` or `ifFalse` 
properties.

*example*
```yaml
...
mockResponse:
  headers:
    conditional:
      condition:
        random: {}
      ifTrue:
        static: 
          session-id: h3l4h23j1l2nk 
      ifFalse:
        static:
          session-id: unknown
...
```

##### EmptyHeadersProvider
Always return empty headers map. (default header provider)

*example*
```yaml
...
mockResponse:
  headers:
    empty: {}
...
```

##### StaticHeadersProvider
Always returns specified headers.

*example*
```yaml
...
mockResponse:
  headers:
    static:
      session-id: n2wnh34h2
      corr-id: w3n2nr23o9n4
...
```

##### StaticFromFileHeadersProvider
Does everything the same way as StaticHeadersProvider with the difference
that headers is loaded from file. Path to file can be absolute or
relative to configuration file passed as argument when starting proxmock.
File is treated as `yaml` file and should contain a map.
File during proxmock runtime can be freely modified and changes
will take effect immediatelly. 

*example*
```yaml
...
- path: /test/path
  method: GET
  action:
    mockResponse:
      headers:
        staticFromFile: ../mock/test.yaml
...
```

*test.yaml*
```yaml
header_1: header 1 value
header_2: header 2 value
```

##### BodyProvider
##### GroovyExpressionBodyProvider
If you want to have more control on determining body of the response
you can use this provider. From withing your script you can access the 
message object (of type `org.springframework.messaging.Message`) 
which holds all request information (like body and payload).
From your script you should return string but also you can return any
non null object in that case object's toString will be used to 
determine body value.

*example*
```yaml
...
- path: /test/path
  method: GET
  action:
    mockResponse:
      body:
        groovyExpression: message.headers
...
```

##### GroovyExpressionFromFileBodyProvider
Does everything the same way as GroovyExpressionFromFileBodyProvider
with the difference that groovy script is loaded from file. Path to the
file can be absolute or relative to configuration file passed as 
argument when starting proxmock.
File during proxmock runtime can be freely modified and changes
will take effect immediatelly. 

*example*
```yaml
...
- path: /test/path
  method: GET
  action:
    mockResponse:
      body:
        groovyExpressionFromFile: ../scripts/test.groovy
...
```

##### ConditionalBodyProvider
This provider depending on condition will return body
given by provider specified in `ifTrue` or `ifFalse` 
properties.

*example*
```yaml
...
mockResponse:
  body:
    conditional:
      condition:
        random: {}
      ifTrue:
        static: success
      ifFalse:
        static: failure
...
```

##### EmptyBodyProvider
Always return empty body. (default body provider)

*example*
```yaml
...
mockResponse:
  body:
    empty: {}
...
```

##### StaticBodyProvider
Always returns specified body content.

*example*
```yaml
...
mockResponse:
  body:
    static: static body content
...
```

##### StaticFromFileBodyProvider
Does everything the same way as StaticBodyProvider with the difference
that body is loaded from file. Path to file can be absolute or
relative to configuration file passed as argument when starting proxmock.
File during proxmock runtime can be freely modified and changes
will take effect immediatelly. 

*example*
```yaml
...
- path: /test/path
  method: GET
  action:
    mockResponse:
      body:
        staticFromFile: ../mock/test.yaml
...
```

#### ProxyAction
Proxy action lets you forward received request to another service.
The adderess to where request should be proxied is configured by 
the UrlProviders. 

Notice: urls `http://localhost/some/uri` will be extended with port 
specified in configuration file used when starting proxmock, so the final
url which will be used is `http://localhost:$port/some/uri`

##### UrlProvider
##### GroovyExpressionUrlProvider
If you want to have more control on determining url to which request 
shoud be proxied you can use this provider. 
From withing your script you can access the 
message object (of type `org.springframework.messaging.Message`) 
which holds all request information (like body and payload).
From your script you should return string holding url value.

*example*
```yaml
...
- path: /test/path
  action:
    proxy:
      toUrl:
        groovyExpression: "return 'http://localhost/proxy/to/mock/response'"
...
```

##### GroovyExpressionFromFileUrlProvider
Does everything the same way as GroovyExpressionFromFileUrlProvider
with the difference that groovy script is loaded from file. Path to the
file can be absolute or relative to configuration file passed as 
argument when starting proxmock.
File during proxmock runtime can be freely modified and changes
will take effect immediatelly. 

*example*
```yaml
...
- path: /test/path
  action:
    proxy:
      toUrl:
        groovyExpressionFromFile: ../scripts/test.groovy
...
```

##### StaticUrlProvider
Always returns specified url. 

*example*
```yaml
...
proxy:
  toUrl:
    static: http://www.google.pl
...
```

##### StaticFromFileUrlProvider
Does everything the same way as StaticUrlProvider with the difference
that url is loaded from file. Path to file can be absolute or
relative to configuration file passed as argument when starting proxmock.
File during proxmock runtime can be freely modified and changes
will take effect immediatelly. 

*example*
```yaml
...
- path: /test/path
  action:
    proxy:
      toUrl:
        staticFromFile: ../mock/test.txt
...
```

#### ConditionalAction
Conditional action alows you to handle request in two different
ways depending on condition.

*example*
```yaml
name: Test
port: 9999
endpoints:
- path: /test
  action:
    conditional:
      condition:
        random: {}
      ifTrue:
        mockResponse:
          body:
            static: "mock response"
      ifFalse:
        proxy:
          toUrl:
            static: http://www.some.service.com/test
```

### Conditions
Common conditions to be used in conditional actions and conditional providers.

#### Always true
Condition that alwasy evaluates to true.

*example*
```yaml
...
conditional:
  condition:
    alwaysTrue: {}
...
```

#### Always false
Condition that alwasy evaluates to false.

*example*
```yaml
...
conditional:
  condition:
    alwaysFalse: {}
...
```

#### Body matches
Evaluates to true only when body matches specified pattern. 
>Pattern syntax is same as the one used in Java.
\
>In case of missing body, condition evaluates to false.

*example*
```yaml
...
conditional:
  condition:
    bodyMatches: .*CONTAINS THIS STRING.*
...
```

#### Groovy expression
Condition given by groovy expression. It will evaluate to true if 
groovy expression returns boolean true or non null object otherwise 
(returned value is boolean false, string 'false', null) evaluates to false.

Variable `message` (of type `org.springframework.messaging.Message[Object]`) 
is available for you in your expression. 

Accessing header map looks as follows: `message.headers`
\
Accessing payload looks as follows: `message.payload`

*example*
```yaml
...
conditional:
  condition:
    groovyExpression: message.headers.containsKey('expected_header') 
...
```

#### Groovy expression file
Condition that behaves exactly the same way as `Groovy expression` but 
expression is loaded from file.
Path to the file can be absolute or relative to the configuration
file passed when starting proxmock.
File during proxmock runtime can be freely modified and changes
will take effect immediatelly. 

*example*
```yaml
...
conditional:
  condition:
    groovyExpressionFile: /etc/configs/expression.groovy
...
```

#### Header matches
Evaluates to true when header value matches given pattern.
>Pattern syntax is same as the one used in Java.
\
>In case of missing header, condition evaluates to false.

*example*
```yaml
...
conditional:
  condition:
    headerMatches:
      header: header_name
      pattern: .*CONTAINS THIS STRING.*
...
```

#### Random
Evaluates to true randomly .

*example*
```yaml
...
conditional:
  condition:
    random: {}
...
```

#### Uri matches
Evaluates to true when request uri matches specified pattern.
>Pattern syntax is same as the one used in Java.

*example*
```yaml
...
conditional:
  condition:
    uriMatches: .*/expected/uri.*
...
```

### Miscellaneous
Proxmock is a spring boot application so it behaves as typical spring boot
application. For example you can pass some spring properties to change
default behaviour of application:
- like override server port by passing property `--server.port` 
(which takes precedence before port specified in configuration file) 
- specify log level for classes 
