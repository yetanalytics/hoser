(ns hoser.data.reader.twitter
  (:require [hoser.data.reader :refer [json-seq]]
            [clojure.java.io :as io]
            [cheshire.core :as c]
            [hoser.util :refer [bz2-reader]]))


;; compressed bz2 stuff

(defn lazy-file-readers [root-path]
  (let [root-dir (clojure.java.io/file root-path)]
    (->> (file-seq root-dir)
         (filter #(and (not (.isDirectory %))
                       (not (.isHidden %))))
         (pmap bz2-reader))))

(defn lazy-tweets-compressed [root-path]
  (filter :text ;; only get tweets, only tweets have text
          (json-seq (lazy-file-readers root-path))))


(defn lazy-tweets [path]
  (filter :text (c/parsed-seq (clojure.java.io/reader path) true)))
