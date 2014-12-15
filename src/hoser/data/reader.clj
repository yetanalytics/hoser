(ns hoser.data.reader
  (:require [clojure.java.io :as io]
            [cheshire.core :as c]
            [hoser.util :refer [bz2-reader]]))

(defn lazy-json [filename]
  (c/parsed-seq (io/reader filename) true))



;; compressed stuff

(defn lazy-file-readers [root-path]
  (let [root-dir (clojure.java.io/file root-path)]
    (->> (file-seq root-dir)
         (filter #(and (not (.isDirectory %))
                       (not (.isHidden %))))
         (pmap bz2-reader))))

(defn json-seq [rs]
  (when-let [srs (seq rs)]
    (concat (c/parsed-seq (first srs) true)
          (lazy-seq (json-seq (rest srs))))))

(defn lazy-tweets-compressed [root-path]
  (filter :text ;; only get tweets, only tweets have text
          (json-seq (lazy-file-readers root-path))))


(defn lazy-tweets [path]
  (filter :text (c/parsed-seq (clojure.java.io/reader path) true)))
