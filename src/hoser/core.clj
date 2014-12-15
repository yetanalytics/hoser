(ns hoser.core
  (:require [hoser.xapi.client :as c]
            [hoser.xapi.statement :as s]
            [hoser.data.reader :as r]))


(defn post-tweet-stmnts [tweets-path total batch-size]
  (let [data (r/lazy-tweets tweets-path)]
    (doseq [d (->> data
                (partition-all batch-size)
                (take (quot total batch-size))
                (map s/tweets->stmts))]
      (c/sync-send-stmt d))))
