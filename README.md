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
you can find below or write one by yourself :) 

Or use this short exemplary configuration: `config.yaml`
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
* [Actions](#Actions)
  * [MockResponse](#MockResponse)
    * [StatusCodeProvider](#StatusCodeProvider)
    * [HeadersProvider](#HeadersProvider)
    * [BodyProvider](#BodyProvider)
  * [Proxy](#Proxy)
    * [ToUrlProvider](#ToUrlProvider)
  * [ConditionalAction](#ConditionalAction)
* [Conditions](#Conditions)


<a href="Actions"></a>
### Actions

<a href="MockResponse"></a>
#### MockResponse

<a href="StatusCodeProvider"></a>
##### StatusCodeProvider

<a href="HeadersProvider"></a>
##### HeadersProvider

<a href="BodyProvider"></a>
##### BodyProvider


<a href="Proxy"></a>
#### Proxy

<a href="ToUrlProvider"></a>
##### ToUrlProvider


<a href="ConditionalAction"></a>
#### ConditionalAction
<a href="Conditions"></a>
### Conditions