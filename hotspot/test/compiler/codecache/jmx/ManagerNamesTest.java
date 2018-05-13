/*
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates. All rights reserved.
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

import jdk.test.lib.Asserts;
import java.lang.management.MemoryPoolMXBean;
import sun.hotspot.code.BlobType;

/**
 * @test ManagerNamesTest
 * @library /testlibrary /test/lib
 * @modules java.base/jdk.internal.misc
 * @modules java.management
 * @build ManagerNamesTest
 * @run main ClassFileInstaller sun.hotspot.WhiteBox
 *     sun.hotspot.WhiteBox$WhiteBoxPermission
 * @run main/othervm -Xbootclasspath/a:. -XX:+UnlockDiagnosticVMOptions
 *     -XX:+WhiteBoxAPI -XX:+SegmentedCodeCache ManagerNamesTest
 * * @run main/othervm -Xbootclasspath/a:. -XX:+UnlockDiagnosticVMOptions
 *     -XX:+WhiteBoxAPI -XX:-SegmentedCodeCache ManagerNamesTest
 * @summary verify getMemoryManageNames calls in case of segmented code cache
 */
public class ManagerNamesTest {

    private final MemoryPoolMXBean bean;
    private final static String POOL_NAME = "CodeCacheManager";

    public static void main(String args[]) {
        for (BlobType btype : BlobType.getAvailable()) {
            new ManagerNamesTest(btype).runTest();
        }
    }

    public ManagerNamesTest(BlobType btype) {
        bean = btype.getMemoryPool();
    }

    protected void runTest() {
        String[] names = bean.getMemoryManagerNames();
        Asserts.assertEQ(names.length, 1,
                "Unexpected length of MemoryManagerNames");
        Asserts.assertEQ(POOL_NAME, names[0],
                "Unexpected value of MemoryManagerName");
        System.out.printf("INFO: Scenario finished successfully for %s%n",
                bean.getName());
    }
}