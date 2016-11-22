(ns test-cljs-addin.core
  (:import [adsk.core]))

(def cleanupList (atom []))

(defn application-get []
  (.. js/adsk -core -Application get))

(defn test-command-created
  [args]
  (let [command (. args -command)
        inputs (. command -commandInputs)]
    (. inputs (addStringValueInput "test_string" "Test String" "Value"))))

(defn add-one-command
  [ui]
  (let [commandId "test"
        commandName "Test Cmd"
        commandDescription "Testing cljs->js addin"
        workspace (.. ui -workspaces (itemById "FusionSolidEnvironment"))
        panels (. workspace -toolbarPanels)
        panel (. panels (add "test_cmd" commandName "Select" false))
        controls (. panel -controls)
        cmddef (.. ui -commandDefinitions (addButtonDefinition commandId commandName commandDescription "resources/TestCmd"))
        ctrl (. controls (addCommand cmddef "" false))]
    (swap! cleanupList conj panel cmddef)
    (.. cmddef -commandCreated (add test-command-created))
    cmddef))

(defn remove-all-commands
  [ui]
  (doseq [c @cleanupList] (. c deleteMe))
  (reset! cleanupList []))

(defn ^:export run [context]
  (try
    (let [app (application-get)
          ui (.. app -userInterface)]
      (add-one-command ui)
      (.. ui (messageBox "Hello addin")))
  (catch :default e
    (js/alert (. e description)))))

(defn ^:export stop [context]
  (try
    (let [app (application-get)
          ui (.. app -userInterface)]
      (remove-all-commands ui)
      (.. ui (messageBox "Stop addin")))
  (catch :default e
    (js/alert (. e description)))))

; Export run and stop globally, so they can be found by the Fusion 360 add-in system
(set! js/run run)
(set! js/stop stop)


