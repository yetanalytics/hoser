(ns hoser.util
  (:require [clojure.java.io])
  (:import (org.apache.commons.compress.compressors.bzip2 BZip2CompressorInputStream)))

(defn load-props
  "Load a Java properties file"
  [file-name]
  (with-open [^java.io.Reader reader (clojure.java.io/reader file-name)]
    (let [props (java.util.Properties.)]
      (.load props reader)
      (into {} (for [[k v] props] [(keyword k) (read-string v)])))))

(defn bz2-reader [file]
  (-> file
      clojure.java.io/input-stream
      BZip2CompressorInputStream.
      clojure.java.io/reader))
