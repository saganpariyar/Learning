package org.excel.readers.main

class Name {
  var count = 0.0
  var name = ""
  
  def getName() : String = {
     return name
    }
  def getCount() : Double = {
     return count
    }
  def setName(_name:String)  {
     name = _name
    }
  def setCount(_count:Double)  {
     count = _count
    }
}