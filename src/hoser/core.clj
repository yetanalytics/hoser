(ns hoser.core
  (:require [hoser.xapi.client :as c]
            [hoser.xapi.statement :as s]
            [hoser.xapi.statement.github :as gh]
            [hoser.data.reader.github :as ghr]
            [hoser.data.reader.twitter :as r]
            [clojure.core.async :as async :refer [<! >! go-loop close! timeout chan to-chan alt! alts! go pipeline-blocking onto-chan]]))


(defn post-tweet-stmnts
  "post (total) tweet statements sync, batched by (batch-size)"
  [tweets-path total batch-size]
  (let [data (r/lazy-tweets tweets-path)]
    (doseq [d (->> data
                (partition-all batch-size)
                (take (quot total batch-size))
                (map s/tweets->stmts))]
      (c/sync-send-stmt d))))


(defn post-tweet-stmnts-hose
  "post (total) tweet statements async, max (hose-size) concurrent reqs"
  [tweets-path total hose-width batch-size]
  (let [data-chan (chan 100 (comp
                             (take total)
                             (partition-all batch-size)
                             (map s/tweets->stmts)))
        done-chan (onto-chan data-chan (r/lazy-tweets tweets-path))
        result-chan (chan)
        results (atom [])
        ]

    (go-loop [remaining (quot total batch-size)]
      (if (> remaining 0)
        (do (swap! results conj (<! result-chan))
            (recur (dec remaining)))
        (do (close! data-chan)
            (close! result-chan)
            (println (str (count (filter #(= 200 %) @results)) " successful posts"))
            (when-let [errors (seq (filter #(not= 200 %) @results))]
              (println (str "errors: " errors))))))

    (pipeline-blocking hose-width result-chan (map c/sync-send-stmt) data-chan)))

(defn send-github-issues []
  (let [s-data (drop 1 ; drop the headers
                     (ghr/get-issue-data "resources/github/issues/results-20150212-125746.csv"))]
    (c/sync-send-stmt (gh/issues->statements s-data))))
