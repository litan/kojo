### Kojo Links

* The [Kojo home-page][1] provides user-level information about Kojo.
* The [Kojo issue-tracker][2] let's you file bug reports.
* The [Kojo Localization file](localization.md) tells you how to translate Kojo to your language.
* The [Kojo AI repo](https://github.com/litan/kojo-ai-2) adds exciting AI capabilities (Neural Style Transfer and Object Detection via Deep Learning, Graphs, etc.) to Kojo.

### To start hacking:

* Make sure you have Java 8 on your path. 
* Run `./sbt.sh clean package` to build Kojo.
* Run `./sbt.sh test` to run the Kojo unit tests.
* Run `./sbt.sh run` to run Kojo (use `net.kogics.kojo.lite.DesktopMain` as the main class)
* As you modify the code, do incremental (and fast) auto-compilation and auto-testing using sbt:
```  
sbt
  > ~compile
  > ~test
```

### IDE setup

#### Intellij IDEA  
Do a `File -> New -> Project from Existing Sources` and import/open the root folder of the Kojo repo. Then import the new project via sbt.

#### Emacs

Put the following in your .emacs config file:
```  
;; cd ~/src; git clone https://github.com/jwiegley/use-package
(eval-when-compile
  (add-to-list 'load-path "~/src/use-package")
  (require 'use-package))
(require 'package)
(add-to-list 'package-archives '("melpa" . "https://melpa.org/packages/") t)
(package-initialize)
;; https://github.com/hvesalai/emacs-scala-mode
(use-package scala-mode
  :interpreter
    ("scala" . scala-mode))
(add-to-list 'auto-mode-alist '("\\.sc\\'" . scala-mode))
(add-to-list 'auto-mode-alist '("\\.kojo\\'" . scala-mode))
```

  [1]: http://www.kogics.net/kojo
  [2]: https://github.com/litan/kojo/issues
  
