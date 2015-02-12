(ns hoser.data.reader
  (:require [clojure.java.io :as io]
            [cheshire.core :as c]
            [hoser.util :refer [bz2-reader]]))

(defn lazy-json [filename]
  (c/parsed-seq (io/reader filename) true))

(defn json-seq [rs]
  (when-let [srs (seq rs)]
    (concat (c/parsed-seq (first srs) true)
            (lazy-seq (json-seq (rest srs))))))
