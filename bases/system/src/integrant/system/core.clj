(ns
  ^{:author "Mark Sto"}
  integrant.system.core
  (:require [clojure.pprint :as pp]
            [clojure.tools.logging :as log]
            [integrant.core :as ig]
            [integrant.system.state :as state]
            [integrant.workload.interface :as workload])
  (:gen-class) ;; без этого не захотело jar'ку собирать
  )

;; Integrant System

;; NB: For our system we follow the Integrant conventions on loading namespaces
;;     that contain the system components (a.k.a. keys in Integrant's parlance)
;;     named with qualified keywords that match their namespaces. For instance,
;;     a 'Config' component residing in the `integrant.system.config` namespace
;;     obtains the `:integrant.system/config` key in the system map.
;;
;;     This approach leverages the uniqueness of Polylith base names and allows
;;     us to use a particular Polylith component under classpath-unique keys as
;;     part of multiple Integrant systems/bases.
;;
;;     Another option is to use an arbitrary key, e.g. `:app/config`, though it
;;     may lead to naming collisions and overwriting of multi-methods' dispatch
;;     values in case if you plan to run multiple Integrant systems together in
;;     a single workspace/project.

(def default-ig-config
  {:integrant.system/config      {}
   :integrant.system/embedded-pg {:config (ig/ref :integrant.system/config)
                                  #_#_:log-file "path/to/pg-logs-redirection"}
   :integrant.system/data-source {:config   (ig/ref :integrant.system/config)
                                  :postgres (ig/ref :integrant.system/embedded-pg)}
   :integrant.workload.integrant/workload {:print-base "prod"}
   :integrant.executor.integrant/executor {:workload-record (ig/ref :integrant.workload.integrant/workload)}})

(def test-ig-config
  {:integrant.system/config      {}
   :integrant.system/embedded-pg {:config (ig/ref :integrant.system/config)
                                  #_#_:log-file "path/to/pg-logs-redirection"}
   :integrant.system/data-source {:config   (ig/ref :integrant.system/config)
                                  :postgres (ig/ref :integrant.system/embedded-pg)}
   :integrant.workload.integrant.test/workload {}
   :integrant.executor.integrant/executor {:workload-record (ig/ref :integrant.workload.integrant.test/workload)}})

(defn halt-system! []
  (state/stop!)
  ::stopped)

(defn init-system!
  [config-name]
  (let [ig-config (eval (symbol (str "integrant.system.core/" config-name)))]
    (log/info "Starting system with config:\n"
              (with-out-str (pp/pprint ig-config)))
    (try
      (halt-system!)
      (state/start! ig-config)
      ::started
      (catch Exception ex
        (log/error ex "Failed to start the system")
        (halt-system!)
        ::failed-to-start))))

;; Base API

;; NB: Properly shutting production systems down is full of its own shenanigans,
;;     so we don't do much here. For more information, see the article "Killing
;;     me softly: Graceful shutdowns in Clojure".

(defn shutdown!
  []
  (let [halt-res (halt-system!)]
    (shutdown-agents)
    halt-res))

(defn launch!
  [config-name]
  (.addShutdownHook (Runtime/getRuntime) (Thread. shutdown!))
  (init-system! config-name))

(defn -main
  [& [config-name]]
  (launch! (or config-name "default-ig-config")))
