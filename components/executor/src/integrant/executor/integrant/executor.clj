(ns integrant.executor.integrant.executor
  (:require
    [integrant.core :as ig]
    [integrant.workload.interface :as workload]))


;; Это мог быть jetty-server или kafka-consumer

(defmethod ig/init-key :integrant.executor.integrant/executor
  [_ {:keys [workload-record]}]
  (workload/write-text workload-record "WOW!"))

(defmethod ig/halt-key! :integrant.executor.integrant/executor
  [_ _]
  nil)
