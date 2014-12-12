(ns hoser.core
  (:require [hoser.xapi.client :as c]
            [hoser.xapi.statement :as s]
            [hoser.data.reader :as r]))

(defn post-tweet-stmnts [tweets-path & [total batch-size]]
  (let [data (cond->> (r/lazy-tweets tweets-path)
               total (take total)
               batch-size (partition-all batch-size)
               batch-size (map s/tweets->stmts)
               )]
    (doseq [d data]
      (c/sync-send-stmt data))))
