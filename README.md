# Proxmock (alpha)

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

## Configuration 
* [Basics](#basics)
* [Endpoints](#endpoints)
* [Actions](#actions)
  * [MockResponse](#mockresponse)
    * [StatusCodeProvider](#statuscodeprovider)
      * [ConditionalStatusCodeProvider](#conditionalstatuscodeprovider)
      * [StaticStatusCodeProvider](#staticstatuscodeprovider)
      * [SuccessStatusCodeProvider](#successtatuscodeprovider)
    * [HeadersProvider](#headersprovider)
      * [ConditionalHeadersProvider](#conditionalheadersprovider)
      * [EmptyHeadersProvider](#emptyheadersprovider)
      * [StaticHeadersProvider](#staticheadersprovider)
    * [BodyProvider](#bodyprovider)
      * [ConditionalBodyProvider](#conditionalbodyprovider)
      * [EmptyBodyProvider](#emptybodyprovider)
      * [StaticBodyProvider](#staticbodyprovider)
  * [Proxy](#proxy)
    * [ToUrlProvider](#tourlprovider)
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

#### MockResponse
MockResponse action lets you define mock response by configuring 
`status code, headers and body` separately by variety of providers.

##### StatusCodeProvider
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

##### BodyProvider
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

#### Proxy
Proxy action lets you forward received request to another service.
The adderess to where request should be proxied is configured by 
the UrlProviders. 

Notice: urls `http://localhost/some/uri` will be extended with port 
specified in configuration file used when starting proxmock, so the final
url which will be used is `http://localhost:$port/some/uri`

##### UrlProvider
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