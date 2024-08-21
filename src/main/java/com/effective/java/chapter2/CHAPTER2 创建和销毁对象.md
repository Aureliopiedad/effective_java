# CHAPTER2: 创建和销毁对象

## 前言

本章节的主题是创建和销毁对象，包括：

- 何时以及如何创建对象
- 何时以及如何避免创建对象
- 如何确保对象能够适时销毁
- 如何管理对象销毁前必须进行的各种清理动作

## 第一条：用静态工厂方法替代构造器

静态工厂方法相信大家都见过了，举一个例子：

```java
public static Boolean valueOf(boolean b) {
	return (b ? TRUE : FALSE);
}
```

静态工厂方法有几点好处：

1. 有名称，方便阅读

   受制于Java方法签名，不同作用的构造器只能通过参数不同区分，但是静态工厂方法没有这个限制。

2. 可以避免创建新对象

   使得不可变类能够使用预先定义好的实例，或将实例缓存起来重复利用。

3. 能够返回子类型对象

4. 返回对象的类可以随着调用变化

   可以参考`EnumSet.noneOf(Class<E> elementType)`，随着传入参数的长度不一致，会返回两种子类型。

5. 返回对象所属的类，在编写静态工厂方法时可以不存在

   参考spi。

几个缺点:

1. 虽然作者的意思是构造器各种不好，但又不能没有，如果没有`public`或`protected`修饰的构造器，子类就不能被实例化，但是这点见仁见智吧，有些不可变类型可能就要这种。

2. 不像构造器那么直接能被调用者发现。为了弥补这个缺陷，有一些私有工厂方法的命名规范:

   1. 类型转化方法：`from`，单个参数，用于返回该类型一个对应的实例。

      ```java
      Date date = Date.from(Instant.now());
      ```

   2. 聚合方法：`of`，多个参数，返回该类型一个对应的实例，用于把参数合并起来。

      ```java
      List<String> list = List.of("1", "2");
      ```

   3. 用于替代`of`和`from`的方法：`valueOf`

   4. 通过可能有的参数返回实例：`instance`或`getInstance`

   5. 确保每次返回一个新的实例：`createInstance`或`newInstance`

   6. `get{Type}`，{Type}表示返回的对象类型，类似于`getInstance`

      ```java
      FileStore test = Files.getFileStore(Path.of("test"));
      ```

   7. `new{Type}`，{Type}表示返回的对象类型，类似于`newInstance`

      ```java
      BufferedReader test = Files.newBufferedReader(Path.of("test"));
      ```

   8. `{Type}`，精简版

      ```java
      List<String> list = Collections.list(Test.INSTANCE);
      ```

## 第二条：遇到多个构造器参数时要考虑使用构建器

不说明了，使用的比较多。

## 第三条：用私有构造器或者枚举类强化Singleton属性

实现Singleton有两种常见的方法：

1. 保持构造器私有并导出公有的final修饰的静态成员

   ```java
   class Singleton1 {
       public static final Singleton1 INSTANCE = new Singleton1();
   
       private Singleton1() {}
   
       public String call() {
           return "singleton1";
       }
   }
   ```

2. 保持构造器私有并导出公有的静态工厂方法

   ```java
   class Singleton2 {
       private static final Singleton2 INSTANCE = new Singleton2();
   
       private Singleton2() {}
   
       public Singleton2 getInstance() {
           return INSTANCE;
       }
   
       public String call() {
           return "singleton2";
       }
   }
   ```

为了将Singleton实现序列化(Serializable)，需要`readresolve()`方法来干预序列化，确保反序列化后和序列化之前的对象的内存地址相同。
