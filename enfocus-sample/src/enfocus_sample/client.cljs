(ns enfocus-sample.client
  (:require
   [enfocus.core :as ef :refer (set-attr from at get-prop do-> after
                                remove-node content substitute)]
   [enfocus.events :as events :refer (listen)]
   [shoreleave.remotes.http-rpc :as rpc])
  (:use-macros
   [enfocus.macros :only [deftemplate defsnippet defaction]])
  (:require-macros
   [shoreleave.remotes.macros :as macros]))

(declare search show-info nav-search)

;;;;;;;;;;;;;;;;;;;
;; helper functions
;;;;;;;;;;;;;;;;;;;

(defn gravatar-url [id]
  (str "http://www.gravatar.com/avatar/" id))

;;;;;;;;;;;;;;;
;; templates
;;;;;;;;;;;;;;;

(defsnippet index-page :compiled "public/prototype/index.html" ["body"]
  []
  "#home-btn a" (set-attr :href "/")
  "#user-form"  (listen :submit
                        #(do (.preventDefault %)
                             (nav-search (from "#yourname"
                                               (get-prop :value))))))

(defsnippet search-page :compiled "public/prototype/search_page.html"
  ["#stage"]
  [name]
  "#name"       (content name)
  ".entry"      (remove-node)
  "#search-btn" (listen :click
                        #(search (from "#search-term"
                                       (get-prop :value)))))

(defsnippet base-entry :compiled "public/prototype/search_page.html"
  ["#entry1"]
  [info]
  "img"        (set-attr :src (gravatar-url (:gravatar_id info)))
  ".logid"     (content (str (:username info) "_btn"))
  ".more-info" (do->
                (set-attr :id (str (:username info) "_btn"))
                (listen :click #(do
                                  (.log js/console %)
                                  (show-info (:username info)
                                               (.-currentTarget %))))))

(defsnippet add-info :compiled "public/prototype/search_page.html"
  [".more-info-table"]
  [info]
  ".name"       (content (:name info))
  ".repo-count" (content (str (:public_repos info)))
  ".followers"  (content (str (:followers info))))

;;;;;;;;;;;;;;;;
;; Actions
;;;;;;;;;;;;;;;;
(defaction nav-search [name] 
  "#stage" (content (search-page name)))

(defaction display-results [entries]
  "#results" (content (map base-entry entries)))

(defn display-info [user btn]
  (at btn (substitute (add-info user))))

(defn search [term]
  (rpc/remote-callback :search-users [term]  display-results))

(defn show-info [id btn]
  (rpc/remote-callback :user-info [id] #(display-info % btn)))

(defaction init []
  "body" (content (index-page)))


(set! (.-onload js/window) init)

