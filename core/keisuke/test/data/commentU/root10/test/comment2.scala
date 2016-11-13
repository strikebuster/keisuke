object HelloWorld {
  def main(args: Array[String]): Unit = {
    val constVal = 1 // 定数
    var variableVal = 1 // 変数

    val intVal: Int = 1 // 型指定定数
    var intVar: Int = 1 // 型指定変数

    /* nest
    /* 基本的な型
       コメント */
    val longVal = 1L
    val floatVal = 1.0f
    val doubleVal = 1.0
    */
    val charVal = 'a'
    val stringVal = "string"
    val rawstringval = """I said "Hello." to /*him*/."""
    val nullVal = null

    // Any 型 (Scala の全ての親クラス)
    val a: Any = 1
    a.asInstanceOf[Int] // キャスト

    // 親クラス
    class MyClass {
      val prop1 = 123
      var prop2 = "abc"
      def method: Unit = println("MyClass")
    }

    // インスタンス化
    val obj = new MyClass
    println(obj.prop1) //=> 123
    obj.prop2 = "xyz" // 'var' にはセッターがある
    println(obj.prop2) //=> xyz

    // 子クラス
    class MySubClass extends MyClass {
      override val prop1 = 456 // val には 'override' が必要
      prop2 = "xyz" // var には 'override' は不要 (というよりは「上書きする」)
      override def method: Unit = { //メソッドのオーバーライド
        super.method // 親クラスは super で参照
        println("MySubClass")
      }
      final val prop3: String = "final でオーバーライドできなくする。"
    }
    val obj2 = new MySubClass
    obj2.method //=> MyClass\nMySubClass
    println(obj2.prop1) //=> 456
    println(obj2.prop2) //=> xyz
    println(obj2.prop3) //=> final でオーバーライドできなくする。
  }
}
