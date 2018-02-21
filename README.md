# Flower

A small Clojure library to experiment with the idea of avoiding conditional syntax in application logic by encoding program flow as data.

Think of Flower as bringing Lisp's "code is data" to the Anti-If campaign:

https://francescocirillo.com/pages/anti-if-campaign

## Quickstart

Add to project.clj:

```
[flower "0.1.0"]
```

Or deps.edn:

```
flower          {:mvn/version "0.1.0" }
```


Paste into a repl:

```
(require '[flower :refer [defflow]])

(defflow simplest {:start identity identity :end})

(= {} (simplest {}))

```

It should return
```
true
```


Run tests:

```
$ cd flower
$ clj -Atest

Running tests in #{"test"}

Testing flower.test.core

Testing flower.test.multimethods

Testing flower.test.perf

Testing flower.test.protocols

Testing flower.test.validation

Testing flower.test.webmachine

Ran 6 tests containing 9 assertions.
0 failures, 0 errors.

```

## Example
 
Pretend you are writing a web service that receives some data from the client, performs some operation on that data, and returns results to the client. This service performs validation to ensure it only runs the operation on data it considers to be valid. This service has some error handling such that failures in processing the data can be returned to the client. 
 
You create a series of functions to perform this work:
 
 * is-valid-request?
 * process-request
 * processing-successful?
 * return-invalid
 * return-success
 * return-failure

You could string these functions together in the body of a web service handler:

```
(let [data data]
  (if (is-valid-request? data)
    (if-let [results (process-request data)]
      (if (processing-successful? results)
        (return-success results)
        (return-failure results))
      (return-failure results))
    (return-invalid data)))
```      

Flower encodes this flow as data, and assigns it to a var:

```
(defflow basic-web-handler
  {:start is-valid-request?
   is-valid-request? { true process-request false return-invalid }
   process-request processing-successful?
   processing-successful? { true return-success false return-error }
   return-invalid :end
   return-success :end
   return-error :end }  )

```  

Each symbol is a function taking a map and returning either a map or a boolean. 

Functions that take and return maps are called *Actions*. 

Functions that take a map and return a boolean are called *Decisions*.

The var ```basic-web-handler``` is a function of a single parameter- a map- and returns a single value- a map- with the results of processing. 

Run the full example:

```
(require '[flower :refer [defflow]])

(defn is-valid-request?
  [context]
  true)

(defn process-request
  [context]
  (assoc context :response {:status 200 :body "Hello World!"} ))

(defn processing-successful?
  [context]
  true)

(defn return-invalid
  [context]
  (assoc context :response {:status 400 :body "Invalid!"} ))

(defn return-error
  [context]
  (assoc context :response {:status 500 :body "Error!"} ))

(defn return-success
  [context]
  (assoc context :response {:status 200 :body "Success!"} ))

(defflow basic-web-response
  {:start is-valid-request?
   is-valid-request? { true process-request false return-invalid }
   process-request processing-successful?
   processing-successful? { true return-success false return-error }
   return-invalid :end
   return-success :end
   return-error :end }  )

(basic-web-response {:request {} :response {}})

```

You should see

```
{:response {:status 200, :body "Success!"}}
```

## Motivation

Flower grew out in the loam of a deceased project to build an on-prem workflow engine, modeled on Amazon's Simple Workflow Service. SWS models workflows as graphs containing action nodes and decision nodes. 

While the project died, some work with this pattern in application code was surprisingly successful. 

Flows are more laborious early on to write, but logic encoded in this fashion was (anecdotally) simpler to test, quicker to fix, and easier to return to months later. 

## Anti-IF

In object-oriented code, the use of if to do what amounts to type switching can be considered a smell, ideally replaced with polymorphism:   

http://michaelfeathers.typepad.com/michael_feathers_blog/2013/11/unconditional-programming.html

But of course not all conditionals can be massaged this way:

https://code.joejag.com/2016/anti-if-the-missing-patterns.html

Clojure encourages various flavors of data-oriented programming. Flower is another one.

## Maturity

This is a toy implementation. It is rudimentary, lacks any optimizations, and is inappropriate for many projects. 

## License

Copyright 2018 Jonah Benton

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
