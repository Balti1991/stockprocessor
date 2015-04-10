StockProcessor is an universal stock data processor for automation of technical analyzation.
The main aim of project is an analyzer automat which can run continously or sheduled to generate transaction signals.

The input of processor is in universal form, with some default implmentations (tipically for hungaryan stock exchange) but it can be easy extended or customized for personal needs.
The output will be a simple GUI, an email notifier, or an external application.
The main processing library is customizable too. Defaultly it uses the TA-lib.
It has a simple stock data visualizer GUI.

You can follow the changes on wiki page [ChangeLog](ChangeLog.md). Or check out the source of the project and try it.

If you want to make your on version from repository you need to have the following utilities:
  * Eclipse IDE: for project handling from http://www.eclipse.org
  * Maven2: for dependency management - the main library and the eclipse plugin

If you have questions feel free to write it down to me or to post an issue if you found a bug or have a good idea.