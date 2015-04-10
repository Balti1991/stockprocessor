# 2009.07.14. #
  * using outputs name in sources insted of instrument
  * some classes moved to new place
  * fixed the xml import:
    * removed the wires from input-outputs
    * the used processor get their alias names from xml (renameing them)
# 2009.07.14. #
  * big refact to maven compatibility:
    * project uses maven module structure
    * sources moved into maven specific /src/main/java location
    * eclipse project files are created by maven plugin - they are not commited into subversion
  * spring used for module wiring, useing the jars /config folder:
    * processors:
      * must have a _processor-{name}.xml_ with a bean definition
      * must implement _ProcessorManager_ interface for manager
    * sources:
      * must have a _source-{name}.xml_ with a bean definition
      * must implement _SourceManager_ interface for manager or
      * must implement _SourceHandler_ interface for handler
  * finished XML import for custom processors
# 2009.07.02. #
  * source & processor manager moved to manager
  * manager uses the InstanceHandler to get new object
# 2009.06.29. #
  * Ta-lib fixed, again works
  * some GUI fixes
  * TwoInputLogicalProcessor for logical processors with two parameters
  * StockData -> ShareData rename
# 2009.06.25. #
  * handler change finished
# 2009.06.23. #
  * decided: the broker objects are simply a special receiver or processor objects, like the others
  * handler: data handler object
    * receiver - data receiver
    * source - data source
    * (processor - data receiver and sender)