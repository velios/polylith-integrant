(ns integrant.workload.interface
  (:require
    [integrant.workload.protocol :as protocol]))

;; пока не решил это нормально вообще или нет
(defn write-text
  [this text]
  (protocol/write-text this text))
