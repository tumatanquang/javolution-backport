# Javolution Backport

Based on [Javolution 5.5.1](https://mvnrepository.com/artifact/javolution/javolution/5.5.1) source code, combined with [rawnet/javolution](https://github.com/rawnet/javolution).

Since version 5.4, the public methods of [`FastTable`](https://tumatanquang.github.io/javolution-backport/javolution/util/FastTable.html) and [`FastList`](https://tumatanquang.github.io/javolution-backport-apidocs/javolution/util/FastList.html) have been marked as `final` and cannot be overridden.

This project has removed `final` to allow overriding of the public methods of these two classes, so you can implement thread-safety in any way you want.

### WARNING:

- Since v5.6.6: The original `FastList` has been replaced with `FastChain`.
- Since v5.6.8: The original `FastChain` has been replaced with `FastSequence`.
- Since v5.6.9:
	- Undo class name `FastSequence` to `FastList`.
	- The abstract class `FastList` has been renamed to `MutableList`.
- Since v5.7.0:
	- Added the packages `javolution.util.concurrent` and `javolution.util.concurrent.locks` based on [Doug Lea's implementation](https://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html).
	- The following classes have been renamed:
		- `MutableList` → `FastAbstractList`.
		- `SharedCollectionImpl` → `FastSharedCollection`.
		- `UnmodifiableCollectionImpl` → `FastUnmodifiableCollection`.
- Since v5.7.1:
	- Added the `javolution.util.primitive` package: A collection of implementations for `FastList` and `FastMap` for primitive data types.
	- Reimplemented for J2ME; when compiled with `ant j2me`, it will default to using `midp_2.0.jar` (MIDP 2.0) and `cldc_1.1.jar` (CLDC 1.1) for the classpath.
- Since v5.7.2:
	- Optimized the `FastTable` source code.
	- For classes in the `javolution.util.primitive` package:
		+ Classes have been renamed to the format `Fast<Datatype>ArrayList`.
		+ The source code has been optimized.
		+ It will now throw an `ArrayIndexOutOfBoundsException` instead of `IndexOutOfBoundsException` when attempting to access an invalid index position.
		+ Renamed the `delete()` method to `removeElement()`.
		+ Removed the ~~`removeRange()`~~ method.

## Suggestions for use:

- `ArrayList` can be replaced with `FastTable`.
- `LinkedList` can be replaced with `FastList`.
- Initialize `FastTable` / `FastList`:

```java
FastTable table = new FastTable();
FastList list = new FastList();
FastAbstractList table = new FastTable();
FastAbstractList list = new FastList();
```

## How to build?

To build, you need to use `ant`; see the [build.xml](https://github.com/tumatanquang/javolution-backport/blob/main/build.xml) file for details.