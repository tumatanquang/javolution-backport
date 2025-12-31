# Javolution Backport

Based on [Javolution 5.5.1](https://mvnrepository.com/artifact/javolution/javolution/5.5.1) source code, combined with [rawnet/javolution](https://github.com/rawnet/javolution).

Since version 5.4, the public methods of [`FastTable`](https://tumatanquang.github.io/javolution-backport/javolution/util/FastTable.html) and [`FastList`](https://tumatanquang.github.io/javolution-backport-apidocs/javolution/util/FastList.html) have been marked as `final` and cannot be overridden.

This project has removed `final` to allow overriding of the public methods of these two classes, so you can implement thread-safety in any way you want.

### WARNING:

- Since v5.6.6: The original `FastList` has been replaced with `FastChain`!
- Since v5.6.8: The original `FastChain` has been replaced with `FastSequence`!
- Since v5.6.9:
	- Undo class name `FastSequence` to `FastList`.
	- The abstract class `FastList` has been renamed to `MutableList`.
- Since v5.7.0:
	- Added the packages `javolution.util.concurrent` and `javolution.util.concurrent.locks` based on Doug Lea's implementation.
	- The following classes have been renamed:
		- `MutableList` → `FastAbstractList`.
		- `SharedCollectionImpl` → `FastSharedCollection`.
		- `UnmodifiableCollectionImpl` → `FastUnmodifiableCollection`.

## Suggestions for use:

- `ArrayList` can be replaced with `FastTable`.
- `LinkedList` can be replaced with `FastList`.
- Initialize `FastTable` / `FastList`:

```java
FastTable table = new FastTable();
FastList list = new FastList();
MutableList table = new FastTable();
MutableList list = new FastList();
```

## How to build?

To build, you need to use `ant`; see the [build.xml](https://github.com/tumatanquang/javolution-backport/blob/main/build.xml) file for details.