(ns flower
  (:require
   [clojure.core.async :as a]
   [clojure.pprint :as p]
   [validateur.validation :as v]
   [differ.core :as differ]
   [flower.impl :as impl]
   ))

(defn custom-validator
  ""
  [c e]
  (fn [s]
    (try
      (let [r (c s)]
        (if r
          [true {}]
          [false e]))
      (catch Exception ex
        [false e]))))

(def flow-validation
  (v/validation-set
   ;; confirm map
   (custom-validator
    #(map? %)
    { :must-be-map "Flow map has to be a map" })

   ;; missing :start in keys
   (custom-validator
    #(:start %)
    { :missing-start "Flow map must have a :start key" })

   ;; missing :end from vals
   (custom-validator
    (fn [flow-map] (filter #(= :end %) (vals flow-map)))
    { :missing-end "Flow map must have an :end val" })
   
   ;; no :start in vals
   (custom-validator
    (fn [flow-map] (nil? (:start (set (filter keyword? (vals flow-map))))))
    { :no-start-in-vals "Flow map can't have :start in vals" })

    ;; no :end in keys
   (custom-validator
    #(nil? (:end %))
    { :no-end-in-keys "Flow map can't have :end in keys" })

    ;; no other keywords other than :start in keys or vals
   (custom-validator
    (fn [flow-map] (= #{ :start }
                      (set (filter keyword? (keys flow-map)))))
    {:no-other-keywords "No keyword keys other than :start :pre :post allowed" })
    
   (custom-validator
    (fn [flow-map] (= #{ :end } (set (filter keyword? (vals flow-map)))))
    {:no-other-keywords "No keyword vals other than :end allowed" })

    ;; every element that isn't a keyword or map should be a fn 
   
    ;; symbols don't chain, symbols not reached
    ;; maps in val need similar checks
   (custom-validator
    (fn [flow-map] (= (impl/key-nodes flow-map) (impl/val-nodes flow-map)))
    {:symbols-dont-chain "Symbols can't chain"})

   ;; symbols loop - skip for now
))      

(defn step
  "flow-fn is a flow-map passed through (flow-fn)
   context-key is either a map or vec.
   call step with the defflow var and the context map
   it runs the :start step of the flow
   it returns a tuple of context-map and symbol to execute from.
   call step with the defflow and the tuple and it runs the step
   of the flow specified by the symbol from the tuple."
  [flow-fn context-key]
  (cond
    (map? context-key)
      (let [[next-context next-key] (flow-fn context-key :start)]
        [next-context next-key])
    (vector? context-key)
      (let [[context key] context-key]
        (if (nil? key)
          [context nil]
          (let [[next-context next-key] (flow-fn context key)]
            [next-context next-key])))))

(defn follow
  "take a flow-fn and a [] of context and key
   executes a single step of the flow, prints the resulting context
   and returns control to the terminal
   hitting return continues the process until end
   entering any char + return ends the process"
  [flow-fn context-key]
  (loop [c-k context-key]
    (let [new-c-k (step flow-fn c-k)]
      (p/pprint new-c-k)
      (let [[c k] new-c-k
            response (read-line)]
        (if (not (nil? k))
          (if (= "" response)
            (recur new-c-k)
            nil)
          nil)))))

(defn trace
  "runs a complete flow-fn on a context with a trace-agent
   the trace-agent receives the before and after of every step
   when done pprints the diffs of the context on each step "
  [flow-fn start-context]
  (let [trace-agent  (agent [])
        end-context  (flow-fn start-context :start trace-agent)]
    (await trace-agent)
    (doseq [[before after] (partition 2 @trace-agent)]
      (p/pprint (differ/diff before after)))
    end-context))
    
(defn flow-fn
  "Returns a function of one parameter that will execute the provided flow map."
  [flow-map]
  (let [vv (flow-validation flow-map)]
    (when (not= {} vv)
      (throw (ex-info "Invalid flow map" { :flow-map flow-map :errors vv }))))
  (letfn [(f ([context]
              (loop [key      :start
                     context  context]
                (let [[next-context next-key]
                      (impl/one-step flow-map context key)]
                  (if  (= :end next-key)
                    next-context
                    (recur next-key next-context)))))
            
          ([context key]
           (let [[next-context next-key]
                 (impl/one-step flow-map context key)]
             [next-context next-key]))
          
          ;; for arity 3 with agent
          ;; do send-off conj [context key]
          ;; and send-off conj [next-context next-key]
          ;; so agent has a record of before and after
          ([context key trace-agent]
           (loop [context context
                  key key
                  trace-agent trace-agent]
             (send trace-agent conj context)
             (let [[next-context next-key]
                   (impl/one-step flow-map context key)]
               (send trace-agent conj next-context)
               (if (= :end next-key)
                 next-context
                 (recur next-context next-key trace-agent)))))
                     
          ;; for arity 4 with out-chan and in-chan
          ;; blocking push onto out-chan, go wait pull from in-chan
          ;; then do one step after pull from in-chan
          ;; then blocking push onto out-chan, go wait pull from in-chan
          ;; then return

          ) ]
       f))


(defn flow-fn-async
  "Returns a function of one parameter that returns a channel on which will be posted the flow result."
  [flow-map]
  (let [flow       (flow-fn flow-map)
        result-ch  (a/chan)]
     (fn [context]
       (a/go
         (let [result (flow context)]
           (a/>! result-ch result)))
       result-ch)))

(defmacro defflow
  "Creates a flow function to synchronously run the provided flow map, and assigns the function to var name."
  [name flow]
  `(def ~name (flow-fn ~flow)))

(defmacro defflow-async
  "Creates a flow function to asynchronously run the provided flow map. The flow function returns a channel on which the flow result will be provided. Assigns the async flow function to var name."
  [name flow]
  `(def ~name (flow-fn-async ~flow)))


