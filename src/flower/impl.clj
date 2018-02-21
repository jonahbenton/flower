(ns flower.impl)

;;
(defn try-fn
  "Run a single parameter function inside a try/catch handler.
   Returns [result ok?].
   On successful return, ok? is true and result is the function return value.
   On exception ok? is false and result is the exception."
  [f ctx]
  (try
    (let [s (f ctx)]
      [s true])
    (catch Exception e
      [e false])))

(defn key-nodes
  ""
  [flow-map]
  (set
    (->>
      (keys flow-map)
      (filter fn?))))

(defn val-nodes
  ""
  [flow-map]
  (set
    (concat
      (->>
        (vals flow-map)
        (filter fn?))
      (->>
        (vals flow-map)
        (filter map?)
        (mapcat vals)
        (filter fn?)))))

(defn one-step
  "takes one step in a flower process
   returns vector of the arguments for the next call
   :end in the first position means done"
  [flow-map context key]
  (let [val  (get flow-map key)]
    (cond
      (= :start key)
        [context val]

      (= :end key)
        [context nil]

      ;; handling for decision functions  
      (map? val)
        (let [[result ok?] (try-fn key context)]
          ;; confirm that result is a boolean?
          ;; confirm that result is one of the keys of val
          (if ok?
            [context (get val result)]
            [(assoc context :error result) :end]))
        
      ;; common case, an action function
      :else
        (let [[result ok?] (try-fn key context)]
          ;; confirm that result is a map and not nil      
          (if ok?
            [result val]
            [(assoc context :error result ) :end]))
        )))


