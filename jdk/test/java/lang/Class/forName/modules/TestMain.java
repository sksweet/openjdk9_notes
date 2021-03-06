/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import java.lang.reflect.Layer;
import java.lang.reflect.Method;
import java.lang.reflect.Module;

public class TestMain {
    public static void main(String[] args) throws Exception {
        Layer boot = Layer.boot();
        Module m1 = boot.findModule("m1").get();
        Module m2 = boot.findModule("m2").get();

        // find exported and non-exported class from a named module
        findClass(m1, "p1.A");
        findClass(m1, "p1.internal.B");
        findClass(m2, "p2.C");

        // find class from unnamed module
        ClassLoader loader = TestMain.class.getClassLoader();
        findClass(loader.getUnnamedModule(), "TestDriver");

        // check if clinit should not be initialized
        // compile without module-path; so use reflection
        Class<?> c = Class.forName(m1, "p1.Initializer");
        Method m = c.getMethod("isInited");
        Boolean isClinited = (Boolean) m.invoke(null);
        if (isClinited.booleanValue()) {
            throw new RuntimeException("clinit should not be invoked");
        }
    }

    static Class<?> findClass(Module module, String cn) {
        Class<?> c = Class.forName(module, cn);
        if (c == null) {
            throw new RuntimeException(cn + " not found in " + module);
        }
        if (c.getModule() != module) {
            throw new RuntimeException(c.getModule() + " != " + module);
        }
        return c;
    }
}
