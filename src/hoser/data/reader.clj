(ns hoser.data.reader
  (:require [clojure.java.io :as io]
            [cheshire.core :as c]))

(defn lazy-json [filename]
  (c/parsed-seq (io/reader filename) true))
