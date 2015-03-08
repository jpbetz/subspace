Subspace
========

Lightweight vector and matrix math library for OpenGL programming in Scala.

Inspired by [glm](http://glm.g-truc.net/0.9.6/index.html) Subspace makes the vector and matrix computations that needs
to be performed on the CPU a bit easier by providing features that graphics programmers are familiar with from shader
programming (like swizzle operators) as well as a complete set of functions for graphics programming,  including
functions that have been deprecated by OpenGL such as `glRotate`, `gluPerspective` and `gluLookAt`.

This contains core classes such as Vector3, Matrix4x4 and Quaternion.  It also
provides [swizzling](https://www.opengl.org/wiki/Data_Type_%28GLSL%29#Swizzling) and
[convenience constructors](https://www.opengl.org/wiki/Data_Type_%28GLSL%29#Vector_constructors).

To minimize it's footprint, this library has no dependencies.  Is intended for use with OpenGL, via bindings such as
LWJGL.

Includes
--------

Vector and matrix classes:  Vector2, Vector3, Vector4, Matrix3x3, Matrix4x4, Quaternion

Swizzle operators and vector convenience constructors work the same as in GLSL.  E.g.:

```
Vector4(0, vec2.yx, 0) == Vector4(0, vec2.y, vec2.x, 0)
vec4.xz == Vector2(vec4.x, vec4.z)
```

Implementations of functions deprecated from OpenGL:

```
val perspectiveMatrix = Matrix4x4.forPerspective(...)
```

ByteBuffer writers to ease integration with low level OpenGL APIs for the JVM such as LWJGL

```
val cameraPosition = Vector4(10, 10, 5, 0).allocateBuffer()
```

OpenGL Programming in Scala
---------------------------

Subspace works well with LWJGL, a library providing access the full OpenGL API from the JVM.

This library can be used with version 2 and 3 of LWJGL.  While LWJGL 2 provides a utility library with vector
and matrix classes, it is rather incomplete.  And in LWJGL 3, they are removing the utility library entirely.

To use this library with LWJGL,  simply build whatever types are needed and then use the toBuffer methods to produce the
ByteBuffers needed by LWJGL.  E.g.:

    val modelViewMatrix = Matrix4x4.forTranslationRotationScale(...)
    glUniformMatrix4("modelViewMatrix", false, modelViewMatrix.toBuffer)

Design
-----

Goals:

* Be good at one thing.  Provide the vector and matrix types needed to program modern against a modern graphics pipeline, and nothing else.
* Provide the best features from the vector and matrix types in shader languages like GLSL.
* Consistent and complete.  Similar libraries for other languages have been studied to make sure all the convenience operations developers expect have been included.
* Scala idomatic. Immutable case classes for all vector and matrix types. Carefully defined operator overloading for natural looking mathematical expressions.
* Minimal footprint.  No dependencies.

Non-goals:

* Provide Scala bindings for OpenGL
* Write a graphics/game engine
* Write a general purpose linear algebra library

Current Limitations
-------------------

* All types are currently reference types.  This has negative performance implications.  While scala does allow
  stack allocated value types to be defined by extending AnyVal,  AnyVal can only be used for single field types, not
  multi field types like Vector2.  It is not clear if Scala will ever support stack allocated value types that could
  be used for my purposes.   Maybe if/when [Java adds value types](http://cr.openjdk.java.net/~jrose/values/values-0.html),
  scala will provide a way to stack allocate these types.

TODO
----

[ ] Finish up test suite
[ ] Implement projection/reflection convenience methods on Vector3
[ ] Flesh out scaladoc
[ ] Add Color and UV coordinate related conveniences.  Might be as simple as adding swizzle operators (rgba, stpq) to Vector3 and Vector4.

References
----------

* http://glm.g-truc.net/0.9.6/index.html
* https://github.com/ra4king/LWJGL-OpenGL-Utils/tree/master/src/com/ra4king/opengl/util/math
* http://developer.android.com/reference/android/opengl/Matrix.html
* http://docs.unity3d.com/ScriptReference/index.html
* https://github.com/mrdoob/three.js/tree/master/src/math
* http://cr.openjdk.java.net/~jrose/values/values-0.html


