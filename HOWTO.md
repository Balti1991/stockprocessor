# Building the project from SVN source #
## Requirements ##
  * java SDK
  * eclipse
    * svn plugin
    * maven plugin

## Check out ##

Using the svn util or eclipse svn-plugin check out the project from Google repositori:

### SVN command ###
Execute the check out command into your workspace:
```
svn checkout http://stockprocessor.googlecode.com/svn/trunk/stockprocessor stockprocessor
```

### Eclipse ###
  * Add new repository: **http://stockprocessor.googlecode.com/svn/**
  * Check out from **trunk** the _stockprocessor_
  * Delete the project from workspace to prepare the next step...

## Maven ##
  * from eclipse _File_ menu
    * _Import.._
  * from _Maven_ slect _Materialize Maven Projects_
    * _Next_
    * _Next_
    * _Finish_
  * in next dialog
    * select _Root Directory_: **stockprocessor**
    * _Finish_
...and wait :) eclipse will recreate the hierarchy of maven modules into the workspace and build it

# Run the Application #
## GUI ##
In the **stockprocessor-gui** project find the _stockprocessor.gui.MainWindow_ and run it as java application.