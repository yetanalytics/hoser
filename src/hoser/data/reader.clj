(ns hoser.data.reader
  (:require [clojure.java.io :as io]
            [cheshire.core :as c]
            [hoser.util :refer [bz2-reader]]))

(defn lazy-json [filename]
  (c/parsed-seq (io/reader filename) true))

(defn lazy-file-readers [root-path]
  (let [root-dir (clojure.java.io/file root-path)]
    (->> (file-seq root-dir)
         (filter #(and (not (.isDirectory %))
                       (not (.isHidden %))))
         (pmap bz2-reader))))

(defn json-seq [rs]
  (concat (c/parsed-seq (first rs) true)
          (lazy-seq (json-seq (rest rs)))))

(defn lazy-tweets [root-path]
  (json-seq (lazy-file-readers root-path)))
