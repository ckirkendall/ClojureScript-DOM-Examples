(ns enfocus-sample.server
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer (resource-response)]
            [shoreleave.middleware.rpc :as rpc :refer (defremote)]
            [tentacles.search :as search]
            [tentacles.users :as users]))

;;;;;;;;;;;;;;;;;
;; remote rpc
;;;;;;;;;;;;;;;;;

(defremote search-users [search-term]
  (search/search-users search-term))

(defremote user-info [id]
  (users/user id))

;;;;;;;;;;;;;;;;;;;
;; routes
;;;;;;;;;;;;;;;;;;;

(defroutes app-routes
  (GET "/" [] (resource-response "blank.html" {:root "public"}))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      rpc/wrap-rpc
      handler/site))

  
